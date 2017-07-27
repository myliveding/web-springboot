package com.dzr.mapper.primary;

import com.dzr.po.WechatToken;

public interface WechatTokenMapper {
    int deleteByPrimaryKey(String appid);

    int insert(WechatToken record);

    int insertSelective(WechatToken record);

    WechatToken selectByPrimaryKey(String appid);

    int updateByPrimaryKeySelective(WechatToken record);

    int updateByPrimaryKey(WechatToken record);
}