package com.dzr.service.impl;

import com.dzr.mapper.primary.UserMapper;
import com.dzr.mapper.secondary.CityMapper;
import com.dzr.po.City;
import com.dzr.po.User;
import com.dzr.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.activation.DataSource;


/**
 * @author dingzr
 * @Description
 * @ClassName UserServiceImpl
 * @since 2017/7/4 13:51
 */

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{

    @Autowired
    UserMapper userMapper;

    @Autowired
    CityMapper cityMapper;

    public void insert(User user){
        userMapper.insert(user);
    }

    public User getUserInfo(Integer id){
        User user = new User();
        user.setAddress("111");
        insert(user);

        //用来测试事务的回滚，回滚之后数据库自增长的id依然是增加的
//        user.setId(1);
//        user.setAddress("222");
//        insert(user);
        return userMapper.selectByPrimary(id);
    }

    public City getCityInfo(Integer id){
        return cityMapper.selectByPrimary(id);
    }

}
