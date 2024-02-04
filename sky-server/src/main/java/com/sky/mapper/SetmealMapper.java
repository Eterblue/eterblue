package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SetmealMapper {
    Integer getByCategory(Integer id);
}
