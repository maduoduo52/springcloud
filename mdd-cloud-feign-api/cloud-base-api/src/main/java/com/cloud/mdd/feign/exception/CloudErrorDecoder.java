package com.cloud.mdd.feign.exception;

import com.alibaba.fastjson.JSON;
import com.cloud.mdd.Constant;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Map;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/8 17:14
 */
@Configuration
@Slf4j
public class CloudErrorDecoder implements ErrorDecoder {

    /**
     * 熔断异常处理
     *
     * @param s
     * @param response
     * @return
     */
    @Override
    public Exception decode(String s, Response response) {
        Exception exception = null;
        log.error("收到异常信息:方法:{}", s);
        log.error("url:{}", response.request().url());
        int code = response.status();
        if (code == Constant.EXCEPTION__VALIDATION_CODE) {  //不需要进入熔断
            String msg = "";
            try {
                String returnStr = getStringFromInputStream(response.body().asInputStream());
                if (!StringUtils.isEmpty(returnStr)) {
                    Map<String, String> map = JSON.parseObject(returnStr, Map.class);
                    msg = map.get("msg");
                } else {
                    msg = "空指针异常";
                }
            } catch (Exception e) {
                log.error("", e);
            }
            exception = new CloudHystrixBadRequestException(Constant.EXCEPTION__VALIDATION_CODE, msg);
        } else if (code == Constant.EXCEPTION_CODE) {
            try {
                String returnStr = getStringFromInputStream(response.body().asInputStream());
                exception = new CloudHystrixBadRequestException(Constant.EXCEPTION_CODE, returnStr);
            } catch (Exception e) {
                log.error("", e);
            }
        } else {
            return feign.FeignException.errorStatus(s, response);
        }
        return exception;
    }

    public String getStringFromInputStream(InputStream inputStream) throws Exception {
        String resultData = null;
        //需要返回的结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        while ((len = inputStream.read(data)) != -1) {
            byteArrayOutputStream.write(data, 0, len);
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
        return resultData;
    }
}
