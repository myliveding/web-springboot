package com.dzr.framework.filter;

/**
 * @author dingzr
 * @Description
 * @ClassName LoginFilter
 * @since 2017/7/21 15:55
 */

//@Order(1)
//@WebFilter(filterName = "loginFilter", urlPatterns = {"/userHtml/*", "/userRest/*", "/userJsp/*"})
//public class LoginFilter extends BaseDataSource implements Filter {

//    private static final Logger logger = LoggerFactory.getLogger(LoginFilter.class);

//    @Autowired FilterUrl filterUrl;

    /**
     * 封装，不需要过滤的list列表
     */
//    protected static List<Pattern> patterns = new ArrayList<Pattern>();

//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//        logger.info("loginFilter filterConfig... " + filterUrl.getSimpleProp());
//        filterUrl.getListProps().forEach(t ->{
//            Pattern p = Pattern.compile(t);
//            patterns.add(p);
//        });
//        logger.info("loginFilter filterConfig... " + patterns.size());
//    }

//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
//            throws IOException, ServletException {
//
//        HttpServletRequest httpRequest = (HttpServletRequest) servletRequest;
//        HttpServletResponse httpResponse = (HttpServletResponse) servletResponse;
//
//        String url = httpRequest.getRequestURI().substring(httpRequest.getContextPath().length());
//        if (url.startsWith("/") && url.length() > 1) {
//            url = url.substring(1);
//        }
//        logger.info("loginFilter--getRequestURI：" + httpRequest.getRequestURI() + "--url:" + url);
//        if (isInclude(url)){
//            filterChain.doFilter(httpRequest, httpResponse);
//            return;
//        } else {
//            RequestDispatcher rd = httpRequest.getRequestDispatcher("/404.jsp");
//            rd.forward(httpRequest, httpResponse);
//            return;
//        }
//    }

//    @Override
//    public void destroy() {
//    }

    /**
     * 是否需要过滤
     * @param url
     * @return
     */
//    private boolean isInclude(String url) {
//        for (Pattern pattern : patterns) {
//            logger.info(pattern + "");
//            Matcher matcher = pattern.matcher(url);
//            //matcher.matches();//这是完全匹配
//            if (matcher.find()) {
//                logger.info(pattern + "匹配成功");
//                return true;
//            }
//        }
//        return false;
//    }

//}


//@Bean
//public FilterRegistrationBean myFilter() {
//        FilterRegistrationBean registration = new FilterRegistrationBean();
//        registration.setFilter(new MyFilter());
//        registration.setDispatcherTypes(EnumSet.allOf(DispatcherType.class));
//        return registration;
//        }