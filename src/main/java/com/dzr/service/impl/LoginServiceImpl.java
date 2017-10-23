package com.dzr.service.impl;

import com.dzr.framework.config.Constant;
import com.dzr.framework.config.UrlConfig;
import com.dzr.framework.config.WechatParams;
import com.dzr.framework.exception.ApiException;
import com.dzr.service.BaseInfoService;
import com.dzr.service.LoginService;
import com.dzr.service.WechatService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @FileName BaseInfoServiceImpl
 * @Author dingzr
 * @CreateTime 2017/8/21 22:22 八月
 */

@Service("loginService")
public class LoginServiceImpl implements LoginService {

    private Logger logger = Logger.getLogger(LoginServiceImpl.class);

    @Autowired
    UrlConfig urlConfig;
    @Autowired
    WechatParams wechatParams;

    @Autowired
    WechatService wechatService;
    @Autowired
    BaseInfoService baseInfoService;

    /**
     * 跳转到充值页面
     *
     * @param model
     * @param request
     * @return
     */
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
     * 扫码接口跳转到扫码支付的页面
     *
     * @param model
     * @param request
     * @return
     */
    public String gotoCode(Model model, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        String[] arr = new String[]{"member_id" + userId};
        String mystr = "member_id=" + userId;
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.SCAN_PAY_INFO, mystr, arr));
        if (res.getInt("error_code") == 0) {
            model.addAttribute("list", JSONArray.fromObject(res.getString("result")));
        } else {
            throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
        }
        return "code";
    }

    public String gotoDiscountCard(Model model, HttpServletRequest request) {
        String status = request.getParameter("status");
        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        model.addAttribute("cards", baseInfoService.gotoDiscountCard(perPage, page, status, request));

        //获取当前用户得手机号码
        String telphone = "";
        String userId = (String) request.getSession().getAttribute("userId");
        String[] arr = new String[]{"member_id" + userId};
        String mystr = "member_id=" + userId;
        JSONObject user = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.USER_INFO, mystr, arr));
        if (user.getInt("error_code") == 0) {
            JSONObject data = JSONObject.fromObject(user.getString("member"));
            telphone = data.getString("mobile");
        }

        wechatService.getWechatShare(model, request);
        //使用openid自动登陆得时候需要去使用中间分享页面
        //model.addAttribute("shareUrl", wechatParams.getDomain() + "/login/gotoSharePage?name=");
        model.addAttribute("shareUrl", wechatParams.getDomain()
                + "/login/receiveCardPage?telphone=" + telphone + "&cardId=ID");
        model.addAttribute("domain", wechatParams.getDomain());

        return "sendcard";
    }

    public void receiveCard(Model model, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        String cardId = request.getParameter("cardId");
        String[] arr = new String[]{"member_id" + userId, "discount_card_id" + cardId};
        String mystr = "member_id=" + userId + "&discount_card_id=" + cardId;
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.RECEIVE_DISCOUNT_CARD, mystr, arr));
        if (res.getInt("error_code") == 0) {
        } else {
            throw new ApiException(10008, res.getString("error_msg"));
        }
    }

}
