package com.dzr.controller;

import com.dzr.framework.config.WechatParams;
import com.dzr.framework.config.WechatUtil;
import com.dzr.service.BaseInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
@RequestMapping("/scope")
public class ScopeController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    WechatParams wechatParams;
    @Autowired
    BaseInfoService baseInfoService;

    @RequestMapping("/openid")
    public ModelAndView getUserOpenIdOfScope(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String next = request.getParameter("next");
        logger.info("ScopeController：" + code + ",cn：" + next);
        if (!"".equals(code) && code != null && !"".equals(next) && next != null) {
            String nextPage = next.substring(0, next.indexOf(".do"));
            String appid = next.substring(next.indexOf(".do") + 3, next.length());
            String pram = "";
            if (appid.length() > 18) {
                pram = appid.substring(18, appid.length());
                appid = appid.substring(0, 18);
            }
            logger.info("pram：" + pram + ",appid：" + appid);
            String openid = WechatUtil.webAuthorization(appid, wechatParams.getAppSecret(), code);
            logger.info("redirect:" + wechatParams.getDomain() + "/" + nextPage + "?openid=" + openid + "&appid=" + appid + "#" + pram);
            HttpSession session = request.getSession();
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
            logger.info("把openid：" + openid + "，appid：" + appid + "加到sesion");

            //先去使用openid进行登录
            baseInfoService.openidLogin(openid, request);

            if (!"".equals(pram)) {
                logger.info(nextPage + "?" + pram);
                return new ModelAndView("redirect:" + wechatParams.getDomain() + "/" + nextPage + "?" + pram);
            } else {
                return new ModelAndView("redirect:" + wechatParams.getDomain() + "/" + nextPage);
            }

        } else {
            logger.error("未将对象引入到实例");
            request.setAttribute("error", "未将对象引入到实例");
            return new ModelAndView("redirect:error.do");
        }
    }


    @RequestMapping("/share")
    public ModelAndView getUserOpenIdOfScopeForShare(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String next = request.getParameter("next");
        String telphone = "";
        String cardId = "";
        //login/receiveCardPage .do wxfb086a201e1de746telphone=18858276308cardId=34
        logger.info("ScopeController：" + code + ",cn：" + next);
        if (!"".equals(code) && code != null && !"".equals(next) && next != null) {
            String nextPage = next.substring(0, next.indexOf(".do")) + ".do";
            String appid = next.substring(next.indexOf(".do") + 3, next.indexOf(".do") + 21);
            logger.info("next:--" + next + "appid:--" + appid);
            telphone = next.substring(next.indexOf("telphone=") + 9, next.indexOf("telphone=") + 20);
            cardId = next.substring(next.indexOf("cardId=") + 7, next.length());

            String openid = WechatUtil.webAuthorization(appid, wechatParams.getAppSecret(), code);
            String param = "telphone=" + telphone + "&cardId=" + cardId;
            logger.info("redirect:" + wechatParams.getDomain() + "/" + nextPage + "?openid=" + openid + "&appid=" + appid + "#" + param);
            HttpSession session = request.getSession();
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
            logger.info("把openid：" + openid + "，appid：" + appid + "加到sesion");

            //先去使用openid进行登录
            baseInfoService.openidLogin(openid, request);

            if (!"".equals(param)) {
                logger.info(nextPage + "?" + param);
                return new ModelAndView("redirect:" + wechatParams.getDomain() + "/" + nextPage + "?" + param);
            } else {
                return new ModelAndView("redirect:" + wechatParams.getDomain() + "/" + nextPage);
            }
        } else {
            logger.error("openidshare.do未将对象引入到实例");
            request.setAttribute("error", "未将对象引入到实例");
            return new ModelAndView("redirect:error.do");
        }
    }

}
