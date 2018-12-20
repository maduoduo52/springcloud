package com.cloud.mdd.mybatisplus.mybatisplus.exception;

import javax.servlet.http.HttpServletResponse;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/9 15:54
 */
public interface CloudExceptionExtendService {

    Object extend(Exception e, HttpServletResponse response);
}
