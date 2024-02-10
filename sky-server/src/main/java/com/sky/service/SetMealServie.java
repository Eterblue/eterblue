package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.result.PageResult;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;

import java.util.List;

public interface SetMealServie {
    void save(SetmealDTO setmealDTO);

    PageResult page(SetmealPageQueryDTO setmealPageQueryDTO);

    void delete(List<Long> ids);

    void update(SetmealDTO setmealDTO);

    SetmealVO getById(Long id);

    void startORend(Integer status, Long id);

    List<Setmeal> list(Setmeal setmeal);

    List<DishItemVO> listWithDish(Long id);
}
