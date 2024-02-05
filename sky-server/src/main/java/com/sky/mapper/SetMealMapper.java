package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.annotation.AutoFill;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.entity.Setmeal;
import com.sky.enumeration.OperationType;
import com.sky.vo.SetmealVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface SetMealMapper {
    Integer getByCategory(Long id);

    @AutoFill(value = OperationType.INSERT)
    void insert(Setmeal setmeal);

    Page<SetmealVO> pageSearch(SetmealPageQueryDTO setmealPageQueryDTO);

    void delete(List<Long> ids);

    Setmeal getById(Long id);

    @AutoFill(value = OperationType.UPDATE)
    void update(Setmeal setmeal);
}
