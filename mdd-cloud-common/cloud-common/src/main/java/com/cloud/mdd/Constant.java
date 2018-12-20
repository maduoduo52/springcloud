package com.cloud.mdd;

import com.cloud.mdd.dto.HeaderDto;

import java.util.HashSet;
import java.util.Set;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/8 17:16
 */
public class Constant {

    /**
     * 特定异常code 不引起熔断异常   此code为业务异常
     */
    public static final  int EXCEPTION_CODE = 499;
    /**
     * 特定异常code 不引起熔断异常  此code为验证异常
     */
    public static final  int EXCEPTION__VALIDATION_CODE = 498;

    /** 请求头数据--------------------------------------------------------start **/
    /**
     * 请求id 由zuul生成
     */
    public static final String HEADER_REQ_ID = "req-id";
    /**
     * 获取ip地址
     */
    public static final String HEADER_REQ_IP = "req-ip";
    /**
     * token   jwt生成的验证token 在单实例登录的时候需要使用
     */
    public static final String HEADER_TOKEN = "token";
    /**
     * 请求转发的时候 以及设置进线程变量中是否为保留 true为保留 false为剔除
     */
    public static final boolean HEADER_RETAIN = true;
    /**
     * 需要保留的请求或者需要剔除的请求头 和 HEADER_搭配使用
     */
    public static Set<String> HEADER_SET = new HashSet<>();
    /**
     * 客户ID
     */
    public static final String HEADER_CUSTOMER = "customer-id";
    /**
     * 用户信息
     */
    public static final String HEADER_CUSTOMER_INFO = "customer-info";
    /**
     * appname
     */
    public static final String HEADER_APPNAME = "app_name";

    /**
     * 操作人ID 在服务内部通讯时使用
     */
    public static final String HEADER_OPERATIONUID = "operation-uid";

    /**
     * 安全头
     */
    public static final String CLOUD_AUTHORIZATION = "cloud-Authorization";

    /**
     * 本地请求数据
     */
    public static InheritableThreadLocal<HeaderDto> HEADER = new InheritableThreadLocal();

    /**请求头数据--------------------------------------------------------end****/

    /**
     * 添加请求信息
     * @param headerDto
     */
    public static void set(HeaderDto headerDto){
        HEADER.set(headerDto);
    }

    /**
     * 删除请求头信息
     */
    public static void remove(){
        HEADER.remove();
    }

    /**
     * 获取
     * @return
     */
    public static HeaderDto get(){
        return HEADER.get();
    }


    static {
        HEADER_SET.add(HEADER_REQ_ID);
        HEADER_SET.add(HEADER_TOKEN);
        HEADER_SET.add(HEADER_CUSTOMER_INFO);
        HEADER_SET.add(HEADER_REQ_IP);
        HEADER_SET.add(HEADER_CUSTOMER);
        HEADER_SET.add(HEADER_APPNAME);
        HEADER_SET.add(HEADER_OPERATIONUID);
    }
}
