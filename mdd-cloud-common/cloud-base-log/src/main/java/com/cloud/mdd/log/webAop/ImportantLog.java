package com.cloud.mdd.log.webAop;


import com.cloud.mdd.log.model.LogConstant;

import java.lang.annotation.*;

import static org.slf4j.spi.LocationAwareLogger.INFO_INT;

/**
 * @author zengliming
 * @date 2018/4/26 16:45
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ImportantLog {
    /**
     * 日志级别
     * @return
     */
    int level() default INFO_INT;

    /**
     * 标题
     * @return
     */
    String title();

    /**
     * 类型
     * @return
     */
    String type() ;

    /**
     * 是否记录参数 默认记录
     * @return
     */
    boolean isLogParam() default true;

    /**
     * 是否记录返回数据 默认记录
     * @return
     */
    boolean isLogResult() default true;

    /**
     * 是否记录请求头信息 默认记录
     * @return
     */
    boolean header() default true;

    /**
     * 剔除那些参数不需要保留的
     * @return
     */
    String[]  exclude() default {};

    /**
     * 存mysql时表明需要存那张表  存ES表明需要存那个索引
     * @return
     */
    String tableName() default LogConstant.IMPORTANTLOG_TABELNAME;

}
