package com.cloud.mdd.log.model;

import com.alibaba.fastjson.JSON;
import lombok.Data;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/4 20:31
 */
@Data
public class MddLog {

    /**
     * 日志级别
     */
    private String level;

    /**
     * 时间毫秒
     */
    private Long timeStamp;

    /**
     * 日志名称
     */
    private String logName;

    /**
     * 线程名称
     */
    private String threadName;

    /**
     * ip
     */
    private String ip;

    /**
     * appName
     */
    private String appName;

    /**
     * 消息体
     */
    private String message;

    /**
     * 消息ID 参考
     */
    private String reqId;

    /**
     * 行数
     */
    private Integer line;

    @Override
    public String toString() {
        return JSON.toJSONString(this);
    }

}
