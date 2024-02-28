package com.sky.service.impl;

import com.sky.context.BaseContext;
import com.sky.dto.ShoppingCartDTO;
import com.sky.entity.Dish;
import com.sky.entity.Setmeal;
import com.sky.entity.ShoppingCart;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetMealMapper;
import com.sky.mapper.ShoppingCartMapper;
import com.sky.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {

    @Autowired
    private ShoppingCartMapper shoppingCartMapper;
    @Autowired
    private DishMapper dishMapper;
    @Autowired
    private SetMealMapper setMealMapper;

    @Override
    public void addShoppingCart(ShoppingCartDTO shoppingCartDTO) {

        //设置购物车信息
        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //从线程中获得userid
        Long userId= BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //根据信息查询
        List<ShoppingCart> list=shoppingCartMapper.list(shoppingCart);

        //如果cart不为空,则number+1
        if (!list.isEmpty()){
            ShoppingCart cart = list.get(0);
            cart.setNumber(cart.getNumber()+1);
            shoppingCartMapper.updateNumber(cart);
        }
        else {
            //如果list为空,需要获取菜品或套餐信息
            Long dishId = shoppingCart.getDishId();
            Long setmealId = shoppingCart.getSetmealId();
            if(dishId != null){
                //获取菜品信息
                Dish dish = dishMapper.getById(dishId);
                shoppingCart.setImage(dish.getImage());
                shoppingCart.setName(dish.getName());
                shoppingCart.setAmount(dish.getPrice());

            }
            else {
                //获取套餐信息
                Setmeal setmeal = setMealMapper.getById(setmealId);
                shoppingCart.setImage(setmeal.getImage());
                shoppingCart.setName(setmeal.getName());
                shoppingCart.setAmount(setmeal.getPrice());
            }
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCart.setNumber(1);
            shoppingCartMapper.insert(shoppingCart);
        }
    }

    @Override
    public List<ShoppingCart> list() {

        ShoppingCart shoppingCart= ShoppingCart.builder()
                .userId(BaseContext.getCurrentId())
                .build();
        return shoppingCartMapper.list(shoppingCart);
    }

    @Override
    public void clean() {
        Long userId = BaseContext.getCurrentId();
        shoppingCartMapper.delete(userId);

    }

    @Override
    public void update(ShoppingCartDTO shoppingCartDTO) {

        ShoppingCart shoppingCart=new ShoppingCart();
        BeanUtils.copyProperties(shoppingCartDTO,shoppingCart);
        //从线程中获得userid
        Long userId= BaseContext.getCurrentId();
        shoppingCart.setUserId(userId);
        //根据信息查询
        List<ShoppingCart> list=shoppingCartMapper.list(shoppingCart);
        if (!list.isEmpty()){
            ShoppingCart cart = list.get(0);
            Integer number = cart.getNumber();
            if(number == 1){
                shoppingCartMapper.deleteById(cart.getId());
            }
            else {
                cart.setNumber(number-1);
                shoppingCartMapper.updateNumber(cart);
            }
        }
    }
}
