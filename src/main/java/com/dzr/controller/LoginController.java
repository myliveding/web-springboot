package com.dzr.controller;

import com.dzr.framework.base.BaseController;
import com.dzr.framework.config.Constant;
import com.dzr.framework.config.UrlConfig;
import com.dzr.framework.exception.ApiException;
import com.dzr.service.BaseInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * @Description 需要校验登录信息的接口
 * @FileName LoginController
 * @Author dingzr
 * @CreateTime 2017/8/20 16:16 八月
 */

//@RestController
@Controller
@RequestMapping("/login")
public class LoginController extends BaseController {

    @Autowired
    UrlConfig urlConfig;
    @Autowired
    BaseInfoService baseInfoService;

    //进入完善资料页面
    @RequestMapping("/perfectInfo")
    public String perfectInfo() {
        return "fillself";
    }

    //进入完善资料页面
    @RequestMapping("/savePerfectInfo")
    public Map<String, Object> savePerfectInfo(String name, String sex, String birth, String wechat, String adress, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        baseInfoService.savePerfectInfo(userId, name, sex, birth, wechat, adress);
        return successResult("savePerfectInfo");
    }

    /**
     * 进入首页
     * @return
     */
    @RequestMapping("/index")
    public String index(Model model, HttpServletRequest request) {
        JSONArray banners = baseInfoService.getBanners();
        model.addAttribute("banners", banners);
        String userId = (String) request.getSession().getAttribute("userId");
        model.addAttribute("user", baseInfoService.getUserInfo(userId));
        return "index";
    }

    /**
     * 进去用户中心
     *
     * @return
     */
    @RequestMapping("/userCenter")
    public String userCenter(Model model, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        model.addAttribute("user", baseInfoService.getUserInfo(userId));
        return "mine";
    }

    /**
     * 进入个人资料页面
     *
     * @return
     */
    @RequestMapping("/gotoUserInfo")
    public String gotoUserInfo(Model model, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        model.addAttribute("user", baseInfoService.getUserInfo(userId));
        return "self";
    }

    /**
     * 进入密码修改页面
     *
     * @return
     */
    @RequestMapping("/gotoPwd")
    public String gotoPwd() {
        return "pwd";
    }

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
     * 进入会员充值页面
     *
     * @return
     */
    @RequestMapping("/gotoVip")
    public String gotoVip(Model model, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        String[] arr = new String[]{"member_id" + userId};
        String mystr = "member_id=" + userId;
        JSONObject user = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.USER_INFO, mystr, arr));
        if (user.getInt("error_code") == 0) {
            model.addAttribute("member", JSONObject.fromObject(user.getString("member")));
        }

        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.MEMBER_RECHARGES, mystr, arr));
        if (res.getInt("error_code") == 0) {
            model.addAttribute("list", JSONArray.fromObject(res.getString("result")));
        } else {
            throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
        }

        return "vip";
    }

    /**
     * 进入消费记录页面
     *
     * @return
     */
    @RequestMapping("/gotoConsumptionRecords")
    public String gotoConsumptionRecords(Model model, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        String[] arr = new String[]{"member_id" + userId};
        String mystr = "member_id=" + userId;
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.AMOUNT_RECORDS, mystr, arr));
        if (res.getInt("error_code") == 0) {
            model.addAttribute("balance", res.getDouble("balance"));
            model.addAttribute("list", JSONArray.fromObject(res.getString("result")));
        } else {
            throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
        }
        return "consumption";
    }

    /**
     * 进入积分兑换记录页面
     *
     * @return
     */
    @RequestMapping("/gotoRecharge")
    public String gotoRecharge(Model model, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        String[] arr = new String[]{"member_id" + userId};
        String mystr = "member_id=" + userId;
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.INTEGRAL_RECORDS, mystr, arr));
        if (res.getInt("error_code") == 0) {
            model.addAttribute("integral", res.getDouble("integral"));
            model.addAttribute("list", JSONArray.fromObject(res.getString("result")));
        } else {
            throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
        }
        return "recharge";
    }

    /**
     * 进入团队页面
     *
     * @return
     */
    @RequestMapping("/gotoTeam")
    public String gotoTeam(Model model, HttpServletRequest request) {
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        model.addAttribute("teams", baseInfoService.gotoTeam(perPage, page, request));
        return "team";
    }

    /**
     * 进入产品推广页面
     *
     * @return
     */
    @RequestMapping("/productCates")
    public String getproductCates(Model model) {
        //获取产品分类
        String[] arr = new String[]{};
        String mystr = "";
        JSONObject result = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.PRODUCT_CATES, mystr, arr));
        if (0 == result.getInt("error_code")) {
            model.addAttribute("cates", result.getJSONArray("result"));
        }
        return "brand";
    }

    @RequestMapping("/productDetail")
    public String getProductDetail(Model model, HttpServletRequest request) {
        String productDetail = request.getParameter("id");
        //获取产品详情
        String[] arr = new String[]{"product_id" + productDetail};
        String mystr = "product_id=" + productDetail;
        JSONObject productInfo = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.PRODUCT_DETAIL, mystr, arr));
        if (0 == productInfo.getInt("error_code")) {
            model.addAttribute("info", productInfo.getJSONObject("result"));
            model.addAttribute("url", "login/productCates");
        }
        return "bshow";
    }

    /**
     * 进入我的优惠券
     *
     * @return
     */
    @RequestMapping("/gotoCouponsCard")
    public String gotoCouponsCard(Model model, HttpServletRequest request) {
        String status = request.getParameter("status");
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        model.addAttribute("cards", baseInfoService.gotoCouponsCard(perPage, page, status, request));
        return "card";
    }

    /**
     * 进入打折卡券发放页面
     *
     * @return
     */
    @RequestMapping("/gotoDiscountCard")
    public String gotoDiscountCard(Model model, HttpServletRequest request) {
        String status = request.getParameter("status");
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        model.addAttribute("cards", baseInfoService.gotoDiscountCard(perPage, page, status, request));
        return "sendcard";
    }

    /**
     * 进入活动页面
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/activitys")
    public String getActivitys(Model model, HttpServletRequest request) {

        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        model.addAttribute("activitys", baseInfoService.getActivitysPaging(page, perPage));
        return "active";
    }

    /**
     * 页面加载更多数据
     * 这个有两种实现方式还有一种在rest里面
     *
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("/activitysPaging")
    public
    @ResponseBody
    Object getActivitysPaging(Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        response.setContentType("text/html; charset=utf-8");
        PrintWriter out = response.getWriter();
        out.println(baseInfoService.getActivitysPaging(page, perPage));
        return null;
    }

    @RequestMapping("/activityDetail")
    public String getActivityDetail(Model model, HttpServletRequest request) {
        String activityDetail = request.getParameter("id");
        //获取活动详情
        model.addAttribute("info", baseInfoService.getActivityDetail(activityDetail));
        model.addAttribute("url", "login/activitys");
        return "bshow";
    }

    @RequestMapping("/gotoCode")
    public String gotoCode(Model model, HttpServletRequest request) {
        String codeId = request.getParameter("id");
        return "code";
    }

}
