package com.dzr.controller;

import com.st.core.ContextHolderUtils;
import com.st.ktv.service.MemberService;
import com.st.ktv.service.impl.WechatAPIServiceImpl;
import com.st.utils.Constant;
import com.st.utils.DataUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;


@Controller
@RequestMapping("/scope")
public class ScopeController {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Resource
    private WechatAPIServiceImpl weixinAPIService;
    @Resource
    private MemberService memberService;

    @RequestMapping("/openid")
    public ModelAndView getUserOpenIdOfScope(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String next = request.getParameter("next");
        logger.info("ScopeController：" + code + ",cn：" + next);
        if (!"".equals(code) && code != null && !"".equals(next) && next != null) {
            String nextPage = next.substring(0, next.indexOf(".do")) + ".do";
            String appid = next.substring(next.indexOf(".do") + 3, next.length());
            String pram = "";
            if (appid.length() > 18) {
                pram = appid.substring(18, appid.length());
                appid = appid.substring(0, 18);
            }
            logger.info("pram：" + pram + ",appid：" + appid);
            String openid = weixinAPIService.getUserOpenIdOfScope(appid, code);
            logger.info("redirect:" + Constant.URL + "/" + nextPage + "?openid=" + openid + "&appid=" + appid + "#" + pram);
            HttpSession session = ContextHolderUtils.getSession();
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
            logger.info("把openid：" + openid + "，appid：" + appid + "加到sesion");
            boolean subscribe = weixinAPIService.IsSubscribe(appid, openid);
            session.setAttribute("subscribe", subscribe);
            //校验这个初次进入公众号的时候时候数据库中存在openid，不存在则加入
            String memberId = memberService.checkLogin(openid, appid);
            if (!"".equals(memberId)) {
                session.setAttribute("memberId", memberId);
                logger.info("memberId：" + memberId + "加到sesion");
            }

            if (DataUtil.isNotEmpty(pram)) {
                logger.info(nextPage + "?" + pram);
                return new ModelAndView("redirect:" + Constant.URL + "/" + nextPage + "?" + pram);

            } else {
                return new ModelAndView("redirect:" + Constant.URL + "/" + nextPage);
            }

        } else {
            logger.error("未将对象引入到实例");
            request.setAttribute("error", "未将对象引入到实例");
            return new ModelAndView("redirect:error.do");
        }
    }


    @RequestMapping("/openidjsp")
    public ModelAndView getUserMapOpenIdOfScope(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String code = request.getParameter("code");
        String next = request.getParameter("next");
        if (!"".equals(code) && code != null && !"".equals(next) && next != null) {
            String nextPage = next.substring(0, next.indexOf(".do")) + ".jsp";
            String appid = next.substring(next.indexOf(".do") + 3, next.length());
            String openid = weixinAPIService.getUserOpenIdOfScope(appid, code);
            logger.info("redirect:" + Constant.URL + "/" + nextPage + "?openid=" + openid + "&appid=" + appid);
            HttpSession session = ContextHolderUtils.getSession();
            session.setAttribute("openid", openid);
            session.setAttribute("appid", appid);
            boolean subscribe = weixinAPIService.IsSubscribe(appid, openid);
            session.setAttribute("Subscribe", subscribe);

            return new ModelAndView("redirect:" + Constant.URL + "/" + nextPage);
        } else {
            logger.error("未将对象引入到实例");
            request.setAttribute("error", "未将对象引入到实例");
            return new ModelAndView("redirect:error.do");
        }
    }

}
