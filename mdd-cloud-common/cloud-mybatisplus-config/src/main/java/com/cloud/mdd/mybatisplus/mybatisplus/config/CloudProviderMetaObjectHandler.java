package com.cloud.mdd.mybatisplus.mybatisplus.config;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/8 14:03
 */
@Slf4j
public class CloudProviderMetaObjectHandler extends MetaObjectHandler {

    /**
     * 插入时的公共字段
     *
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            Object addTime = getFieldValByName("addTime", metaObject);
            if (addTime == null) {
                setFieldValByName("addTime", new Date(), metaObject);
            }
            Object version = getFieldValByName("version", metaObject);
            if (version == null) {
                setFieldValByName("version", 0, metaObject);
            }
            Object deleteFlag = getFieldValByName("deleteFlag", metaObject);
            if (deleteFlag == null) {
                setFieldValByName("deleteFlag", 0, metaObject);
            }
        }catch (Exception e) {
            log.error("新增公共字段注入异常：{}", e);
        }
    }

    /**
     * 修改公共字段
     *
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            Object updateTime = getFieldValByName("updateTime", metaObject);
            if (updateTime == null) {
                setFieldValByName("updateTime", new Date(), metaObject);
            }
        }catch (Exception e) {
            log.error("修改公共字段注入异常：{}", e);
        }
    }
}
