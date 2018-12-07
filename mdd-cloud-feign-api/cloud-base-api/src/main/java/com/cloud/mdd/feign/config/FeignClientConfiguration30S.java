package com.cloud.mdd.feign.config;

import feign.Request;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/7 23:56
 */
@Configuration
public class FeignClientConfiguration30S {

    /**
     * 超时设置
     *
     * @return
     */
    @Bean
    public Request.Options options() {
        return new Request.Options(30000, 30000);
    }
}
