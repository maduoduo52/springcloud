package com.cloud.mdd.mybatisplus.mybatisplus.exception;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/9 15:50
 */
@Data
@Slf4j
public class CloudException extends RuntimeException {

    private int code;

    public CloudException(String message, int code) {
        super(message);
        this.code = code;
    }

    public CloudException(String message) {
        super(message);
    }

}
