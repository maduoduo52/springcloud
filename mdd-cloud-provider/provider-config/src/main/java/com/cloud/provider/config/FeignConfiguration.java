package com.cloud.provider.config;

import com.cloud.mdd.Constant;
import feign.RequestInterceptor;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/20 22:11
 */
@RefreshScope
@Configuration
public class FeignConfiguration {

    /**
     * 创建Feign请求拦截器，设置安全头信息
     * @return
     */
    @Bean
    public RequestInterceptor basicAuthRequestInterceptor() {
        return template ->
                template.header(Constant.HEADER_REQ_ID, Constant.HEADER.get().encode());
    }
}
