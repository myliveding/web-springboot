package com.dzr.po.wx;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Weixin {

    private String jsapiTicket; //js调用的凭据

    @SerializedName("fullName")
    private String access_token;    //accesstoken

    private String expires_in;

    private String refresh_token;

    private String openid;
    private String scope;


}
