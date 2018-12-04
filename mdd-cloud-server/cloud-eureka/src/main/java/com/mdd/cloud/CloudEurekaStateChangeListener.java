package com.mdd.cloud;

import com.netflix.appinfo.InstanceInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceCanceledEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRegisteredEvent;
import org.springframework.cloud.netflix.eureka.server.event.EurekaInstanceRenewedEvent;
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
        log.warn("===>服务断开： appName:{}  -> serverId:{}", appName, serverId);
    }

    @EventListener
    public void listener(EurekaInstanceRegisteredEvent eurekaInstanceRegisteredEvent) {
        InstanceInfo instanceInfo = eurekaInstanceRegisteredEvent.getInstanceInfo();
        log.warn("===>服务建立连接：appName:{}", instanceInfo.getAppName());
    }

    @EventListener
    public void listener(EurekaInstanceRenewedEvent eurekaInstanceRenewedEvent) {
        log.warn("===>{} -> {} 服务进行续约", eurekaInstanceRenewedEvent.getServerId(), eurekaInstanceRenewedEvent.getAppName());
    }
}
