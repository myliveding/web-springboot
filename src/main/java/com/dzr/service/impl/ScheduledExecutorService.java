package com.dzr.service.impl;

import com.dzr.util.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author dingzr
 * @Description 计划任务方法类
 * @ClassName ScheduledExecutorService
 * @since 2017/7/20 11:01
 */

@Component
public class ScheduledExecutorService {

    private static final Logger logger = LoggerFactory.getLogger(ScheduledExecutorService.class);

    //上一次开始执行时间点之后5秒再执行
    @Scheduled(fixedRate = 5000)
    //上一次执行完毕时间点之后5秒再执行
//    @Scheduled(fixedDelay = 5000)
    //第一次延迟1秒后执行，之后按fixedRate的规则每5秒执行一次
//    @Scheduled(initialDelay=1000, fixedRate=5000)
    //通过cron表达式定义规则
//    @Scheduled(cron="*/5 * * * * *")
    public void printTime(){
        logger.info("现在时间：" + DateUtils.getDateNowTime());
    }

}
