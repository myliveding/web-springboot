package com.dzr.controller;

import com.dzr.framework.base.BaseController;
import com.dzr.framework.config.Constant;
import com.dzr.framework.config.UrlConfig;
import com.dzr.framework.exception.ApiException;
import com.dzr.service.BaseInfoService;
import com.dzr.service.LoginService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
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
    UrlConfig urlConfig;
    @Autowired
    BaseInfoService baseInfoService;
    @Autowired
    LoginService loginService;

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

    @RequestMapping("/products")
    public Map<String, Object> getProducts(HttpServletRequest request) {
        String cateId = request.getParameter("cateId");
        String perPage = request.getParameter("prePage");
        String page = request.getParameter("page");
        //获取产品列表
        String[] arr = new String[]{"cate_id" + cateId, "per_page" + perPage, "page" + page};
        String mystr = "cate_id=" + cateId + "&per_page=" + perPage + "&page=" + page;
        JSONObject info = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.PRODUCTS, mystr, arr));
        if (info.getInt("error_code") == 0) {
        } else {
            throw new ApiException(info.getInt("error_code"), info.getString("error_msg"));
        }
        return successResult(info.getJSONArray("result"));
    }

    /**
     * 团队
     *
     * @param request
     * @return
     */
    @RequestMapping("/teamPaging")
    public Map<String, Object> getTeamPaging(HttpServletRequest request) {
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        return successResult(baseInfoService.gotoTeam(perPage, page, request));
    }

    /**
     * 打折
     *
     * @param request
     * @return
     */
    @RequestMapping("/discountPaging")
    public Map<String, Object> getDiscountPaging(HttpServletRequest request) {
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        String status = request.getParameter("status");
        return successResult(baseInfoService.gotoDiscountCard(perPage, page, status, request));
    }

    @RequestMapping("/receiveCard")
    public void receiveCard(Model model, HttpServletRequest request) {
        loginService.receiveCard(model, request);
    }

    /**
     * 优惠
     *
     * @param request
     * @return
     */
    @RequestMapping("/couponsPaging")
    public Map<String, Object> getCouponsPaging(HttpServletRequest request) {
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        String status = request.getParameter("status");
        return successResult(baseInfoService.gotoCouponsCard(perPage, page, status, request));
    }

}
