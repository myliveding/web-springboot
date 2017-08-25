package com.dzr.service.impl;

import com.dzr.framework.config.Constant;
import com.dzr.framework.config.UrlConfig;
import com.dzr.framework.exception.ApiException;
import com.dzr.service.BaseInfoService;
import com.dzr.util.StringUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description
 * @FileName BaseInfoServiceImpl
 * @Author dingzr
 * @CreateTime 2017/8/21 22:22 八月
 */

@Service("baseInfoService")
public class BaseInfoServiceImpl implements BaseInfoService {

    private Logger logger = Logger.getLogger(BaseInfoServiceImpl.class);

    @Autowired
    UrlConfig urlConfig;

    /**
     * 验证码
     *
     * @param mobile
     */
    public void sendSms(String mobile) {
        if (StringUtils.isEmpty(mobile)) {
            throw new ApiException(20101);
        }
        String[] arr = new String[]{"mobile" + mobile};
        String mystr = "mobile=" + mobile;
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.SMS_CODE, mystr, arr));
        throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
    }

    /**
     * 注册方法
     *
     * @param mobile
     */
    public void register(String name, String mobile, String password, String code, HttpServletRequest request) {
        if (StringUtils.isEmpty(mobile)) {
            throw new ApiException(10007, "手机号");
        } else if (!StringUtils.isMobileNo(mobile)) {
            throw new ApiException(10008, "手机号码格式不正确");
        }
        if (StringUtils.isEmpty(code)) {
            throw new ApiException(10007, "验证码");
        } else if (code.length() > 4) {
            throw new ApiException(10008, "验证码格式不正确");
        }
        if (StringUtils.isEmpty(password)) {
            throw new ApiException(10007, "密码");
        }

        String[] arr;
        String mystr = "";
        StringBuffer buffer = new StringBuffer();
        List<String> list = new ArrayList<String>();
        list.add("mobile" + mobile);
        buffer.append("mobile=").append(mobile);
        list.add("sms_code" + code);
        buffer.append("&sms_code=").append(code);
        list.add("password" + password);
        buffer.append("&password=").append(password);
        list.add("reg_invitation_code" + name);
        buffer.append("&reg_invitation_code=").append(name);
        mystr = buffer.toString();
        arr = list.toArray(new String[list.size()]);
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.REGISTER, mystr, arr));
        if (res.getInt("error_code") == 0) {
            JSONObject data = JSONObject.fromObject(res.getString("data"));
            //登录成功就加session
            HttpSession session = request.getSession();
            session.setAttribute("userId", data.getString("member_id"));
            session.setAttribute("mobile", data.getString("mobile"));
        }
        throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
    }

    /**
     * 注册第二步
     *
     * @param name
     * @param sex
     * @param birth
     * @param wechat
     * @param adress
     */
    public void savePerfectInfo(String userId, String name, String sex, String birth, String wechat, String adress) {

        if (StringUtils.isEmpty(name)) {
            throw new ApiException(10007, "姓名");
        } else if (name.length() > 10) {
            throw new ApiException(20104);
        }
        if (StringUtils.isNotEmpty(birth) && !StringUtils.isValidDate(birth)) {
            throw new ApiException(20105);
        }
        if (StringUtils.isEmpty(wechat)) {
            throw new ApiException(10007, "微信号");
        }
        if (StringUtils.isNotEmpty(sex) && !sex.equals("1") && !sex.equals("0")) {
            throw new ApiException(10008, "性别只能是男或女");
        }

        String[] arr;
        String mystr = "";
        StringBuffer buffer = new StringBuffer();
        List<String> list = new ArrayList<String>();
        list.add("member_id" + userId);
        buffer.append("member_id=").append(userId);
        list.add("name" + name);
        buffer.append("&name=").append(name);
        list.add("wechat_num" + wechat);
        buffer.append("&wechat_num=").append(wechat);
        if (StringUtils.isNotEmpty(birth)) {
            list.add("birthday" + birth);
            buffer.append("&birthday=").append(birth);
        }
        if (StringUtils.isNotEmpty(sex)) {
            int s = 0;
            if (sex.equals("男")) {
                s = 1;
            } else if (sex.equals("女")) {
                s = 2;
            }
            list.add("gender" + s);
            buffer.append("&gender=").append(s);
        }
        if (StringUtils.isNotEmpty(adress)) {
            list.add("address" + adress);
            buffer.append("&address=").append(adress);
        }
        mystr = buffer.toString();
        arr = list.toArray(new String[list.size()]);
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.UPDATE_INFO, mystr, arr));
        if (res.getInt("error_code") == 0) {
        } else {
            throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
        }
    }

    /**
     * 获取轮播图
     *
     * @return
     */
    public JSONArray getBanners() {
        JSONArray banners = null;
        String[] arr = new String[]{};
        String mystr = "";
        JSONObject bannersR = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.BANNERS, mystr, arr));
        if (0 == bannersR.getInt("error_code")) {
            banners = bannersR.getJSONArray("result");
        } else {
            logger.info("获取的轮播图返回值：" + bannersR.getInt("error_code"));
            throw new ApiException(bannersR.getInt("error_code"), bannersR.getString("error_msg"));
        }
        return banners;
    }

    /**
     * 获取公司协议
     *
     * @return
     */
    public void getCompanyProtocol() {
    }

    /**
     * 登录方法
     *
     * @param mobile
     * @param password
     * @param code
     */
    public void login(String mobile, String password, String code, HttpServletRequest request) {

        if (StringUtils.isEmpty(mobile)) {
            throw new ApiException(20101);
        }
        if (StringUtils.isEmpty(password) && StringUtils.isEmpty(code)) {
            throw new ApiException(20102);
        }
        if (StringUtils.isNotEmpty(password) && StringUtils.isNotEmpty(code)) {
            throw new ApiException(20103);
        }
        String[] arr;
        String mystr = "";
        StringBuffer buffer = new StringBuffer();
        List<String> list = new ArrayList<String>();
        list.add("mobile" + mobile);
        buffer.append("mobile=").append(mobile);
        if (StringUtils.isNotEmpty(password)) {
            list.add("password" + password);
            buffer.append("&password=").append(password);
        }
        if (StringUtils.isNotEmpty(code)) {
            list.add("sms_code" + code);
            buffer.append("&sms_code=").append(code);
        }
        mystr = buffer.toString();
        arr = list.toArray(new String[list.size()]);
        String url = StringUtils.isNotEmpty(password) ? Constant.NORMAL_LOGIN : Constant.SMS_LOGIN;
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + url, mystr, arr));
        if (res.getInt("error_code") == 0) {
            JSONObject data = JSONObject.fromObject(res.getString("data"));
            //登录成功就加session
            HttpSession session = request.getSession();
            session.setAttribute("userId", data.getString("member_id"));
            session.setAttribute("mobile", data.getString("mobile"));

        } else {
            throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
        }
    }

    /**
     * 获取PHP的用户信息
     *
     * @param memberId
     * @return
     */
    public JSONObject getUserInfo(String memberId) {
        String[] arr = new String[]{"member_id" + memberId};
        String mystr = "member_id=" + memberId;
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.USER_INFO, mystr, arr));
        if (res.getInt("error_code") == 0) {
            res = JSONObject.fromObject(res.getString("member"));
        } else {
            throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
        }
        return res;
    }

    /**
     * 重置密码
     *
     * @param request
     * @param first
     * @param secondary
     */
    public void resetPassword(String first, String secondary, HttpServletRequest request) {
        HttpSession session = request.getSession();

        if (StringUtils.isEmpty(first) || StringUtils.isEmpty(secondary)) {
            throw new ApiException(10007, "密码");
        }

        String userId = (String) request.getSession().getAttribute("userId");
        String[] arr = new String[]{"member_id" + userId, "password" + first, "confirm_pwd" + secondary};
        String mystr = "member_id=" + userId + "&password=" + first + "&confirm_pwd=" + secondary;
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.CHANGE_PWD, mystr, arr));
        if (res.getInt("error_code") == 0) {
            //清空session
            session.setAttribute("userId", "");
        } else {
            throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
        }
    }

    /**
     * 优惠券列表
     *
     * @param perPage
     * @param page
     * @param status
     * @param request
     * @return
     */
    public JSONArray gotoCard(String perPage, String page, String status, HttpServletRequest request) {

        JSONArray jsonArray;
        //获取活动列表
        String[] arr;
        String mystr = "";
        StringBuffer buffer = new StringBuffer();
        List<String> list = new ArrayList<String>();

        String userId = getUserId(request);
        list.add("member_id" + userId);
        buffer.append("&member_id=").append(userId);
        if (StringUtils.isNotEmpty(status)) {
            list.add("status" + status);
            buffer.append("&status=").append(status);
        }
        if (StringUtils.isNotEmpty(perPage)) {
            list.add("per_page" + perPage);
            buffer.append("&per_page=").append(perPage);
        }
        if (StringUtils.isNotEmpty(page)) {
            list.add("page" + page);
            buffer.append("&page=").append(page);
        }
        mystr = buffer.toString();
        arr = list.toArray(new String[list.size()]);
        JSONObject coupons = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.COUPONS_LIST, mystr, arr));
        if (coupons.getInt("error_code") != 0) {
            throw new ApiException(coupons.getInt("error_code"), coupons.getString("error_msg"));
        } else {
            jsonArray = JSONArray.fromObject(coupons.getString("result"));
        }

        return jsonArray;
    }

    /**
     * 获取session里面的用户ID
     *
     * @param request
     * @return
     */
    private String getUserId(HttpServletRequest request) {
        return (String) request.getSession().getAttribute("userId");
    }

}
