package com.cloud.mdd.dto;

import com.alibaba.fastjson.JSON;
import com.cloud.mdd.utils.IdWorkerUtil;
import lombok.Data;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Herder 相关数据
 *
 * @author Maduo
 * @date 2018/12/19 21:54
 */
@Data
public final class HeaderDto {

    /**
     * 安全头信息
     */
    private String authorization;

    /**
     * 请求ID
     */
    private Long reqId;

    /**
     * 请求 ip
     */
    private String reqIp;

    /**
     * 请求服务记录
      */
    private List<ServerUrls> servers;

    /**
     * 请求的emp 即admin后台用户ID
     */
    private Long empId;

    /**
     * 请求的用户ID
     */
    private Long customerId;

    /**
     * 扩展字段
     * app 用户的信息 手机信号
     */
    private Map<String,Object> exMaps;

    /**
     * 私有化构造方法
     */
    private HeaderDto(){}

    /**
     * 初始化
     * @param ip
     * @return
     */
    public static HeaderDto init(String ip){
        HeaderDto herderDto = new HeaderDto();
        herderDto.setReqIp(ip);
        herderDto.setReqId(IdWorkerUtil.getId());
        return herderDto;
    }

    /**
     * 初始化
     * @param ip
     * @param empId
     * @return
     */
    public static HeaderDto initEmp(String ip, Long empId){
        HeaderDto herderDto = new HeaderDto();
        herderDto.setReqIp(ip);
        herderDto.setReqId(IdWorkerUtil.getId());
        herderDto.setEmpId(empId);
        return herderDto;
    }

    /**
     * 初始化
     * @param ip
     * @param customerId
     * @return
     */
    public static HeaderDto initCustomer(String ip, Long customerId){
        HeaderDto herderDto = new HeaderDto();
        herderDto.setReqIp(ip);
        herderDto.setReqId(IdWorkerUtil.getId());
        herderDto.setCustomerId(customerId);
        return herderDto;
    }

    /**
     * 添加安全拖验证信息
     * @param authorization
     * @return
     */
    public HeaderDto addAuthorization(String authorization){
        this.authorization = authorization;
        return this;
    }

    /**
     * 添加本次服务器信息
     * @param server
     * @param url
     * @param ip
     * @return
     */
    public HeaderDto addServer(String server, String url, String ip){
        if(this.servers==null){
            this.servers = new ArrayList<>();
        }
        this.servers.add(new ServerUrls(server,url,ip));
        return this;
    }



    /**
     * 添加扩展信息
     * @param key
     * @param value
     * @return
     */
    public HeaderDto addEx(String key, String value){
        if(this.exMaps==null){
            this.exMaps = new HashMap<>();
        }
        this.exMaps.put(key,value);
        return this;
    }

    /**
     * 添加扩展信息
     * @param exMaps
     * @return
     */
    public HeaderDto addExAll(Map<String,Object> exMaps){
        if(this.exMaps==null){
            this.exMaps = new HashMap<>();
        }
        this.exMaps.putAll(exMaps);
        return this;
    }

    /**
     * url 编码
     * @return
     */
    public String encode(){
        if(this==null){
            return null;
        }
        String str = JSON.toJSONString(this);
        return str;
    }

    /**
     * url 解码
     * @param str
     * @return
     */
    public static HeaderDto decode(String str){
       str =  URLDecoder.decode(str);
       return JSON.parseObject(str,HeaderDto.class);
    }

    /**
     *  url 编码
     * @param dto
     * @return
     */
    public static String encode(HeaderDto dto){
        String str = JSON.toJSONString(dto);
        return URLEncoder.encode(str);
    }

}
