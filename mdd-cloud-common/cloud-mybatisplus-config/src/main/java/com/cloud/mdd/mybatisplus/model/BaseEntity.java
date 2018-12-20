package com.cloud.mdd.mybatisplus.model;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.Version;
import com.baomidou.mybatisplus.enums.FieldFill;
import lombok.Data;

import java.util.Date;

/**
* 数据库基础字段实体（数据库实体需要继承自此类）
* @author Maduo
* @Description
* @create 2018-05-15
* @version 1.0
**/
@Data
public class BaseEntity {
    /**
     * 添加时间
     */
    @TableField(value = "add_time",fill = FieldFill.INSERT)
    private Date addTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time",fill = FieldFill.UPDATE)
    private Date updateTime;

    /**
     * 版本号
     */
    @Version
    @JSONField(serialize = false)
    @TableField(value = "version",fill = FieldFill.INSERT)
    private Integer version;

    /**
     * 备注
     */
    @JSONField(serialize = false)
    private String baRemark;

    /**
     * 是否删除(0：未删除，1：已删除)
     */
    @TableLogic
    @JSONField(serialize = false)
    @TableField(value = "is_delete",fill = FieldFill.INSERT)
    private Integer isDelete;

    /**
     * 公共字段致空
     */
    public void setNull(){
        this.addTime=null;
        this.updateTime=null;
        this.version=null;
        this.baRemark=null;
        this.isDelete=null;
    }

    /**
     * 修改时公共字段设置为null
     */
    public void setUpdate(){
        this.updateTime=null;
    }



}
