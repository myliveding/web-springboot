package com.dzr.framework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author dingzr
 * @Description
 * @ClassName TemplateConfig
 * @since 2017/9/14 10:07
 */

@Data
@Component
@ConfigurationProperties(prefix = "template")
public class TemplateConfig {

    private String buySuss;

}
