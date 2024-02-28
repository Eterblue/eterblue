package com.sky.controller.user;

import com.sky.constant.StatusConstant;
import com.sky.entity.Dish;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Api(tags = "C端菜品接口")
@RequestMapping("/user/dish/")
@RestController("userDishController")
public class DishController {

    @Autowired
    private DishService dishService;
    @Autowired
    private RedisTemplate redisTemplate;

    @ApiOperation("根据分类查询菜品")
    @GetMapping("/list")
    public Result<List<DishVO>> list(Long categoryId){
        Dish dish= Dish.builder()
                .categoryId(categoryId)
                .status(StatusConstant.ENABLE)
                .build();

        log.info("根据分类id查询菜品：{}",categoryId);
        //设置动态分类id
        String key="dish_"+categoryId;
        //从redis获取菜品集合
        List<DishVO> list = (List<DishVO>) redisTemplate.opsForValue().get(key);
        //如果list不为空，返回结果
        if (list !=null && list.size()>0){
            return Result.success(list);
        }
        //list为空,查询数据库，并把查询结果添加到redis中
        list=dishService.listWithFlavor(dish);
        redisTemplate.opsForValue().set(key,list);

        return Result.success(list);
    }
}
