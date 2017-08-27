package com.dzr.controller;

import com.dzr.framework.base.BaseController;
import com.dzr.framework.config.Constant;
import com.dzr.framework.config.UrlConfig;
import com.dzr.framework.exception.ApiException;
import com.dzr.service.BaseInfoService;
import com.dzr.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Description 需要校验登录信息的接口
 * @FileName LoginController
 * @Author dingzr
 * @CreateTime 2017/8/20 16:16 八月
 */

@RestController
//@Controller
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
        return "mime";
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
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.MEMBER_RECHARGES, mystr, arr));
        if (res.getInt("error_code") == 0) {
            model.addAttribute("balance", res.getDouble("balance"));
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
    @RequestMapping("/gotoBrand")
    public String gotoBrand() {
        return "brand";
    }

    /**
     * 进入我的优惠券
     *
     * @return
     */
    @RequestMapping("/gotoCard")
    public String gotoCard(Model model, HttpServletRequest request) {
        String status = request.getParameter("status");
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        model.addAttribute("cards", baseInfoService.gotoCard(perPage, page, status, request));
        return "card";
    }

    /**
     * 进入打折卡券发放页面
     *
     * @return
     */
    @RequestMapping("/gotoSendCard")
    public String gotoSendCard(Model model, HttpServletRequest request) {
        String status = request.getParameter("status");
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        model.addAttribute("cards", baseInfoService.gotoSendCard(perPage, page, status, request));
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
        //获取活动列表
        String[] arr;
        String mystr = "";
        StringBuffer buffer = new StringBuffer();
        List<String> list = new ArrayList<String>();
        if (StringUtils.isNotEmpty(perPage)) {
            list.add("per_page" + perPage);
            buffer.append("&per_page=").append(perPage);
        }
        if (StringUtils.isNotEmpty(page)) {
            list.add("page" + page);
            buffer.append("&page=").append(page);
        }
        mystr = buffer.toString();
        arr = list.toArray(new String[list.size()]);
        JSONObject bannersInfo = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.ACTIVITYS, mystr, arr));
        if (0 == bannersInfo.getInt("error_code")) {
            model.addAttribute("activitys", bannersInfo.getJSONArray("result"));
        }
        return "active";
    }

    @RequestMapping("/activityDetail")
    public String getActivityDetail(Model model, HttpServletRequest request) {
        String activityDetail = request.getParameter("id");
        //获取活动详情
        String[] arr = new String[]{"activity_id" + activityDetail};
        String mystr = "activity_id=" + activityDetail;
        JSONObject info = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.ACTIVITY_DETAIL, mystr, arr));
        if (0 == info.getInt("error_code")) {
            model.addAttribute("info", info.getJSONObject("result"));
            model.addAttribute("url", "${pageContext.request.contextPath}/login/activitys");
        }
        return "bshow";
    }

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

    @RequestMapping("/products")
    public Map<String, Object> getProducts(HttpServletRequest request) {
        String cateId = request.getParameter("cateId");
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        //获取产品列表
        String[] arr = new String[]{"cate_id" + cateId, "per_page" + perPage, "page" + page};
        String mystr = "cate_id=" + cateId + "&per_page=" + perPage + "&page=" + page;
        JSONObject info = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.PRODUCTS, mystr, arr));
        if (0 == info.getInt("error_code")) {
        } else {
            throw new ApiException(info.getInt("error_code"), info.getString("error_msg"));
        }
        return successResult(info.getJSONArray("result"));
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
            model.addAttribute("url", "${pageContext.request.contextPath}/login/productCates");
        }
        return "bshow";
    }

}
