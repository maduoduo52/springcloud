package com.cloud.mdd.log.webAop;

import java.util.Map;

/**
 * @author zengliming
 * @date 2018/4/26 17:08
 */
public interface ImportantLogService {
    /**
     * 返回需要扩展的信息
     * @param importantLog 注解对象
     * @return
     * @throws Throwable
     */
    Map<String,String> importantLogExtendMap(ImportantLog importantLog)throws Throwable;
}
