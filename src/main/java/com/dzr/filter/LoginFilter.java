package com.dzr.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
@WebFilter(filterName = "loginFilter", urlPatterns = {"/userHtml/*", "/userRest/*", "/userJsp/*"})
public class LoginFilter implements Filter{

    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

    @Autowired FilterUrl filterUrl;

    /**
     * 封装，不需要过滤的list列表
     */
    protected static List<Pattern> patterns = new ArrayList<Pattern>();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        logger.info("loginFilter filterConfig... " + filterUrl.getSimpleProp());
        filterUrl.getListProps().forEach(t ->{
            Pattern p = Pattern.compile(t);
            patterns.add(p);
        });
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
        if (isInclude(url)){
            filterChain.doFilter(httpRequest, httpResponse);
            return;
        } else {
            RequestDispatcher rd = httpRequest.getRequestDispatcher("/404.jsp");
            rd.forward(httpRequest, httpResponse);
            return;
        }
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
            logger.info(pattern + "");
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
