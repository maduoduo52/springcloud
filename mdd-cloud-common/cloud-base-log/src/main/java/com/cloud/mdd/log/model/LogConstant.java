package com.cloud.mdd.log.model;

import com.cloud.mdd.log.quartz.LogService;
import com.cloud.mdd.utils.SpringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/4 21:25
 */
@Slf4j
public class LogConstant {

    @Autowired
    private static LogService logService;

    public static void serviceInit() {
        if (logService == null) {
            logService = SpringUtil.getBean(LogService.class);
        }
    }

    /**
     * 最多存储多少日志 超过上限的时候清空 不能因为日志而影响其他程序
     */
    public static int NUM = 50;
    /**
     * 标志 代表是否允许添加数据进list
     */
    public static boolean FLAG = true;
    /**
     * 程序已经启动完成  只有当程序（spring容器） 启动完成后那么 MQ 定时任务等才能加载完毕
     */
    public static boolean IS_START = false;

    /**
     * 重要日志 topic
     */
    public static final String IMPORTANTLOG_TOPIC = "IMPORTANTLOG_TOPIC";

    /**
     * 存mysql时表明需要存那张表  存ES表明需要存那个索引
     */
    public final static String IMPORTANTLOG_TABELNAME = "mdd-important-log";

    /**
     * 存日志数据 放在静态区中
     * <p>
     * volatile 关键字作用：1、保证此变量对所有的线程的可见性，保证了新值能立即同步到主内存，以及每次使用前立即从主内存刷新
     * 2、禁止指令重排序优化
     */
    private volatile static List<MddLog> MDDLOGS = new ArrayList<>();

    /**
     * 获取全部数据
     * 使用切换地址来获取值
     *
     * @return
     */
    public static List<MddLog> getAll() {
        List<MddLog> logList;
        //同步从静态区读取日志
        synchronized (MDDLOGS) {
            //将静态区日志赋值到新的list中
            logList = MDDLOGS;
            //new一个新的地址
            MDDLOGS = new ArrayList<>();
        }
        return logList;
    }

    public static void add(MddLog mddLog) {
        serviceInit();
        //日志放入静态区
        MDDLOGS.add(mddLog);
        //判断程序有没有加载完毕  如果程序没有加载完毕则记录所有日志 不进入下列判断
        if (LogConstant.IS_START) {
            //当数量到达NUM的时候 则清除日志数据 避免日志数量过多
            if (MDDLOGS.size() > NUM) {

                if (logService == null) {
                    LogConstant.FLAG = false;
                    log.warn("你还没有实现LogService，日志无法存储！ {}条日志被清除", MDDLOGS.size());
                    MDDLOGS.clear();
                } else {
                    log.warn("日志数量达到上限，开始主动调用保存:{}", MDDLOGS.size());
                    logService.save(getAll());
                }
            }
        }
    }

}
