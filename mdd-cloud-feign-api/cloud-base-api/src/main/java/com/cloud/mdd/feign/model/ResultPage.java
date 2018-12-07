package com.cloud.mdd.feign.model;

import com.cloud.mdd.model.Pagination;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/7 23:52
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResultPage<T> {

    /**
     * 当页数据
     */
    private List<T> list;

    /**
     * 页码信息
     */
    private Pagination page;
}
