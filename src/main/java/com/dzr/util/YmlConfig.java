package com.dzr.util;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author dingzr
 * @Description
 * @ClassName YmlConfig
 * @since 2017/7/12 14:41
 */
@Data
@Component
@ConfigurationProperties(prefix="spring.datasource.primary")
public class YmlConfig {
    private String url;
    private String username;
    private String password;
}