package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/category")
@Slf4j
@Api(tags = "分类相关管理")
public class CategoryController {


    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    @ApiOperation("分类分页查询")
    public Result<PageResult> page(CategoryPageQueryDTO categoryPageQueryDTO){

        log.info("分类分页查询：{}",categoryPageQueryDTO);
        PageResult result=categoryService.page(categoryPageQueryDTO);
        return Result.success(result);
    }

    @GetMapping("/list")
    @ApiOperation("根据类型查询")
    public Result<List<Category>> typeSearch(Integer type){

        log.info("查询类型:{}",type);
        List<Category> category=categoryService.search(type);
        return Result.success(category);
    }

    @PostMapping
    @ApiOperation("新增分类")
    public Result<Category> save(@RequestBody CategoryDTO categoryDTO){
        log.info("新增分类:{}",categoryDTO);
        categoryService.save(categoryDTO);
        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除分类")
    public Result delete(Integer id){
        log.info("删除分类，id为:{}",id);
        categoryService.delete(id);
        return Result.success();
    }

    @PostMapping("/status/{status}")
    @ApiOperation("启用禁用分类")
    public Result startORend (@PathVariable Integer status,Long id){

        log.info("修改状态为：{},id:{}",status,id);
        categoryService.startORend(status,id);
        return Result.success();
    }

    @PutMapping
    @ApiOperation("修改分类")
    public Result update(@RequestBody CategoryDTO categoryDTO){
        log.info("修改分类:{}",categoryDTO);
        categoryService.update(categoryDTO);
        return Result.success();
    }
}
