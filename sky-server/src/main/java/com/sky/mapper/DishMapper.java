package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.vo.DishVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishMapper {
    Integer getByCategory(Long id);

    @AutoFill(value = OperationType.INSERT)
    void insert(Dish dish);

    Page<DishVO> search(DishPageQueryDTO dishPageQueryDTO);

    Dish getById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Dish dish);

    void delete(List<Long> ids);

    List<Dish> getByCategoryId(Dish dish);
}
