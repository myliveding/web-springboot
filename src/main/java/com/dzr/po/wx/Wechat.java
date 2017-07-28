package com.dzr.po.wx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Wechat {

    private String ticket; //js调用的临时凭据
    private String access_token; //accesstoken
    private String expires_in;
    private String refresh_token;
    private String openid;
    private String scope;
    private Integer errcode; //错误code
    private String errmsg; //错误信息


}
