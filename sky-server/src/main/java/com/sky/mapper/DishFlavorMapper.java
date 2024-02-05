package com.sky.mapper;

import com.sky.entity.DishFlavor;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DishFlavorMapper {

    void insert(List<DishFlavor> flavors);

    List<DishFlavor> dishById(Long id);

    void upodate(List<DishFlavor> flavors);

    void deleteBydishID(List<Long> ids);
}
