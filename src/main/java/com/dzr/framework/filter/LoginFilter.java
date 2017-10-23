package com.dzr.framework.filter;


import com.dzr.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author dingzr
 * @Description
 * @ClassName LoginFilter
 * @since 2017/7/21 15:55
 */

@Order(1)
@WebFilter(filterName = "loginFilter", urlPatterns = {"/login/*", "/rest/*"})
public class LoginFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    private static final String excludedUrls = "login/systemInfo";

    /**
     * 封装，不需要过滤的list列表
     */
    private List<Pattern> patterns = new ArrayList<>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        String[] urls = excludedUrls.toString().split(",");
        for (int i = 0; i < urls.length; i++) {
            patterns.add(Pattern.compile(urls[i]));
        }
        logger.info("loginFilter filterConfig... " + patterns.size());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;

        String url = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
        if (url.startsWith("/") && url.length() > 1) {
            url = url.substring(1);
        }
        logger.info("loginFilter--getRequestURI：" + httpRequest.getRequestURI() + "--url:" + url);
        if (!isInclude(url)) {
            String userId = (String) httpRequest.getSession().getAttribute("userId");
            if (null == userId || "".equals(userId)) {
                logger.info("参数：" + httpRequest.getQueryString());
                if (null != httpRequest.getQueryString() && !"".equals(httpRequest.getQueryString())
                        && httpRequest.getQueryString().contains("telphone")) {
                    httpRequest.getSession().setAttribute("backUrl", httpRequest.getRequestURI() + "?" + httpRequest.getQueryString());
                }
                RequestDispatcher rd = httpRequest.getRequestDispatcher("/");
                rd.forward(httpRequest, httpResponse);
                return;
            }
        }
        filterChain.doFilter(httpRequest, httpResponse);
    }

    @Override
    public void destroy() {
    }

    /**
     * 是否需要过滤
     * @param url
     * @return
     */
    private boolean isInclude(String url) {
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(url);
            //matcher.matches();//这是完全匹配
            if (matcher.find()) {
                logger.info(pattern + "匹配成功");
                return true;
            }
        }
        return false;
    }

}