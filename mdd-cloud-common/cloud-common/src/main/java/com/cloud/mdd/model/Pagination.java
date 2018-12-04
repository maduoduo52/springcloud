package com.cloud.mdd.model;

import lombok.Data;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/4 21:05
 */
@Data
public class Pagination {

    /**
     * 总数
     */
    private int total;

    /**
     * 每页显示条数，默认 10
     */
    private int size = 10;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 当前页
     */
    private int current = 1;
}
