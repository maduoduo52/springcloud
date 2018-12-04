package com.cloud.mdd.log;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import ch.qos.logback.core.Layout;
import com.alibaba.fastjson.JSON;
import com.cloud.mdd.log.model.LogConstant;
import com.cloud.mdd.log.model.MddImportLog;
import com.cloud.mdd.log.model.MddLog;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.util.Map;

import static ch.qos.logback.classic.Level.*;

/**
 * User: Maduo
 * Date: 2018/1/3
 * Time: 14:56
 * Version: V1.0.0
 */
@Slf4j(topic = "MyAppender")
public class MyAppender extends AppenderBase<ILoggingEvent> {

    private Layout<ILoggingEvent> layout;

    public Layout<ILoggingEvent> getLayout() {
        return layout;
    }

    public void setLayout(Layout<ILoggingEvent> layout) {
        this.layout = layout;
    }


    @Override
    protected void append(ILoggingEvent loggingEvent) {
        if (LogConstant.FLAG) {
            String loggerName = loggingEvent.getLoggerName();
            if (!loggerName.contains("shared.resolver.aws.ConfigClusterResolver")) {
                //格式化过后的数据
                String msg = loggingEvent.getFormattedMessage();
                if (loggerName.equals(LogConstant.IMPORTANTLOG_TOPIC)) {
                    String[] str = layout.doLayout(loggingEvent).split("!`@#\\$_\\$#@`!");
                    MddImportLog mddImportLog = JSON.parseObject(str[0], MddImportLog.class);
                    mddImportLog.setTimeStamp(System.currentTimeMillis());
                    if (!StringUtils.isEmpty(msg)) {
                        Map<String, Object> map = JSON.parseObject(msg, Map.class);
                        mddImportLog.setTitle(map.get("title"));
                        mddImportLog.setType(map.get("type"));
                        mddImportLog.setParam(map.get("param"));
                        mddImportLog.setResult(map.get("result"));
                        mddImportLog.setDeclaringTypeName(map.get("declaringTypeName"));
                        mddImportLog.setSignature(map.get("signature"));
                        mddImportLog.setExtendMap(map.get("extendMap"));
                        mddImportLog.setTableName(map.get("tableName"));
                        mddImportLog.setMessage(null);
                    }
                    LogConstant.add(mddImportLog);
                } else {
                    int levelInt = loggingEvent.getLevel().levelInt;
                    //打印字符以!`@#\$_\$#@`!进行分割
                    //下标为0为JSON数据
                    //下标为1位异常数据
                    String[] str = layout.doLayout(loggingEvent).split("!`@#\\$_\\$#@`!");
                    MddLog mddLog = JSON.parseObject(str[0], MddLog.class);
                    mddLog.setMessage(msg);
                    mddLog.setTimeStamp(System.currentTimeMillis());
                    switch (levelInt) {
                        //debug 级别日志  暂时忽略不管
                        case DEBUG_INT:
                            break;
                        //info 级别日志
                        case INFO_INT:
                            LogConstant.add(mddLog);
                            break;
                        case WARN_INT:
                        case ERROR_INT:
                            if (str.length > 1) {
                                if (!str[1].trim().equals("")) {
                                    mddLog.setMessage(mddLog.getMessage() + "\r\n" + str[1]);
                                }
                            }
                            LogConstant.add(mddLog);
                            break;
                        default:
                    }
                }
            }
        }
    }


}