package com.dzr.service.impl;

import com.dzr.framework.config.Constant;
import com.dzr.framework.config.UrlConfig;
import com.dzr.framework.exception.ApiException;
import com.dzr.po.wx.WechatUser;
import com.dzr.service.BaseInfoService;
import com.dzr.service.WechatService;
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
    @Autowired
    WechatService wechatService;

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
        if (res.getInt("error_code") == 0) {
            throw new ApiException(10008, res.getString("error_msg"));
        }
        throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
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

        //登录成功就加session
        HttpSession session = request.getSession();
        Object openIdObject = session.getAttribute("openid");
        String openId = null != openIdObject ? openIdObject.toString() : "";

        String[] arr;
        String mystr;
        StringBuilder buffer = new StringBuilder();
        List<String> list = new ArrayList<>();
        list.add("mobile" + mobile);
        buffer.append("mobile=").append(mobile);

        if (!"".equals(openId)) {
            list.add("openid" + openId);
            buffer.append("&openid=").append(openId);
        }

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
            session.setAttribute("userId", data.getString("member_id"));
            session.setAttribute("mobile", data.getString("mobile"));

        } else {
            throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
        }
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
        String mystr;
        StringBuffer buffer = new StringBuffer();
        List<String> list = new ArrayList<>();
        list.add("mobile" + mobile);
        buffer.append("mobile=").append(mobile);
        list.add("sms_code" + code);
        buffer.append("&sms_code=").append(code);
        list.add("password" + password);
        buffer.append("&password=").append(password);
        list.add("reg_invitation_code" + name);
        buffer.append("&reg_invitation_code=").append(name);

        //登录成功就加session
        HttpSession session = request.getSession();
        getUserInfoFromWechat(list, buffer, session);

        mystr = buffer.toString();
        arr = list.toArray(new String[list.size()]);
        JSONObject res = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.REGISTER, mystr, arr));
        if (res.getInt("error_code") == 0) {
            JSONObject data = JSONObject.fromObject(res.getString("data"));

            String memberId = data.getString("member_id");
            session.setAttribute("userId", memberId);
            session.setAttribute("mobile", data.getString("mobile"));
        }
        throw new ApiException(res.getInt("error_code"), res.getString("error_msg"));
    }

    /**
     * 获取微信用户信息并添加到用户信息表
     *
     * @param list
     * @param buffer
     * @param session
     */
    private void getUserInfoFromWechat(List<String> list, StringBuffer buffer, HttpSession session) {
        Object openIdObject = session.getAttribute("openid");
        String openId = null != openIdObject ? openIdObject.toString() : "";
        if (!"".equals(openId)) {
            list.add("openid" + openId);
            buffer.append("&openid=").append(openId);
            //去获取微信用户信息
            WechatUser user = wechatService.getUserInfo(openId);
            if (null != user.getNickname()) {
                list.add("wechat_nickname" + user.getNickname());
                buffer.append("&wechat_nickname=").append(user.getNickname());
            }
            if (null != user.getHeadimgurl()) {
                list.add("head_url" + user.getHeadimgurl());
                buffer.append("&head_url=").append(user.getHeadimgurl());
            }
        }
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
        if (StringUtils.isNotEmpty(sex) && !sex.equals("1") && !sex.equals("2")) {
            throw new ApiException(10008, "性别只能是男或女");
        }

        String[] arr;
        String mystr;
        StringBuffer buffer = new StringBuffer();
        List<String> list = new ArrayList<>();
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
            list.add("gender" + sex);
            buffer.append("&gender=").append(sex);
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
            throw new ApiException(10008, res.getString("error_msg"));
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
     * 团队列表
     *
     * @param perPage
     * @param page
     * @param request
     * @return
     */
    public JSONArray gotoTeam(String perPage, String page, HttpServletRequest request) {

        JSONArray jsonArray;
        String[] arr;
        String mystr = "";
        StringBuffer buffer = new StringBuffer();
        List<String> list = new ArrayList<>();

        String userId = getUserId(request);
        list.add("member_id" + userId);
        buffer.append("&member_id=").append(userId);
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
        JSONObject teams = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.TEAMS_LIST, mystr, arr));
        if (teams.getInt("error_code") != 0) {
            throw new ApiException(10008, teams.getString("error_msg"));
        } else {
            jsonArray = JSONArray.fromObject(teams.getString("result"));
        }
        return jsonArray;
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
    public JSONArray gotoCouponsCard(String perPage, String page, String status, HttpServletRequest request) {
        JSONArray jsonArray = getCardList(perPage, page, status, request, Constant.COUPONS_LIST);
        return jsonArray;
    }


    /**
     * 打折券发放列表
     *
     * @param perPage
     * @param page
     * @param status
     * @param request
     * @return
     */
    public JSONArray gotoDiscountCard(String perPage, String page, String status, HttpServletRequest request) {
        JSONArray jsonArray = getCardList(perPage, page, status, request, Constant.DISCOUNT_CARDS);
        return jsonArray;
    }

    /**
     * 调用去获取卡券列表
     *
     * @param perPage
     * @param page
     * @param status
     * @param request
     * @param url
     * @return
     */
    private JSONArray getCardList(String perPage, String page, String status, HttpServletRequest request, String url) {
        JSONArray jsonArray;
        //获取活动列表
        String[] arr;
        String mystr;
        StringBuilder buffer = new StringBuilder();
        List<String> list = new ArrayList<>();

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
        JSONObject coupons = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + url, mystr, arr));
        if (coupons.getInt("error_code") != 0) {
            throw new ApiException(10008, coupons.getString("error_msg"));
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

    /**
     * 获取系统信息
     * @param type 默认为全部 1会员卡封面 2服务协议 3会员权益 4积分规则
     * @return
     */
    public JSONObject getSystemInfo(String type) {
        if ("".equals(type)) {
            throw new ApiException(10007, "类型");
        }

        String[] arr = new String[]{"type" + type};
        String mystr = "type=" + type;
        JSONObject info = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.SYSTEM_SETTING, mystr, arr));
        if (info.getInt("error_code") == 0) {
            info = JSONObject.fromObject(info.getString("result"));
        } else {
            throw new ApiException(info.getInt("error_code"), info.getString("error_msg"));
        }
        return info;
    }

    /**
     * 获取活动列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    public JSONArray getActivitysPaging(String page, String pageSize) {

        //获取活动列表
        String[] arr;
        String mystr;
        StringBuilder buffer = new StringBuilder();
        List<String> list = new ArrayList<>();

        if (StringUtils.isNotEmpty(page)) {
            list.add("page" + page);
            buffer.append("&page=").append(page);
        }
        if (StringUtils.isNotEmpty(pageSize)) {
            list.add("per_page" + pageSize);
            buffer.append("&per_page=").append(pageSize);
        }
        mystr = buffer.toString();
        arr = list.toArray(new String[list.size()]);
        final JSONObject acts = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.ACTIVITYS, mystr, arr));
        JSONArray jsonArray = null;
        if (acts.getInt("error_code") == 0) {
            jsonArray = acts.getJSONArray("result");
        } else {
            throw new ApiException(10008, acts.getString("error_msg"));
        }
        return jsonArray;
    }

    public JSONObject getActivityDetail(String activityDetail) {
        //获取活动详情
        String[] arr = new String[]{"activity_id" + activityDetail};
        String mystr = "activity_id=" + activityDetail;
        JSONObject info = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.ACTIVITY_DETAIL, mystr, arr));
        if (0 == info.getInt("error_code")) {
            info = info.getJSONObject("result");
        } else {
            throw new ApiException(10008, info.getString("error_msg"));
        }
        return info;
    }


}
