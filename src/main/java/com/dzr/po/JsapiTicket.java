package com.dzr.po;

import lombok.Data;

@Data
public class JsapiTicket {
    private String appid;

    private String ticket;

    private Integer createTime;

    private Integer updateTime;

    private Boolean isDelete;

}