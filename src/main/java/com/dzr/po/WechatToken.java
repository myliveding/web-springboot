package com.dzr.po;

import lombok.Data;

@Data
public class WechatToken {

    private Integer id;

    private String appid;

    private String appsecret;

    private String name;

    private Integer type;

    private String token;

    private String remark;

    private Integer createTime;

    private Integer updateTime;

    private Boolean isDelete;

}