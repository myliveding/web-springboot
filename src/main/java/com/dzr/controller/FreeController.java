package com.dzr.controller;

import com.dzr.framework.base.BaseController;
import com.dzr.framework.config.Constant;
import com.dzr.framework.config.UrlConfig;
import com.dzr.framework.config.WechatParams;
import com.dzr.framework.exception.ApiException;
import com.dzr.service.BaseInfoService;
import com.dzr.service.WechatService;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
    WechatParams wechatParams;
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

    @RequestMapping("/openidLogin")
    public Map<String, Object> openidLogin(String openId, HttpServletRequest request) {
        baseInfoService.openidLogin(openId, request);
        return successResult("openidLogin");
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
     * @return object
     */
    @RequestMapping(value = "sendTemplateMessage", method = RequestMethod.POST)
    public Object sendTemplateMessage(@RequestParam(value = "type") String type,
                                      @RequestParam(value = "openId") String openId,
                                      @RequestParam(value = "first") String first,
                                      @RequestParam(value = "keyword1") String keyword1,
                                      @RequestParam(value = "keyword2") String keyword2,
                                      @RequestParam(value = "keyword3", required = false) String keyword3,
                                      @RequestParam(value = "keyword4", required = false) String keyword4,
                                      @RequestParam(value = "keyword5", required = false) String keyword5,
                                      @RequestParam(value = "remark", required = false) String remark,
                                      @RequestParam(value = "url", required = false) String url) {


        return wechatService.sendTemplateMessageByType(type, first, keyword1, keyword2, keyword3, keyword4, keyword5, openId, remark, url);
    }

    @RequestMapping("/gotoSharePage")
    public String gotoSharePage(Model model, HttpServletRequest request) {
        String name = request.getParameter("name");
        //String reg = "(^|&)" + name + "=([^&]*)(&|$)";
        if (name.equals("productCates")) {

            model.addAttribute("shareUrl", "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                    + wechatParams.getAppId() + "&redirect_uri=" + wechatParams.getDomain()
                    + "/scope/openid.do?next=login/productCates.do" + wechatParams.getAppId()
                    + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");

        } else if (name.equals("receiveCardPage")) {
//            /free/gotoSharePage?name=receiveCardPage&telphone=" + telphone + "&cardId=ID
            String telphone = request.getParameter("telphone");
            String cardId = request.getParameter("cardId");
            model.addAttribute("shareUrl", "https://open.weixin.qq.com/connect/oauth2/authorize?appid="
                    + wechatParams.getAppId() + "&redirect_uri=" + wechatParams.getDomain()
                    + "/scope/openid.do?next=/login/receiveCardPage.do" + wechatParams.getAppId()
                    + "telphone=" + telphone + "&cardId=" + cardId
                    + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect");
        }
        return "share";
    }

}
