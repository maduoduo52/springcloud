package com.cloud.mdd.annotations;


import com.cloud.mdd.enums.MVCEnum;

import java.lang.annotation.*;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/1 15:57
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface DisableMVC {

    /**
     * 那些地址禁止访问
     * @return
     */
    MVCEnum[] address() default {MVCEnum.deleteBatchIds};
}
