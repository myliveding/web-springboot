package com.dzr.framework.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

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
public class Constant {

    @Autowired
    static InvokingUrl invokingUrl;
    private static Logger logger = LoggerFactory.getLogger(Constant.class);

    //获取轮播图
    public static final String BANNERS = invokingUrl.getPhp() + "/api/get_banners";
    //获取产品分类
    public static final String PRODUCT_CATES = invokingUrl.getPhp() + "/api/get_product_cates";
    //获取产品列表
    public static final String PRODUCTS = invokingUrl.getPhp() + "/api/get_products";
    //获取产品详情
    public static final String PRODUCT_DETAIL = invokingUrl.getPhp() + "/api/get_product";
    //获取活动列表
    public static final String ACTIVITYS = invokingUrl.getPhp() + "/api/get_activitys";
    //获取活动详情
    public static final String ACTIVITY_DETAIL = invokingUrl.getPhp() + "/api/get_activity";


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
