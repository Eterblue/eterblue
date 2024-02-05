package com.sky.controller.admin;


import com.sky.annotation.AutoFill;
import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.enumeration.OperationType;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/dish")
@Api(tags ="菜品管理")
@Slf4j
public class DishController {

    @Autowired
    private DishService dishService;

    @ApiOperation("新增菜品")
    @PostMapping
    public Result save(@RequestBody DishDTO dishDTO){

        log.info("新增菜品:{}",dishDTO);
        dishService.save(dishDTO);

        return Result.success();
    }

    @ApiOperation("分页查询")
    @GetMapping("/page")
    public Result<PageResult> page( DishPageQueryDTO dishPageQueryDTO){

        log.info("分页查询:{}",dishPageQueryDTO);

        PageResult result=dishService.page(dishPageQueryDTO);
        return Result.success(result);
    }

    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getById(@PathVariable Long id){

        log.info("根据id查询:{}",id);
        DishVO dishVO=dishService.getById(id);

        return Result.success(dishVO);
    }

    @ApiOperation("修改菜品")
    @PutMapping
    public Result update(@RequestBody DishDTO dishDTO){

        log.info("修改菜品：{}",dishDTO);
        dishService.update(dishDTO);

        return Result.success();
    }

    @ApiOperation("修改起售停售状态")
    @PostMapping("/status/{status}")
    public Result startORend(@PathVariable Integer status,Long id){
        log.info("修改状态:{},id;{}",status,id);
        dishService.startORend(status,id);

        return Result.success();
    }

    @DeleteMapping
    @ApiOperation("删除菜品")
    public Result delete(@RequestParam List<Long> ids){

        log.info("删除菜品:{}",ids);
        dishService.delete(ids);

        return Result.success();
    }


    @ApiOperation("根据分类查询菜品")
    @GetMapping("/list")
    public Result<Dish> getByCategoryId(Long categoryId){

        log.info("根据分类id查询菜品:{}",categoryId);
        Dish dish=dishService.getByCategoryId(categoryId);

        return Result.success(dish);
    }
}
