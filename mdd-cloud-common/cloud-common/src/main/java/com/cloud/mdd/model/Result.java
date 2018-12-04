package com.cloud.mdd.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cloud.mdd.enums.ResultCodeEnum;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc:  返回数据对象
 * @Author Maduo
 * @Create 2018/12/4 21:06
 */
@Data
public class Result {
    /**
     * code值
     */
    private int code;
    /**
     * 数据
     */
    private Object data;
    /**
     * 描述信息
     */
    private String msg;

    /**
     * 私有无参构造 不允许外部私自新加返回状态
     */
    private Result() {
    }

    /**
     * 带参数构造
     *
     * @param codeEnum
     */
    public Result(ResultCodeEnum codeEnum) {
        this.code = codeEnum.getStatus();
        this.msg = codeEnum.getDesc();
    }

    /**
     * 带参数构造
     *
     * @param resultCodeEnum 返回结果编码
     * @param msg            结果描述
     */
    public Result(ResultCodeEnum resultCodeEnum, String msg, Object data) {
        this.code = resultCodeEnum.getStatus();
        this.msg = msg;
        if (null == msg || "".equals(msg)) {
            this.msg = resultCodeEnum.getDesc();
        }
        this.data = data;
    }

    /**
     * 异常返回（code=500）
     *
     * @param msg
     * @return
     */
    public static Result error(String msg) {
        return new Result(ResultCodeEnum.ERROR, msg, null);
    }

    /**
     * 成功带数据返回（code = 200）
     * @param data
     * @return
     */
    public static Result success(Object data) {
        return new Result(ResultCodeEnum.SUCCESS, null, data);
    }

    /**
     * 无数据成功返回（code=200）
     *
     * @param msg 结果描述
     * @return
     */
    public static Result success(String msg) {
        return new Result(ResultCodeEnum.SUCCESS, msg, null);
    }

    /**
     * 参数验证失败异常返回（code=999）
     *
     * @param msg 提示信息
     * @return
     */
    public static Result validationError(String msg) {
        return new Result(ResultCodeEnum.VALIDATION_ERROR, msg, null);
    }

    /**
     * 安全校验失败
     *
     * @param msg
     * @return
     */
    public static Result safetyCheck(String msg) {
        if (msg == null) {
            return new Result(ResultCodeEnum.SAFETY_CHECK);
        } else {
            return new Result(ResultCodeEnum.SAFETY_CHECK, msg, null);
        }
    }

    /**
     * 多vo对象返回
     *
     * @param msg
     * @param objs
     * @return
     */
    public static Result successOnVo(String msg, Object... objs) {
        Result result = new Result(ResultCodeEnum.SUCCESS, msg, null);
        if (objs != null) {
            Map<String, Object> map = new HashMap<>();
            for (Object obj : objs) {
                map.put(captureName(obj.getClass().getSimpleName()), obj);
            }
            result.setData(map);
        }
        return result;
    }

    /**
     * 多vo合并为1
     * @param msg
     * @param objs
     * @return
     */
    public static Result seccessMoreVo(String msg,Object ...objs){
        Result result = new Result(ResultCodeEnum.SUCCESS, msg, null);
        if (objs != null) {
            Map map = new HashMap<>();
            for (Object obj : objs) {
                Map m = JSON.parseObject(JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue),Map.class);
                map.putAll(m);
            }
            result.setData(map);
        }
        return result;
    }

    /**
     * 多vo合并为1
     * @param objs
     * @return
     */
    public static Result seccessMoreVo(Object ...objs){
        Result result = new Result(ResultCodeEnum.SUCCESS, null, null);
        if (objs != null) {
            Map map = new HashMap<>();
            for (Object obj : objs) {
                Map m = JSON.parseObject(JSON.toJSONString(obj,SerializerFeature.WriteMapNullValue),Map.class);
                map.putAll(m);
            }
            result.setData(map);
        }
        return result;
    }

    /**
     * 多VO对象返回
     *
     * @param objs
     * @return
     */
    public static Result successOnVo(Object... objs) {
        Result result = new Result(ResultCodeEnum.SUCCESS, null, null);
        if (objs != null) {
            Map<String, Object> map = new HashMap<>();
            for (Object obj : objs) {
                map.put(captureName(obj.getClass().getSimpleName()), obj);
            }
            result.setData(map);
        }
        return result;
    }


    /**
     * 首字母小写
     *
     * @param name
     * @return
     */
    private static String captureName(String name) {
        char[] cs = name.toCharArray();
        cs[0] += 32;
        return String.valueOf(cs);
    }
}
