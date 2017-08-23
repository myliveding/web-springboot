package com.dzr.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Description
 * @FileName Constant
 * @Author dingzr
 * @CreateTime 2017/8/20 15:38 八月
 */

public final class Constant {

    private static Logger logger = LoggerFactory.getLogger(Constant.class);

    //获取轮播图
    public static final String BANNERS = "/api/get_banners";
    //获取产品分类
    public static final String PRODUCT_CATES = "/api/get_product_cates";
    //获取产品列表
    public static final String PRODUCTS = "/api/get_products";
    //获取产品详情
    public static final String PRODUCT_DETAIL = "/api/get_product";
    //获取活动列表
    public static final String ACTIVITYS = "/api/get_activitys";
    //获取活动详情
    public static final String ACTIVITY_DETAIL = "/api/get_activity";
    //短信验证码
    public static final String SMS_CODE = "/api/get_sms_code";
    //注册接口
    public static final String REGISTER = "/api/register";
    //注册第二步骤以及更改资料接口
    public static final String UPDATE_INFO = "/api/perfect_member";
    //用户名密码登录
    public static final String NORMAL_LOGIN = "/api/login";
    //短信验证码登录
    public static final String SMS_LOGIN = "/api/mobile_login";
    //修改密码
    public static final String CHANGE_PWD = "/api/editpwd";
    //获取用户信息
    public static final String USER_INFO = "/api/get_member";
    //获取充值消费记录列表接口
    public static final String AMOUNT_RECORDS = "/api/get_amount_records";
    //获取积分记录列表接口
    public static final String INTEGRAL_RECORDS = "/api/get_integral_records";
    //获取优惠券列表接口
    public static final String COUPONS_LIST = "/api/get_coupons";
    //获取打折卡列表接口
    public static final String DISCOUNT_CARDS = "/api/get_discount_cards";
    //获取团队列表接口
    public static final String TEAMS_LIST = "/api/get_teams";
    //获取会员充值设置列表接口
    public static final String MEMBER_RECHARGES = "/api/get_member_recharges";
    //充值成功处理接口
    public static final String RECHARGE_SUC = "/api/recharge_suc";

    /**
     * @param urlPath 接口地址
     * @param mystr   待提交的数据，包括数据以及密文
     * @return 返回json字符串 error为0正常
     * @throws Exception
     */
    public static String getInterface(String urlPath, String mystr, String[] arr) {
        StringBuffer sb = new StringBuffer();
        try {
            URL url = new URL(urlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setConnectTimeout(1000 * 50);
            connection.setRequestMethod("POST");
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");
            connection.getOutputStream().write(mystr.toString().getBytes("UTF-8"));
            connection.getOutputStream().flush();
            connection.getOutputStream().close();
            logger.info("提交的数据为：" + urlPath + mystr);

            InputStream inputStream = connection.getInputStream();
            Reader reader = new InputStreamReader(inputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(reader);
            String str = null;

            while ((str = bufferedReader.readLine()) != null) {
                sb.append(str);
            }
            reader.close();
            connection.disconnect();
            logger.info("返回的数据为：" + sb);
        } catch (Exception e) {
            logger.error("获取数据出错,提交的数据为：" + urlPath + mystr, e);
        }
        return sb.toString();
    }

}
