package com.cloud.mdd.log.webAop;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.cloud.mdd.utils.SpringUtil;
import javassist.*;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

import static ch.qos.logback.classic.Level.*;
import static com.cloud.mdd.log.model.LogConstant.IMPORTANTLOG_TOPIC;

/**
 * @author zengliming
 * @date 2018/3/14 13:47
 */
@Aspect
@Component
@Order(50) //日志级别加载顺序调整
@Slf4j(topic = IMPORTANTLOG_TOPIC)
public class LogAspect {

    private ImportantLogService importantLogService;

    /**
     * 重要日志切入点
     *
     * @param pjp
     * @param importantLog
     * @return
     * @throws Throwable
     */
    @Around("@annotation(importantLog)")
    public Object RequestLog(ProceedingJoinPoint pjp, ImportantLog importantLog) throws Throwable {
        Object o = pjp.proceed();
        int level = importantLog.level();
        String title = importantLog.title();
        String type = importantLog.type();
        boolean isLogParam = importantLog.isLogParam();
        boolean isLogResult = importantLog.isLogResult();

        /**
         * Signature 包含了方法名、申明类型以及地址等信息
         */
        String class_name = pjp.getTarget().getClass().getName();
        String method_name = pjp.getSignature().getName();
        String[] paramNames = getFieldsName(class_name, method_name);

        Map<String, Object> map = new HashMap<>();
        map.put("title", title);
        map.put("tableName", importantLog.tableName());
        map.put("type", type);
        map.put("declaringTypeName", pjp.getSignature().getDeclaringTypeName());
        map.put("signature", pjp.getSignature().getName());
        if (isLogParam) {
            Object[] obj = pjp.getArgs();
            Map<String, Object> args = new HashMap<>();
            if (obj != null) {
                String exclude[] = importantLog.exclude();
                if (exclude == null || exclude.length == 0) {
                    args.put("args", obj);
                } else {
                    for (int i = 0; i < obj.length; i++) {
                        String pname = paramNames[i];//参数名
                        boolean pf = true;
                        for (String s : exclude) {
                            if (s.equals(pname)) {
                                pf = false;
                                break;
                            }
                        }
                        if (pf) {
                            Object arg = obj[i]; //参数值
                            if (arg != null) {
                                boolean falg = true;
                                for (Class noJsonClass : LogAopContant.NO_JSON_CLASS) {
                                    if (noJsonClass.isAssignableFrom(arg.getClass())) {
                                        falg = false;
                                        break;
                                    }
                                }
                                if (falg) {
                                    args.put(pname, arg);
                                }
                            } else {
                                args.put(pname, arg);
                            }
                        }
                    }
                }
            }
            map.put("param", args);
        }
        if (isLogResult) {
            map.put("result", JSON.toJSONString(o));
        }
        //判断扩展类
        if (importantLogService == null) {
            try {
                importantLogService = SpringUtil.getBean(ImportantLogService.class);
            } catch (Exception e) {
            }
        }
        if (importantLogService != null) {
            Map<String, String> extendMap = importantLogService.importantLogExtendMap(importantLog);
            if (extendMap != null) {
                map.put("extendMap", extendMap);
            }
        }
        String str = "";
        try {
            str = JSON.toJSONString(map, SerializerFeature.WriteMapNullValue);
        } catch (Exception e) {
            //防止遇到不能json转换的数据
            str = map.toString();
        }
        switch (level) {
            //info 级别日志
            case INFO_INT:
                log.info("{}", str);
                break;
            //警告级别日志
            case WARN_INT:
                log.warn("{}", str);
                break;
            //错误级别日志
            case ERROR_INT:
                log.error("{}", str);
                break;
            default:
                log.debug("{}", str);
        }
        return o;
    }

    /**
     * 使用javassist来获取方法参数名称
     *
     * @param class_name  类名
     * @param method_name 方法名
     * @return
     * @throws Exception
     */
    private String[] getFieldsName(String class_name, String method_name) throws Exception {
        Class<?> clazz = Class.forName(class_name);
        String clazz_name = clazz.getName();
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(clazz);
        pool.insertClassPath(classPath);

        CtClass ctClass = pool.get(clazz_name);
        CtMethod ctMethod = ctClass.getDeclaredMethod(method_name);
        MethodInfo methodInfo = ctMethod.getMethodInfo();
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);
        if (attr == null) {
            return null;
        }
        String[] paramsArgsName = new String[ctMethod.getParameterTypes().length];
        int pos = Modifier.isStatic(ctMethod.getModifiers()) ? 0 : 1;
        for (int i = 0; i < paramsArgsName.length; i++) {
            paramsArgsName[i] = attr.variableName(i + pos);
        }
        return paramsArgsName;
    }

}
