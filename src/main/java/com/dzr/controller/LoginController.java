package com.dzr.controller;

import com.dzr.framework.base.BaseController;
import com.dzr.framework.config.Constant;
import com.dzr.framework.config.UrlConfig;
import com.dzr.service.BaseInfoService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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

    //调用去获取用户的信息
    @RequestMapping("/perfectInfo")
    public String register(Model model, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        JSONObject res = baseInfoService.getUserInfo(userId);
        if (0 == res.getInt("error_code")) {
            model.addAttribute("member", res.getJSONObject("member"));
        } else {
            return "error";
        }
        return "fillself";
    }

    /**
     * 进入首页
     * @return
     */
    @RequestMapping("/index")
    public String index() {
        return "index";
    }

    @RequestMapping("/activitys")
    public String getActivitys(Model model, HttpServletRequest request) {

        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        //获取活动列表
        String[] arr = new String[]{"per_page" + perPage, "page" + page};
        String mystr = "per_page=" + perPage + "&page=" + page;
        JSONObject bannersInfo = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.ACTIVITYS, mystr, arr));
        if (0 == bannersInfo.getInt("error_code")) {
            model.addAttribute("activitys", bannersInfo.getJSONArray("result"));
        }
        return "map";
    }

    @RequestMapping("/{activityDetail}")
    public String getActivityDetail(@PathVariable("activityDetail") String activityDetail, Model model) {

        //获取活动详情
        String[] arr = new String[]{"activity_id" + activityDetail};
        String mystr = "activity_id=" + activityDetail;
        JSONObject info = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.ACTIVITY_DETAIL, mystr, arr));
        if (0 == info.getInt("error_code")) {
            model.addAttribute("info", info.getJSONObject("result"));
        }
        return "map";
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
        return "myonly/map";
    }

    @RequestMapping("/products")
    public String getProducts(Model model, HttpServletRequest request) {
        String cateId = request.getParameter("cateId");
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        //获取产品列表
        String[] arr = new String[]{"cate_id" + cateId, "per_page" + perPage, "page" + page};
        String mystr = "cate_id=" + cateId + "&per_page=" + perPage + "&page=" + page;
        JSONObject info = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.PRODUCTS, mystr, arr));
        if (0 == info.getInt("error_code")) {
            model.addAttribute("products", info.getJSONArray("result"));
        }
        return "myonly/map";
    }

    @RequestMapping("/{productDetail}")
    public String getProductDetail(@PathVariable("productDetail") String productDetail, Model model) {

        //获取产品详情
        String[] arr = new String[]{"product_id" + productDetail};
        String mystr = "product_id=" + productDetail;
        JSONObject productInfo = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.PRODUCT_DETAIL, mystr, arr));
        if (0 == productInfo.getInt("error_code")) {
            model.addAttribute("info", productInfo.getJSONObject("result"));
        }
        return "myonly/map";
    }

}
