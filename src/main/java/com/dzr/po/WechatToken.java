package com.dzr.po;

import lombok.Data;

@Data
public class WechatToken {

    private String appid;

    private String appsecret;

    private String wxname;

    private String accessToken;

    private String remark;

    private Integer createTime;

    private Integer updateTime;

    private Boolean isDelete;

}