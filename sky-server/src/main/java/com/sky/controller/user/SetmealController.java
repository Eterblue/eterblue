package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Setmeal;
import com.sky.entity.SetmealDish;
import com.sky.result.Result;
import com.sky.service.SetMealServie;
import com.sky.vo.DishItemVO;
import com.sky.vo.SetmealVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController("userSetmealController")
@RequestMapping("/user/setmeal")
@Api(tags = "C端套餐接口")
public class SetmealController {

    @Autowired
    SetMealServie setMealServie;

    @ApiOperation("根据分类id查询套餐")
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Long categoryId){

        log.info("根据分类id查询套餐：{}",categoryId);
        Setmeal setmeal= Setmeal.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();

        List<Setmeal> list=setMealServie.list(setmeal);

        return Result.success(list);
    }

    @ApiOperation("根据套餐id查询包含的菜品")
    @GetMapping("/dish/{id}")
    public Result<List<DishItemVO>> DishList(@PathVariable Long id){

        log.info("根据id查询包含的菜品：{}",id);
        List<DishItemVO> list=setMealServie.listWithDish(id);

        return Result.success(list);
    }
}
