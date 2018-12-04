package com.cloud.mdd.log.quartz;

import com.cloud.mdd.log.model.LogConstant;
import com.cloud.mdd.log.model.MddLog;
import com.cloud.mdd.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/4 21:43
 */
@Component
@EnableScheduling
@Slf4j
public class QuartzService {

    private LogService logService;

    /**
     * 定时任务
     */
    @Scheduled(fixedDelay = 3000)
    public void timerToNow() {
        //程序员没有启动完成 不允许执行
        if (!LogConstant.IS_START) {
            return;
        }
        //当不允许产生日志时
        if (!LogConstant.FLAG) {
            return;
        }
        try {
            if (logService == null) {
                //获取实现类
                logService = SpringUtil.getBean(LogService.class);
            }
        } catch (Exception e) {
        }
        //当实现类存在时
        if (logService != null) {
            //保存日志
            logService.save(LogConstant.getAll());
        } else {
            if (LogConstant.FLAG) {
                //当实现类不存在时
                LogConstant.FLAG = false;  //不允许再产生日志
                //获取所有数据
                List<MddLog> mddLogs = LogConstant.getAll();
                log.warn("警告完成：你有{}条数据即将销毁", mddLogs.size());
                //清除 释放内存
                mddLogs.clear();
            }
        }
    }
}
