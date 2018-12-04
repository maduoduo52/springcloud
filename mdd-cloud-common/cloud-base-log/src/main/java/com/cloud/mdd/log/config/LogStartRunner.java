package com.cloud.mdd.log.config;

import com.cloud.mdd.log.model.LogConstant;
import com.cloud.mdd.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @author Maduo
 * @date 2018/5/30 17:06
 */
@Component
@Slf4j
@Order(99)
public class LogStartRunner implements CommandLineRunner {
    @Override
    public void run(String... strings) throws Exception {
        //程序加载完毕
        LogConstant.IS_START = true;
        try {
            //尝试去找 ReqIdConfig的实现类
            ReqIdConfig.reqIdService = SpringUtil.getBean(ReqIdService.class);
        } catch (Exception e) {
        }
    }
}
