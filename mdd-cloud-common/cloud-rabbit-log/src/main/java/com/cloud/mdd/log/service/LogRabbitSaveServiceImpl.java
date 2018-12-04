package com.cloud.mdd.log.service;

import com.cloud.mdd.log.LogSenderService;
import com.cloud.mdd.log.model.MddLog;
import com.cloud.mdd.log.quartz.LogService;
import com.cloud.mdd.utils.CollectionUtilPlus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/4 22:04
 */
@Service
public class LogRabbitSaveServiceImpl implements LogService {

    @Autowired
    private LogSenderService logSenderService;

    @Override
    public void save(List<MddLog> mddLogs) {
        if (CollectionUtilPlus.isNotNullOrEmpty(mddLogs)) {
            logSenderService.sendList(mddLogs);
        }
    }
}
