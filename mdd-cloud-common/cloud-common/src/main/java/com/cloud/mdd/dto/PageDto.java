package com.cloud.mdd.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/4 20:55
 */
@Data
public class PageDto<T> {

    /**
     * 扩展参数
     */
    private T params;
    /**
     * 当前页数
     *
     * @JSONField(serialize = false) 该注解 用来忽略不想序列化的字段的
     */
    @JSONField(serialize = false)
    private Integer currentPage = 1;
    /**
     * 每页显示条数
     */
    @JSONField(serialize = false)
    private Integer pageSize = 10;
    /**
     * 排序规则（asc：正序，desc：倒序）
     */
    @JSONField(serialize = false)
    private String sortType;
    /**
     * 排序字段
     */
    @JSONField(serialize = false)
    private String sortField;
    /**
     * 查询关键字
     */
    @JSONField(serialize = false)
    private String keyword;
}
