package com.dzr.mapper.primary;

import com.dzr.po.JsapiTicket;

public interface JsapiTicketMapper {

    int deleteByPrimaryKey(String appid);

    int insert(JsapiTicket record);

    int insertSelective(JsapiTicket record);

    JsapiTicket selectByPrimaryKey(String appid);

    int updateByPrimaryKeySelective(JsapiTicket record);

    int updateByPrimaryKey(JsapiTicket record);
}