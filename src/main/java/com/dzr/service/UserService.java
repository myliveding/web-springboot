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

    public User getUserInfo(Integer id);
    public City getCityInfo(Integer id);

}
