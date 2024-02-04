package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Api("分类相关接口")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryMapper categoryMapper;
    @Autowired
    DishMapper dishMapper;
    @Autowired
    SetmealMapper setmealMapper;

    @Override
    public PageResult page(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(),categoryPageQueryDTO.getPageSize());

        Page<Category> categories=categoryMapper.pagequery(categoryPageQueryDTO);
        long total = categories.getTotal();
        List<Category> result = categories.getResult();

        return new PageResult(total,result);
    }

    @Override
    public List<Category> search(Integer type) {

        List<Category> list=categoryMapper.list(type);
        return list;
    }

    @Override
    public void save(CategoryDTO categoryDTO) {

        Category category=new Category();
        BeanUtils.copyProperties(categoryDTO,category);
        category.setId(null);
        category.setStatus(StatusConstant.DISABLE);
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());

        categoryMapper.insert(category);
    }

    @Override
    public void delete(Integer id) {
        //判断该分类下是否有菜品或是套餐
        //若有，则该分类无法删除
        Integer count=dishMapper.getByCategory(id);
        if (count>0){
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        }
        count =setmealMapper.getByCategory(id);
        if(count>0){
            throw new DeletionNotAllowedException(MessageConstant.DISH_BE_RELATED_BY_SETMEAL);
        }
        categoryMapper.delete(id);
    }

    @Override
    public void startORend(Integer status, Long id) {

        Category category= Category.builder()
                .status(status)
                .id(id)
                .build();

        categoryMapper.update(category);
    }

    @Override
    public void update(CategoryDTO categoryDTO) {

        Category category=new Category();

        BeanUtils.copyProperties(categoryDTO,category);

        category.setUpdateUser(BaseContext.getCurrentId());
        category.setUpdateTime(LocalDateTime.now());

        categoryMapper.update(category);
    }
}
