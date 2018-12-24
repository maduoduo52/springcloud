package com.cloud.customer.server.controller;

import com.cloud.mdd.model.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/22 16:03
 */
@Slf4j
@RestController
@RequestMapping("customer")
public class CustomerController {

    @PostMapping("test.html")
    public Result test() {
        return Result.success("测试");
    }
}
