package com.dzr.controller;

import com.dzr.framework.config.Constant;
import com.dzr.framework.config.InvokingUrl;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @Description
 * @FileName ActivityController
 * @Author dingzr
 * @CreateTime 2017/8/20 16:17 八月
 */

@Controller
@RequestMapping("/activity")
public class ActivityController {

    @Autowired
    InvokingUrl invokingUrl;

    @RequestMapping("/activitys")
    public String getActivitys(Model model, HttpServletRequest request) {

        String perPage = request.getParameter("perPage");
        String page = request.getParameter("page");
        //获取活动列表
        String[] arr = new String[]{"per_page" + perPage, "page" + page};
        String mystr = "per_page=" + perPage + "&page=" + page;
        JSONObject bannersInfo = JSONObject.fromObject(Constant.getInterface(invokingUrl.getPhp() + Constant.ACTIVITYS, mystr, arr));
        if (0 == bannersInfo.getInt("error_code")) {
            model.addAttribute("activitys", bannersInfo.getJSONArray("result"));
        }
        return "map";
    }

    @RequestMapping("/{activityDetail}")
    public String getActivityDetail(@PathVariable("activityDetail") String activityDetail, Model model) {

        //获取活动详情
        String[] arr = new String[]{"activity_id" + activityDetail};
        String mystr = "activity_id=" + activityDetail;
        JSONObject info = JSONObject.fromObject(Constant.getInterface(invokingUrl.getPhp() + Constant.ACTIVITY_DETAIL, mystr, arr));
        if (0 == info.getInt("error_code")) {
            model.addAttribute("info", info.getJSONObject("result"));
        }
        return "map";
    }
}
