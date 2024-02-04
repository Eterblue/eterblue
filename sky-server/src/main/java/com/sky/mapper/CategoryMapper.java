package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CategoryMapper {

    Page<Category> pagequery(CategoryPageQueryDTO categoryPageQueryDTO);


    List<Category> list(Integer type);

    void insert(Category category);

    void delete(Integer id);

    void update(Category category);
}
