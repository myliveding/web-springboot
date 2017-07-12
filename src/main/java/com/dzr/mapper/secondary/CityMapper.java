package com.dzr.mapper.secondary;

import com.dzr.po.City;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

/**
 * @author dingzr
 * @Description
 * @ClassName CityMapper
 * @since 2017/7/5 13:52
 */

@Mapper
@Component
public interface CityMapper {

    City selectByPrimary(Integer id);

}
