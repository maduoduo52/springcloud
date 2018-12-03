package com.mdd.cloud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/3 22:01
 */
@Slf4j
@Component
public class CloudEurekaStateChangeListener {

    @EventListener
    public void listener(EurekaInstanceCanceledEvent eurekaInstanceCanceledEvent) {
        String appName = eurekaInstanceCanceledEvent.getAppName();
        String serverId = eurekaInstanceCanceledEvent.getServerId();
        log.warn("===>服务断线： appName:{}  -> serverId:{}", appName, serverId);
    }
}
