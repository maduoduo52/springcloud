package com.cloud.customer.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableHystrix
@EnableDiscoveryClient
@EnableEurekaClient
@SpringBootApplication
public class CloudCustomerServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudCustomerServerApplication.class, args);
    }

}

