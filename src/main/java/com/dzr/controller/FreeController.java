package com.dzr.controller;

import com.dzr.framework.base.BaseController;
import com.dzr.framework.config.Constant;
import com.dzr.framework.config.UrlConfig;
import com.dzr.framework.exception.ApiException;
import com.dzr.service.BaseInfoService;
import com.dzr.service.WechatService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author dingzr
 * @Description 不需要校验是否登录的接口
 * @ClassName FreeController
 * @since 2017/8/23 17:27
 */

@RestController
@RequestMapping("/free")
public class FreeController extends BaseController {

    @Autowired
    UrlConfig urlConfig;
    @Autowired
    BaseInfoService baseInfoService;
    @Autowired
    WechatService wechatService;

    @RequestMapping("/sendSms")
    public Map<String, Object> sendSms(String mobile, HttpServletRequest request) {
        baseInfoService.sendSms(mobile);
        return successResult("sendSms");
    }


    @RequestMapping("/register")
    public Map<String, Object> register(String name, String mobile, String password, String code, HttpServletRequest request) {
        baseInfoService.register(name, mobile, password, code, request);
        return successResult("register");
    }


    @RequestMapping("/login")
    public Map<String, Object> login(String mobile, String password, String code, HttpServletRequest request) {
        baseInfoService.login(mobile, password, code, request);
        return successResult("login");
    }

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
            throw new ApiException(10008, info.getString("error_msg"));
        }
        return successResult(info.getJSONArray("result"));
    }

    /**
     * 发送微信模版消息
     *
     * @param request
     * @return object
     */
    @RequestMapping("/sendTemplateMessage")
    public Object sendTemplateMessage(HttpServletRequest request) {
        String type = request.getParameter("type");  //0 业务服务提醒 ；  1 认证通知；2 消息提醒 ；3 获得代金券通知；4注册通知；
        //5 参保成功通知；6参保失败通知；7停保成功通知；8停保失败通知；9退款成功通知；10服务到期提醒；11业务办理取消通知；12订单未支付通知;13订单支付成功;14业务动态提醒;15手机号绑定
        String firstStr = request.getParameter("first") == null ? "" : request.getParameter("first");
        String keyword1Str = request.getParameter("keyword1") == null ? "" : request.getParameter("keyword1");
        String keyword2Str = request.getParameter("keyword2") == null ? "" : request.getParameter("keyword2");
        String keyword3Str = request.getParameter("keyword3") == null ? "" : request.getParameter("keyword3");
        String keyword4Str = request.getParameter("keyword4") == null ? "" : request.getParameter("keyword4");
        String keyword5Str = request.getParameter("keyword5") == null ? "" : request.getParameter("keyword5");
        String remarkStr = request.getParameter("remark") == null ? "" : request.getParameter("remark");
        String url = request.getParameter("url") == null ? "" : request.getParameter("url");
        String openId = request.getParameter("openId") == null ? "" : request.getParameter("openId");
        //      openId="on7Tqvg7aZC7syuOTTYG60aPD3Ig";
        JSONObject resultStr = new JSONObject();
        if (null != type) {
            resultStr = wechatService.sendTemplateMessageByType(type, firstStr, keyword1Str, keyword2Str, keyword3Str, keyword4Str, keyword5Str, openId, remarkStr, url);
        }
        return resultStr;
    }

}
