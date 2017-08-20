package com.dzr.controller;

import com.dzr.framework.config.Constant;
import com.dzr.framework.config.InvokingUrl;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description 产品控制器
 * @FileName ProductController
 * @Author dingzr
 * @CreateTime 2017/8/20 16:17 八月
 */

@Controller
@RequestMapping("/product")
public class ProductController {

    @Autowired
    InvokingUrl invokingUrl;

    @RequestMapping("/productCates")
    public String getproductCates(Model model) {

        //获取产品分类
        String[] arr = new String[]{};
        String mystr = "";
        JSONObject result = JSONObject.fromObject(Constant.getInterface(invokingUrl.getPhp() + Constant.PRODUCT_CATES, mystr, arr));
        if (0 == result.getInt("error_code")) {
            model.addAttribute("cates", result.getJSONArray("result"));
        }
        return "map";
    }

    @RequestMapping("/products")
    public String getProducts(Model model, HttpServletRequest request) {
        String cateId = request.getParameter("cateId");
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        //获取产品列表
        String[] arr = new String[]{"cate_id" + cateId, "per_page" + perPage, "page" + page};
        String mystr = "cate_id=" + cateId + "&per_page=" + perPage + "&page=" + page;
        JSONObject info = JSONObject.fromObject(Constant.getInterface(invokingUrl.getPhp() + Constant.PRODUCTS, mystr, arr));
        if (0 == info.getInt("error_code")) {
            model.addAttribute("products", info.getJSONArray("result"));
        }
        return "map";
    }

    @RequestMapping("/{productDetail}")
    public String getProductDetail(@PathVariable("productDetail") String productDetail, Model model) {

        //获取产品详情
        String[] arr = new String[]{"product_id" + productDetail};
        String mystr = "product_id=" + productDetail;
        JSONObject productInfo = JSONObject.fromObject(Constant.getInterface(invokingUrl.getPhp() + Constant.PRODUCT_DETAIL, mystr, arr));
        if (0 == productInfo.getInt("error_code")) {
            model.addAttribute("info", productInfo.getJSONObject("result"));
        }
        return "map";
    }

}
