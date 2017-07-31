package com.dzr.util;

import com.dzr.framework.Constant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;


public class SignUtil {
    //	private static String token = "wechat";
    private static Logger logger = LoggerFactory.getLogger(SignUtil.class);

    /**
     * 验证签名
     *
     * @param signature
     * @param timestamp
     * @param nonce
     * @return
     */
    public static boolean checkSignature(String signature, String timestamp,
                                         String nonce) {
        String[] arr = new String[]{Constant.APP_TOKEN, timestamp, nonce};
        // 将token、timestamp、nonce三个参数进行字典序排序
        Arrays.sort(arr);
        StringBuilder content = new StringBuilder();
        for (int i = 0; i < arr.length; i++) {
            content.append(arr[i]);
        }
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            logger.error("验证签名出现异常！" + e.getMessage(), e);
        }

        content = null;
        // 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
        return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
    }

    /**
     * 网页授权接口签名验证
     *
     * @param jsapiTicket
     * @param timestamp
     * @param noncestr
     * @param url
     * @return
     */
    public static String getSignature(String jsapiTicket, String timestamp, String noncestr, String url) {
        String[] arr = new String[]{"jsapi_ticket=" + jsapiTicket, "noncestr=" + noncestr, "timestamp=" + timestamp, "url=" + url};
        Arrays.sort(arr);
        String content = arr[0].concat("&" + arr[1]).concat("&" + arr[2]).concat("&" + arr[3]);
        MessageDigest md = null;
        String tmpStr = null;

        try {
            md = MessageDigest.getInstance("SHA-1");
            // 将三个参数字符串拼接成一个字符串进行sha1加密
            byte[] digest = md.digest(content.toString().getBytes());
            tmpStr = byteToStr(digest);
        } catch (NoSuchAlgorithmException e) {
            logger.error("网页授权接口签名验证出现异常！" + e.getMessage(), e);
        }

        content = null;
        return tmpStr;
    }

    /**
     * 将字节数组转换为十六进制字符串
     *
     * @param byteArray
     * @return
     */
    private static String byteToStr(byte[] byteArray) {
        String strDigest = "";
        for (int i = 0; i < byteArray.length; i++) {
            strDigest += byteToHexStr(byteArray[i]);
        }
        return strDigest;
    }

    /**
     * 将字节转换为十六进制字符串
     *
     * @param mByte
     * @return
     */
    private static String byteToHexStr(byte mByte) {
        char[] digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A',
                'B', 'C', 'D', 'E', 'F'};
        char[] tempArr = new char[2];
        tempArr[0] = digit[(mByte >>> 4) & 0X0F];
        tempArr[1] = digit[mByte & 0X0F];

        String s = new String(tempArr);
        return s;
    }

    public static final String HmacSHA1 = "HmacSHA1";

    /**
     * 生成签名数据
     *
     * @param arr     待加密数组
     * @param urlPath 待加密地址
     * @param key     密钥
     * @return 密文字符串
     * @throws Exception
     */
    public static String getSignature(String[] arr, String urlPath, String key) throws UnsupportedEncodingException, NoSuchAlgorithmException, InvalidKeyException {
        StringBuilder content = new StringBuilder();
        //参数进行字典序排序
        if (arr != null && arr.length > 0) {
            Arrays.sort(arr);
            for (int i = 0; i < arr.length; i++) {
                content.append(arr[i]);
            }
        }

        String data = urlPath + content.toString();
        byte[] keyBytes = key.getBytes("UTF-8");
        SecretKeySpec signingKey = new SecretKeySpec(keyBytes, HmacSHA1);
        Mac mac = Mac.getInstance(HmacSHA1);
        mac.init(signingKey);
        byte[] rawHmac = mac.doFinal(data.getBytes("UTF-8"));
        StringBuilder sb = new StringBuilder();
        for (byte b : rawHmac) {
            sb.append(byteToHexStr(b));
        }
        return sb.toString();
    }

}
