package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.vo.DishItemVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealDishMapper {
    Integer getByDishID(List<Long> ids);

    void insert(List<SetmealDish> setmealDishes);

    void delete(List<Long> ids);

    List<SetmealDish> getBySetMealId(Long id);

    List<DishItemVO> getDishItemVO(Long id);
}
