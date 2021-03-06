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

    /**
     * 用来测试事务的回滚，使用重复主键ID=1
     * 回滚之后数据库自增长的id依然是增加的
     * @return
     */
    public User checkTransactional(){
        User user = new User();
        user.setAddress("测试事务回滚的数据插入before");
        user.setAge(24);
        insert(user);

        //用来测试事务的回滚，回滚之后数据库自增长的id依然是增加的
        user.setId(1);
        user.setAddress("测试事务回滚的真实数据数据插入");
        user.setAge(25);
        insert(user);
        return user;
    }

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    public User getUser(Integer id){
        return userMapper.selectByPrimary(id);
    }

    /**
     * 获取城市信息
     * @param id
     * @return
     */
    public City getCity(Integer id){
        return cityMapper.selectByPrimary(id);
    }

    /**
     * 获取用户信息以及相关联的城市信息
     * @param userId
     * @param cityId
     * @return
     */
    public User getUserCityInfo(Integer userId, Integer cityId){
        User user = userMapper.selectByPrimary(userId);
        if(null != user){
            user.setCity(cityMapper.selectByPrimary(cityId));
        }
        return user;
    }

}
