package com.sky.mapper;

import com.sky.annotation.AutoFill;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper {
    Integer getByCategory(Integer id);

    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);
}
