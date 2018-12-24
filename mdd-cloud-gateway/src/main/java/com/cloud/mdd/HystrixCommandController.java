package com.cloud.mdd;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/21 20:11
 */
@Slf4j
@RestController
public class HystrixCommandController {

    /**
     * 断路器
     */
    @HystrixCommand(commandKey = "customerHystrixCommand")
    public void hystrixCommand() {
        log.warn("===》customer-server:服务触发断路器");
    }

    /**
     * 服务短路
     */
    @GetMapping("hystrixTimeout")
    public void  hystrixTimeout() {
        log.error("===》customer-server:服务触发断路由");
    }
}
