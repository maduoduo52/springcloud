package com.cloud.mdd.log.model;

import lombok.Data;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/4 20:36
 */
@Data
public class MddImportLog extends MddLog {

    /**
     * 标题
     */
    private Object title;
    /**
     * 类型
     */
    private Object type;

    /**
     * 参数
     */
    private Object param;

    /**
     * 返回结果
     */
    private Object result;

    /**
     * 扩展数据
     */
    private Object extendMap;

    /**
     * 类
     */
    private Object declaringTypeName;

    /**
     * 方法
     */
    private Object signature;

    /**
     * table
     */
    private Object tableName;
}
