package com.dzr.controller;

import com.dzr.framework.config.Constant;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Description
 * @FileName LoginController
 * @Author dingzr
 * @CreateTime 2017/8/20 16:16 八月
 */

@Controller
@RequestMapping("/login")
public class LoginController {


    @RequestMapping("/getBanners")
    public String getBanners(Model model) {

        //获取某类包厢的内部列表
//        String[] arr = new String[]{"shop_id" + shopId,"room_type_id" + roomTypeId};
//        String mystr = "shop_id=" + shopId + "&room_type_id=" + roomTypeId;
        String[] arr = new String[]{};
        String mystr = "";
        JSONObject bannersInfo = JSONObject.fromObject(Constant.getInterface(Constant.BANNERS, mystr, arr));
        if (0 == bannersInfo.getInt("error_code")) {
            model.addAttribute("banners", bannersInfo.getJSONArray("result"));
        }
        return "map";
    }

}
