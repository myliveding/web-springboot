package com.dzr.filter;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author dingzr
 * @Description
 * @ClassName FilterUrl
 * @since 2017/7/25 11:10
 */

@Data
@Component
@ConfigurationProperties(prefix = "filter")
public class FilterUrl {

    private String simpleProp;
    private String[] arrayProps;
    private List<String> listProps;
    private Map<String,String> mapProps;
    private List<Map<String,String>> mapListProps;

}
