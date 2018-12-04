package com.cloud.mdd;

import com.cloud.mdd.config.ReqHandlerInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@SpringBootApplication
@EnableConfigServer
@EnableEurekaClient
public class CloudConfigCenterApplication extends WebMvcConfigurerAdapter {

    public static void main(String[] args) {
        SpringApplication.run(CloudConfigCenterApplication.class, args);
    }

    /**
     * 重写拦截规则
     *
     * @param registry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new ReqHandlerInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }
}
