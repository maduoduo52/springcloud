package com.cloud.mdd.log;

import com.cloud.mdd.log.model.MddImportLog;
import com.cloud.mdd.log.model.MddLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/4 21:52
 */
@Configuration
@Slf4j(topic = "rabbit-log")
public class LogRabbitQueue {

    /**
     * 日志消息队列名称
     */
    public static final String LOG_QUEUE = "mdd-wallet-log";

    /**
     * 重要日志队列名称
     */
    public static final String IMPORTANT_LOG_QUEUE = "mdd-wallet-important-log";

    @Bean
    public Queue log() {
        return new Queue(LOG_QUEUE);
    }

    @Bean
    public Queue important() {
        return new Queue(IMPORTANT_LOG_QUEUE);
    }
}

