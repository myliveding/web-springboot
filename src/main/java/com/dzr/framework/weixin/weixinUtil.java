package com.dzr.framework.weixin;

import com.dzr.framework.Constant;
import com.dzr.po.wx.AccessToken;
import com.dzr.po.wx.Menu;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.json.JacksonJsonParser;

import javax.net.ssl.*;
import java.io.*;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dingzr
 * @Description 微信的工具类
 * @ClassName WeixinUtil
 * @since 2017/7/27 14:24
 */
public class WeixinUtil {


    private static Logger logger = LoggerFactory.getLogger(WeixinUtil.class);
    /**
     * 调用access_token接口URL
     */
    public static final String ACCESS_TOKEN_URL = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
    /**
     * 调用创建菜单接口URL
     */
    public static final String MENU_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/menu/create?access_token=ACCESS_TOKEN";

    /**
     * 调用获取菜单接口URL
     */
    public static final String MENU_GET_URL = "https://api.weixin.qq.com/cgi-bin/menu/get?access_token=ACCESS_TOKEN";

    /**
     * 调用获取用户信息接口URL
     */
    public static final String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID";

    /**
     * 调用发送消息接口URL
     */
    public static final String MESSAGE_CUSTOM_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/custom/send?access_token=ACCESS_TOKEN";
    /**
     * 获取消息模板ID
     */
    public static final String MESSAGE_TEMPLATE_ID_URL = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token=ACCESS_TOKEN";
    /**
     * 发送消息模板消息
     */
    public static final String MESSAGE_TEMPLATE_SEND_URL = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";
    /**
     * 调用生成二维码接口URL
     */
    public static final String QRCODE_CREATE_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=ACCESS_TOKEN";
    /**
     * 调用网页授权接口URL 通过code换取网页授权access_token
     */
    public static final String OAUTH2_ACCESS_TOKEN_URL = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
    /**
     * 调用微信JS接口的临时票据
     */
    public static final String TICKET_GETTICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    // 上传多媒体文件到微信服务器
    public static final String UPLOAD_MEDIA_URL = "http://file.api.weixin.qq.com/cgi-bin/media/upload?access_token=ACCESS_TOKEN&type=TYPE";
    public static final String DOWNLOAD_MEDIA_URL = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
    //通过用户的OpenID查询其所在的GroupID
    public static final String USER_GROUP = "https://api.weixin.qq.com/cgi-bin/groups/getid?access_token=ACCESS_TOKEN";
    //移动用户分组
    public static final String USER_GROUP_MOVE = "https://api.weixin.qq.com/cgi-bin/groups/members/update?access_token=ACCESS_TOKEN";


