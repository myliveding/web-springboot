package com.dzr.framework.base;

import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

/**
 * Controller层的基类，第一个版本中主要提供分页方法
 *
 * @author zhangsheng
 *         <p/>
 *         date：2014-2-17
 */
@Slf4j
public class BaseController {

    protected Map<String, Object> successResult(Object object) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        map.put("msg", "ok");
        map.put("data", object);
        return map;
    }

    protected Map<String, Object> successResult(long total, Object object) {
        Map<String, Object> map = new HashMap<>();
        map.put("status", 0);
        map.put("msg", "ok");
        map.put("data", object);
        map.put("recordsTotal", total);
        map.put("recordsFiltered", total);

        return map;
    }
}