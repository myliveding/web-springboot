package com.dzr.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Description 接口的调用地址
 * @FileName InvokingUrl
 * @Author dingzr
 * @CreateTime 2017/8/20 15:35 八月
 */

@Data
@Component
@ConfigurationProperties(prefix = "url")
public class UrlConfig {

    private String php;
    private String domain;

}