    /**
     * http请求  handleRequest
     *
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
        StringBuffer buffer = new StringBuffer();
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
        } catch (ConnectException ce) {
            logger.error("Weixin server connection timed out.", ce);
        } catch (Exception e) {
            logger.error("https request error:{}", e);
        }
        return buffer.toString();
    }

    /**
     * https的请求（微信支付在https环境下需要加载微信api安全证书（下载自微信商户平台））
     *
     * @param requestUrl
     * @param requestMethod
     * @param outputStr
     * @return
     */
    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        String jsonObject = null;
        StringBuffer buffer = new StringBuffer();
        try {
            // 证书文件(微信商户平台-账户设置-API安全-API证书-下载证书)
            String keyStorePath = WeixinUtil.class.getResource("/").getPath() + "/apiclient_cert.p12";
            // 证书密码（默认为商户ID）
            String password = Constant.MCH_ID;
            // 实例化密钥库
            KeyStore ks = KeyStore.getInstance("PKCS12");
            // 获得密钥库文件流
            FileInputStream fis = new FileInputStream(keyStorePath);
            // 加载密钥库
            ks.load(fis, password.toCharArray());
            // 关闭密钥库文件流
            fis.close();
            // 实例化密钥库
            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            // 初始化密钥工厂
            kmf.init(ks, password.toCharArray());

            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(kmf.getKeyManagers(), null, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
            httpUrlConn.setSSLSocketFactory(ssf);

            httpUrlConn.setDoOutput(true);
            httpUrlConn.setDoInput(true);
            httpUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod))
                httpUrlConn.connect();

            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String str = null;
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            bufferedReader.close();
            inputStreamReader.close();
            // 释放资源
            inputStream.close();
            inputStream = null;
            httpUrlConn.disconnect();
            jsonObject = buffer.toString();
        } catch (ConnectException ce) {
            logger.error("Weixin server connection timed out.", ce);
        } catch (Exception e) {
            logger.error("https request error:{}", e);
        }
        return jsonObject;
    }

    /**
     * 刷新accessToken
     *
     * @param appid     应用ID
     * @param appsecret 应用密钥
     * @return
     */
    public static AccessToken getAccessTokenForWXService(String appid, String appsecret) {

        AccessToken accessToken = null;
        String requestUrl = ACCESS_TOKEN_URL.replace("APPID", appid).replace("APPSECRET", appsecret);
        String accessTokenTemp = httpRequest(requestUrl, "GET", null);
        // 如果请求成功
        if (!"".equals(accessTokenTemp)) {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                accessToken = objectMapper.readValue(accessTokenTemp, AccessToken.class);

                accessToken.setToken(jsonObject.getString("access_token"));
                accessToken.setExpiresIn(jsonObject.getInt("expires_in"));
            } catch (JsonParseException e) {
                accessToken = null;
                logger.error("获取token失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            } catch (IOException e) {

            }
        }
        return accessToken;
    }


    /**
     * 创建菜单
     *
     * @param menu        菜单实例
     * @param accessToken 有效的access_token
     * @return 0表示成功，其他值表示失败
     */
    public static int createMenu(Menu menu, String accessToken) {


        int result = 0;

        // 拼装创建菜单的url
        String url = MENU_CREATE_URL.replace("ACCESS_TOKEN", accessToken);
        // 将菜单对象转换成json字符串
        ObjectMapper mapper = new ObjectMapper();
        String jsonMenu = mapper.writeValueAsString(menu);
        // 调用接口创建菜单
        String = httpRequest(url, "POST", jsonMenu);

        if (null != jsonObject) {
            if (0 != jsonObject.getInt("errcode")) {
                result = jsonObject.getInt("errcode");
                logger.error("创建菜单失败 errcode:{} errmsg:{}", jsonObject.getInt("errcode"), jsonObject.getString("errmsg"));
            }
        }

        return result;
    }


    /**
     * 获取菜单
     *
     * @param accessToken 有效的access_token
     * @return
     */
    public static JSONObject getMenu(String accessToken) {
        String userUrl = MENU_GET_URL.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonObject = httpRequest(userUrl, "GET", null);
        return jsonObject;
    }

    /**
     * 获取用户详细信息
     *
     * @param accessToken 有效的access_token
     * @param openid      对应的openid
     * @return
     */
    public static JSONObject getUserInfo(String accessToken, String openid) {
        String userUrl = USER_INFO_URL.replace("ACCESS_TOKEN", accessToken).replace("OPENID", openid);
        JSONObject jsonObject = httpRequest(userUrl, "GET", null);
        return jsonObject;
    }

    /**
     * 发送文本消息
     *
     * @param accessToken 有效的access_token
     * @param context     待发送的文本对象
     * @return
     */
    public static String setSendMessage(String accessToken, String context) {
        String url = MESSAGE_TEMPLATE_SEND_URL.replace("ACCESS_TOKEN", accessToken);
        // 将文本对象转换成json字符串
        // 调用接口发送消息
        logger.info("发送文本消息00：" + context);
        JSONObject jsonObject = httpRequest(url, "POST", context);
        logger.info("发送文本消息结果：" + jsonObject.toString());
        return jsonObject;
    }

    /**
     * 网页授权 通过code换取网页授权access_token
     *
     * @param appid     应用ID
     * @param appsecret 应用密钥
     * @param code      填写第一步获取的code参数
     * @return
     */
    public static JSONObject getUserInfoOfScope(String appid, String appsecret, String code) {
        String userUrl = OAUTH2_ACCESS_TOKEN_URL.replace("APPID", appid).replace("SECRET", appsecret).replace("CODE", code);
        JSONObject jsonObject = httpRequest(userUrl, "GET", null);
        return jsonObject;
    }

    /**
     * 获取微信JS接口的临时票据
     *
     * @param accessToken 有效的access_token
     * @return
     */
    public static JSONObject getJSAPITicket(String accessToken) {
        String url = TICKET_GETTICKET_URL.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonObject = httpRequest(url, "GET", null);
        logger.info("获取微信JS接口的临时票据:" + jsonObject.toString());
        return jsonObject;
    }

    /**
     * 消息模版ID获取
     *
     * @param accessToken
     * @param shortId
     * @return
     */
    public static JSONObject getTemplateId(String accessToken, String shortId) {
        String requestUrl = MESSAGE_TEMPLATE_ID_URL.replace("ACCESS_TOKEN", accessToken);
        JSONObject jsonObject = httpRequest(requestUrl, "POST", shortId);
        return jsonObject;
    }

    /**
     * 发送模板消息
     *
     * @param accessToken 有效的access_token
     * @param content     待发送的消息对象
     * @return
     */
    public static JSONObject sendTemplateMessage(String accessToken, String content) {
        String url = MESSAGE_TEMPLATE_SEND_URL.replace("ACCESS_TOKEN", accessToken);
        // 调用接口发送消息
        JSONObject jsonObject = httpRequest(url, "POST", content);
        return jsonObject;
    }

}
