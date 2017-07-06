package com.dzr.service.impl;

import com.dzr.mapper.primary.UserMapper;
import com.dzr.mapper.secondary.CityMapper;
import com.dzr.po.City;
import com.dzr.po.User;
import com.dzr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * @author dingzr
 * @Description
 * @ClassName UserServiceImpl
 * @since 2017/7/4 13:51
 */

@Service("userService")
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;

    @Autowired
    CityMapper cityMapper;



    public User getUserInfo(Integer id){
        return userMapper.selectByPrimary(id);
    }

    public City getCityInfo(Integer id){
        return cityMapper.selectByPrimary(id);
    }

}
