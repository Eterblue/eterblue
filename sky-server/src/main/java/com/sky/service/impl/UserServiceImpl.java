package com.sky.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.sky.constant.MessageConstant;
import com.sky.dto.UserLoginDTO;
import com.sky.entity.User;
import com.sky.exception.LoginFailedException;
import com.sky.mapper.UserMapper;
import com.sky.properties.WeChatProperties;
import com.sky.service.UserService;
import com.sky.utils.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl implements UserService {

    private static final String wx_LOGIN="https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private WeChatProperties weChatProperties;

    @Override
    public User wxlogin(UserLoginDTO userLoginDTO) {
        //调用当前用户的openid，如果为空，则登录失败

        String openid = getOpenId(userLoginDTO.getCode());
        if (openid ==null){
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        User user=userMapper.wxlogin(openid);

        //判断该用户是否为新用户
        if(user == null){
            //自动注册,并添加
            user= User.builder()
                    .createTime(LocalDateTime.now())
                    .openid(openid)
                    .build();
            userMapper.insert(user);
        }


        return user;
    }
    private  String getOpenId(String code){
        Map<String,String> map=new HashMap<>();
        map.put("appid",weChatProperties.getAppid());
        map.put("secret",weChatProperties.getSecret());
        map.put("js_code",code);
        map.put("grant_type","authorization_code");

        String response = HttpClientUtil.doGet(wx_LOGIN, map);

        JSONObject object = JSON.parseObject(response);
        String openid = object.getString("openid");
        return openid;
    }
}
