package com.sky.service.impl;

import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private DishFlavorMapper dishFlavorMapper;

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
}
