package com.cloud.mdd.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Maduo
 * @date 2018/12/19 21:20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public final class ServerUrls {

    /**
     * 服务名称
     */
    private String server;

    /**
     * 请求的地址信息
     */
    private String url;

    /**
     * 服务对应的内网ip地址
     */
    private String ip;
}
