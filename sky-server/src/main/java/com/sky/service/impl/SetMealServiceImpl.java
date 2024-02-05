package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.result.PageResult;
import com.sky.service.SetMealServie;
import com.sky.vo.SetmealVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
public class SetMealServiceImpl implements SetMealServie {

    @Autowired
    private SetMealMapper setMealMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;
    @Autowired
    private DishMapper dishMapper;

    @Override
    @Transactional
    public void save(SetmealDTO setmealDTO) {
        //添加套餐基本信息
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setMealMapper.insert(setmeal);
        //添加套餐中的菜品
        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.stream().forEach(setmealDish -> {setmealDish.setSetmealId(setmeal.getId());});
        setMealDishMapper.insert(setmealDishes);
    }

    @Override
    public PageResult page( SetmealPageQueryDTO setmealPageQueryDTO) {
        PageHelper.startPage(setmealPageQueryDTO.getPage(),setmealPageQueryDTO.getPageSize());

        Page<SetmealVO> page=setMealMapper.pageSearch(setmealPageQueryDTO);
        long total = page.getTotal();
        List<SetmealVO> result = page.getResult();

        return new PageResult(total,result);
    }

    @Override
    @Transactional
    public void delete(List<Long> ids) {
        //在售套餐不能删除
        ids.stream().forEach(id->{
            Setmeal setmeal=setMealMapper.getById(id);
            if(setmeal.getStatus().equals(StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ON_SALE);
            }
        });
        //删除套餐
        setMealMapper.delete(ids);
        //删除套餐菜品关系
        setMealDishMapper.delete(ids);
    }

    @Override
    public void update(SetmealDTO setmealDTO) {
        //更新套餐
        Setmeal setmeal=new Setmeal();
        BeanUtils.copyProperties(setmealDTO,setmeal);
        setMealMapper.update(setmeal);
        //更新套餐菜品关系
        //获取套餐的主键id
        Long id = setmealDTO.getId();
        List<Long> ids=new ArrayList<>();
        ids.add(id);
        setMealDishMapper.delete(ids);

        List<SetmealDish> setmealDishes = setmealDTO.getSetmealDishes();
        setmealDishes.stream().forEach(setmealDish -> {setmealDish.setSetmealId(id);});
        setMealDishMapper.insert(setmealDishes);

    }

    @Override
    public SetmealVO getById(Long id) {

        Setmeal setmeal = setMealMapper.getById(id);
        SetmealVO setmealVO=new SetmealVO();
        BeanUtils.copyProperties(setmeal,setmealVO);

        List<SetmealDish> setmealDishes=setMealDishMapper.getBySetMealId(id);

        setmealVO.setSetmealDishes(setmealDishes);

        return setmealVO;
    }

    @Override
    @Transactional
    public void startORend(Integer status, Long id) {
        //当套餐转为起售时
        //判断套餐内的菜品是否存在未售

        if(status == StatusConstant.DISABLE){
            List<SetmealDish> setmealDishes = setMealDishMapper.getBySetMealId(id);
            setmealDishes.stream().forEach(setmealDish -> {
                Long dishId = setmealDish.getDishId();
                Dish dish = dishMapper.getById(dishId);
                if(dish.getStatus()==StatusConstant.DISABLE){
                    throw new DeletionNotAllowedException(MessageConstant.SETMEAL_ENABLE_FAILED);
                }
            });
        }

        Setmeal setmeal= Setmeal.builder()
                .status(status)
                .id(id)
                .build();

        setMealMapper.update(setmeal);
    }
}
