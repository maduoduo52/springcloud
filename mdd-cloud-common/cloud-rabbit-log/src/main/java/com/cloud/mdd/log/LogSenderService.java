package com.cloud.mdd.log;

import com.alibaba.fastjson.JSON;
import com.cloud.mdd.log.model.MddImportLog;
import com.cloud.mdd.log.model.MddLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/4 21:57
 */
@Configuration
@Slf4j(topic = "rabbit-log")
public class LogSenderService {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 发送
     *
     * @param mddLog
     */
    public void send(MddLog mddLog) {
        if (mddLog != null) {
            if (mddLog instanceof MddImportLog) {
                //如果是重要日志
                MddImportLog mddImportLog = (MddImportLog) mddLog;
                if (StringUtils.isEmpty(mddImportLog.getType())) {
                    this.rabbitTemplate.convertAndSend(LogRabbitQueue.IMPORTANT_LOG_QUEUE, JSON.toJSONString(mddImportLog));
                } else {
                    log.warn("ImportantLog为设置type属性值，无法记录 原始数据:{}", mddImportLog);
                }
            } else {
                if (!StringUtils.isEmpty(mddLog.getMessage())) {
                    this.rabbitTemplate.convertAndSend(LogRabbitQueue.LOG_QUEUE, JSON.toJSONString(mddLog));
                } else {
                    log.warn("不记录为空的日志数据:{}", mddLog);
                }
            }
        } else {
            log.warn("不记录为空的日志数据");
        }
    }

    /**
     * 批量发送
     *
     * @param mddLogs
     */
    public void sendList(List<MddLog> mddLogs) {
        for (int i = mddLogs.size() - 1; i >= 0; i--) {
            //先记录重要日志
            MddLog mddLog = mddLogs.get(i);
            if (mddLog instanceof MddImportLog) {
                MddImportLog mddImportLog = (MddImportLog) mddLog;
                if (!StringUtils.isEmpty(mddImportLog.getType())) {
                    this.rabbitTemplate.convertAndSend(LogRabbitQueue.IMPORTANT_LOG_QUEUE, JSON.toJSONString(mddImportLog));
                } else {
                    log.warn("ImportantLog为设置type属性值，无法记录 原始数据:{}", mddImportLog);
                }
                mddLogs.remove(i);
            }
        }
        if (mddLogs.size() > 0) {
            this.rabbitTemplate.convertAndSend(LogRabbitQueue.LOG_QUEUE, JSON.toJSONString(mddLogs));
        }
    }
}
