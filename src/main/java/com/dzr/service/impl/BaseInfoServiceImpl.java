package com.dzr.service.impl;

import com.dzr.framework.config.Constant;
import com.dzr.framework.config.UrlConfig;
import com.dzr.service.BaseInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    public JSONArray getBanners() {
        JSONArray banners = null;
        String[] arr = new String[]{};
        String mystr = "";
        JSONObject bannersR = JSONObject.fromObject(Constant.getInterface(urlConfig.getPhp() + Constant.BANNERS, mystr, arr));
        if (0 == bannersR.getInt("error_code")) {
            banners = bannersR.getJSONArray("result");
        } else {
            logger.info("获取的轮播图返回值：" + bannersR.getInt("error_code"));
        }
        return banners;
    }

}
