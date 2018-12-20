package com.cloud.mdd.feign.exception;

import com.netflix.hystrix.exception.HystrixBadRequestException;
import lombok.Data;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/8 17:21
 */
@Data
public class CloudHystrixBadRequestException extends HystrixBadRequestException {

    private int code;

    public CloudHystrixBadRequestException(String message) {
        super(message);
    }

    public CloudHystrixBadRequestException(int code,String message) {
        super(message);
        this.code = code;
    }
}
