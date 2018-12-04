package com.cloud.mdd.log.config;

import ch.qos.logback.classic.pattern.ClassicConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;

/**
 * @author Maduo
 * @date 2018/5/30 15:52
 */
@Slf4j
public class ReqIdConfig extends ClassicConverter {

    public static ReqIdService reqIdService;

    public static String reqId(){
        if(reqIdService==null){
            return "-1";
        }
        String str =  reqIdService.getReqId();
        return str==null?"-1":str;
    }

    @Override
    public String convert(ILoggingEvent event) {
        return reqId();
    }
}
