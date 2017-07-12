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

@Mapper
@Component
public interface UserMapper {

    int insert(User user);

    User selectByPrimary(Integer id);

}
