package com.dzr.service;

import com.dzr.po.City;
import com.dzr.po.User;

/**
 * @author dingzr
 * @Description
 * @ClassName UserService
 * @since 2017/7/4 13:51
 */
public interface UserService {

    /**
     * 用来测试事务的回滚，使用重复主键ID=1
     * 回滚之后数据库自增长的id依然是增加的
     * @return
     */
    public User checkTransactional();

    /**
     * 获取用户信息
     * @param id
     * @return
     */
    public User getUser(Integer id);

    /**
     * 获取城市信息
     * @param id
     * @return
     */
    public City getCity(Integer id);

    /**
     * 获取用户信息以及相关联的城市信息
     * @param userId
     * @param cityId
     * @return
     */
    public User getUserCityInfo(Integer userId, Integer cityId);

}
