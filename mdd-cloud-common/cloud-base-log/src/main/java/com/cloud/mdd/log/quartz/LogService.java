package com.cloud.mdd.log.quartz;

import com.cloud.mdd.log.model.MddLog;

import java.util.List;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/4 21:36
 */
public interface LogService {

    /**
     * 保存
     *
     * @param mddLogs
     */
    void save(List<MddLog> mddLogs);
}
