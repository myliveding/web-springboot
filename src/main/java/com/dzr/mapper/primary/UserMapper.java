package com.dzr.mapper.primary;

import com.dzr.po.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author dingzr
 * @Description
 * @ClassName CityMapper
 * @since 2017/7/5 13:52
 */

@Component
public interface UserMapper {

    public User selectByPrimary(Integer id);

}
