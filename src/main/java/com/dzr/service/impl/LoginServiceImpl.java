package com.dzr.service.impl;

import com.dzr.framework.config.Constant;
import com.dzr.framework.config.UrlConfig;
import com.dzr.framework.config.WechatParams;
import com.dzr.framework.exception.ApiException;
import com.dzr.service.BaseInfoService;
import com.dzr.service.LoginService;
import com.dzr.service.WechatService;
import com.dzr.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

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
            throw new ApiException(10008, res.getString("error_msg"));
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
        String mystr = "member_id=" + userId;

        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.SCAN_PAY_INFO, mystr, null));
        if (res.getInt("error_code") == 0) {
            JSONObject data = JSONObject.fromObject(res.getString("result"));
            model.addAttribute("info", data);

            HttpSession session = request.getSession();
            if (data.containsKey("coupons")) {
                session.setAttribute("coupons", data.getJSONArray("coupons"));
            }
            if (data.containsKey("discount_cards")) {
                session.setAttribute("cards", data.getJSONArray("discount_cards"));
            }
//            //去调用获取最优方案
//            res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.PAY_CHOICE, mystr, null));
//            if(res.getInt("error_code") == 0){
//                //0优惠券 1打折卡 2积分
//                model.addAttribute("type", res.get("type"));
//                data = JSONObject.fromObject(res.getString("result"));
//                //19代表优惠券或是打折卡的id
//                //98代表优惠券或是打折卡的优惠金额
//                model.addAttribute("type", res.get("type"));
//                model.addAttribute("type", res.get("type"));
//            }
        } else {
            throw new ApiException(10008, res.getString("error_msg"));
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
        model.addAttribute("shareUrl", wechatParams.getDomain()
                + "/login/gotoSharePage?name=receiveCardPage&telphone=" + telphone + "&cardId=ID");
//        model.addAttribute("shareUrl", wechatParams.getDomain()
//                + "/login/receiveCardPage?telphone=" + telphone + "&cardId=ID");
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

    /**
     * 扫码支付的最优选择
     *
     * @param money
     * @param request
     */
    public JSONObject payChoice(String money, HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        if (StringUtils.isEmpty(userId)) {
            throw new ApiException(10008, "请先选择登陆");
        }
        if (StringUtils.isEmpty(money)) {
            throw new ApiException(10008, "输入金额不能为空");
        }
        String mystr = "member_id=" + userId + "&money=" + money;
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.PAY_CHOICE, mystr, null));
        if (res.getInt("error_code") == 0) {
            return res;
        } else {
            throw new ApiException(10008, res.getString("error_msg"));
        }
    }

}
