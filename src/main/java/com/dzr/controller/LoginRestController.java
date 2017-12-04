package com.dzr.controller;

import com.dzr.framework.base.BaseController;
import com.dzr.framework.config.UrlConfig;
import com.dzr.service.BaseInfoService;
import com.dzr.service.LoginService;
import com.dzr.service.WechatService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    @Autowired
    WechatService wechatService;

    /**
     * 重置密码
     *
     * @param first
     * @param secondary
     * @param request
     * @return
     */
    @RequestMapping("/resetPassword")
    public Map<String, Object> resetPassword(String first, String secondary, HttpServletRequest request) {
        baseInfoService.resetPassword(first, secondary, request);
        return successResult("resetPassword");
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

    @RequestMapping("/receiveCard")
    public Map<String, Object> receiveCard(Model model, HttpServletRequest request) {
        loginService.receiveCard(model, request);
        return successResult("receiveCard");
    }


    @RequestMapping("/balancePay")
    public Map<String, Object> balancePay(@RequestParam(value = "money") String money,
                                          @RequestParam(value = "balance", required = false) String balance,
                                          @RequestParam(value = "integral", required = false) String integral,
                                          @RequestParam(value = "discountCardId", required = false) String discountCardId,
                                          @RequestParam(value = "couponId", required = false) String couponId,
                                          HttpServletRequest request) {

        wechatService.balancePay(money, balance, integral, discountCardId, couponId, request);
        return successResult("balancePay");
    }

    //进入完善资料页面
    @RequestMapping("/savePerfectInfo")
    public Map<String, Object> savePerfectInfo(String name, String sex, String birth, String wechat, String address, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        baseInfoService.savePerfectInfo(userId, name, sex, birth, wechat, address);
        return successResult("savePerfectInfo");
    }


    @RequestMapping("/payChoice")
    public JSONObject payChoice(@RequestParam(value = "money") String money,
                                HttpServletRequest request) {

        return loginService.payChoice(money, request);
    }

}
