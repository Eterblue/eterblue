package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealDishMapper;
import com.sky.result.PageResult;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;
    @Autowired
    private SetMealDishMapper setMealDishMapper;

    @Override
    @Transactional
    public void save(DishDTO dishDTO) {
        //新增菜品
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);

        dishMapper.insert(dish);
        //获取菜品id
        Long dishId=dish.getId();
        //添加菜品口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors !=null && !flavors.isEmpty()){

            flavors.stream().forEach(dishFlavor -> {dishFlavor.setDishId(dishId);});
            dishFlavorMapper.insert(flavors);
        }
    }

    @Override
    public PageResult page(DishPageQueryDTO dishPageQueryDTO) {

        PageHelper.startPage(dishPageQueryDTO.getPage(), dishPageQueryDTO.getPageSize());
        Page<DishVO> dishes=dishMapper.search(dishPageQueryDTO);

        long total = dishes.getTotal();
        List<DishVO> result = dishes.getResult();

        return new PageResult(total,result);
    }

    @Override
    public DishVO getById(Long id) {

        //根据id查询出dish
        Dish dish=dishMapper.getById(id);
        //查询dish的口味
        List<DishFlavor> list=dishFlavorMapper.dishById(id);

        DishVO dishVO=new DishVO();
        BeanUtils.copyProperties(dish,dishVO);
        dishVO.setFlavors(list);

        return dishVO;
    }

    @Override
    public void update(DishDTO dishDTO) {

        //修改菜品
        Dish dish=new Dish();
        BeanUtils.copyProperties(dishDTO,dish);
        dishMapper.update(dish);
        //修改菜品口味
        //注意，须先把口味删除再添加
        List<Long> dishID=new ArrayList<>();
        dishID.add(dish.getId());
        dishFlavorMapper.deleteBydishID(dishID);
        //删除后重新添加菜品口味
        //需要判断菜品口味是否为空
        //添加菜品口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if(flavors !=null && !flavors.isEmpty()){
            flavors.stream().forEach(dishFlavor -> {dishFlavor.setDishId(dish.getId());});
            dishFlavorMapper.insert(flavors);
        }
    }

    @Override
    public void startORend(Integer status, Long id) {
        Dish dish= Dish.builder()
                .status(status)
                .id(id)
                .build();
        dishMapper.update(dish);
    }


    /**
     * 批量删除菜品
     * @param ids
     */
    @Override
    @Transactional
    public void delete(List<Long> ids) {
        //菜品是否起售
        for (Long id : ids) {
            Dish dish=dishMapper.getById(id);
            if (dish.getStatus().equals(StatusConstant.ENABLE)){
                throw new DeletionNotAllowedException(MessageConstant.DISH_ON_SALE);
            }
        }
        //菜品是否关联套餐
        Integer count = setMealDishMapper.getByDishID(ids);
        if (count >0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        //菜品删除
        dishMapper.delete(ids);
        //口味删除
        dishFlavorMapper.deleteBydishID(ids);
    }

    @Override
    public List<Dish> getByCategoryId(Long categoryId) {
        Dish dish= Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();
        return dishMapper.getByCategoryId(dish);
    }
}
