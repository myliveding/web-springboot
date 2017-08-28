package com.dzr.controller;

import com.dzr.framework.base.BaseController;
import com.dzr.service.BaseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author dingzr
 * @Description 需要校验并返回json格式的数据接口
 * @ClassName LoginRestController
 * @since 2017/8/23 17:27
 */

@RestController
@RequestMapping("/rest")
public class LoginRestController extends BaseController {

    @Autowired
    BaseInfoService baseInfoService;

    /**
     * 页面加载更多数据
     *
     * @param request
     * @return
     */
    @RequestMapping("/activitysPaging")
    public Map<String, Object> getActivitysPaging(HttpServletRequest request) {
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        return successResult(baseInfoService.getActivitysPaging(page, perPage));
    }

}
