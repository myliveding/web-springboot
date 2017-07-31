package com.dzr.mapper.primary;

import com.dzr.po.WechatToken;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public interface WechatTokenMapper {

    int deleteByPrimaryKey(Integer id);

    int insert(WechatToken record);

    int insertSelective(WechatToken record);

    WechatToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WechatToken record);

    int updateByPrimaryKey(WechatToken record);

    WechatToken selectTokenByAppId(Map<String, String> map);

    int updateByAppId(WechatToken record);
}