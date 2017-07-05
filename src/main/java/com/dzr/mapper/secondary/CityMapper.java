package com.dzr.mapper.secondary;

import com.dzr.po.City;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author dingzr
 * @Description
 * @ClassName CityMapper
 * @since 2017/7/5 13:52
 */

@Component
public interface CityMapper {

    public City selectByPrimary(Integer id);

}
