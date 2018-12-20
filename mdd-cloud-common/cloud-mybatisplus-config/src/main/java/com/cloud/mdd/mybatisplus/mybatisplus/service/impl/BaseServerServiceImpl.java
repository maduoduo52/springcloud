package com.cloud.mdd.mybatisplus.mybatisplus.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.cloud.mdd.Constant;
import com.cloud.mdd.mybatisplus.dao.DeleteMapper;
import com.cloud.mdd.mybatisplus.model.BaseEntity;
import com.cloud.mdd.mybatisplus.mybatisplus.service.BaseServerIService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Desc: M 持久层对象
 * T entity对象
 * @Author Maduo
 * @Create 2018/12/1 16:15
 */
@Slf4j
public abstract class BaseServerServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements BaseServerIService<T> {

    @Autowired
    private DeleteMapper deleteMapper;

    /**
     * 静态区Map存储所有的bean对象class路径
     */
    public static Map<String, String> ENTITY_MAP = new ConcurrentHashMap<>();
    /**
     * 静态区map存储bean对象对于的ID属性名称
     */
    public static Map<String, String> ENTITY_ID = new ConcurrentHashMap<>();

    /**
     * 静态区map存储bean对象对于的ID属性字段字段名称
     */
    public static Map<String, String> ENTITY_ID_CL = new ConcurrentHashMap<>();

    /**
     * 静态区map存储bean对象和baen对象对应的表名
     */
    public static Map<String, String> ENTITY_TABLE = new ConcurrentHashMap<>();

    /**
     * 是否允许删除  此删除是把删除替换为update语句
     */
    public static Map<String, Boolean> IS_DELETE = new ConcurrentHashMap<>();

    /**
     * entity 类型  是1：基础BaseEntity 还是:2：BaseManageEntity
     */
    public static Map<String, Integer> ENTITY_TYPE = new ConcurrentHashMap<>();

    /**
     * 构造方法获取数据
     */
    public BaseServerServiceImpl() {
        try {
            Type type = getClass().getGenericSuperclass();
            if (type instanceof ParameterizedType) {
                ParameterizedType pType = (ParameterizedType) type;
                //直接获取第二个泛型 第二个泛型为实体bean对象
                Type claz = pType.getActualTypeArguments()[1];
                //放进map中
                ENTITY_MAP.put(this.getClass().getName(), claz.getTypeName());
                //获取class对象
                Class clas = Class.forName(claz.getTypeName());

                //开始获取类的注解
                Annotation allAnnos = clas.getDeclaredAnnotation(TableName.class);
                if (allAnnos != null) {
                    TableName tableName = (TableName) allAnnos;
                    String tname = tableName.value();
                    if (!StringUtils.isEmpty(tname)) {
                        ENTITY_TABLE.put(this.getClass().getName(), tname);
                    }
                }
                //没有设置表名时
                if (ENTITY_TABLE.get(this.getClass().getName()) == null) {
                    String tname = clas.getSimpleName();
                    Set<String> set = new HashSet<>();
                    char[] chars = tname.toCharArray();
                    for (int i = 1; i < chars.length; i++) {
                        char aChar = chars[i];
                        if (Character.isUpperCase(aChar)) {
                            set.add(String.valueOf(aChar));
                        }
                    }
                    if (set.size() > 0) {
                        for (String s : set) {
                            tname = tname.replaceAll(s, "_" + s.toLowerCase());
                        }
                    }
                    ENTITY_TABLE.put(this.getClass().getName(), tname);
                }
                //判断是不是BaseEntity
                if (BaseEntity.class.isAssignableFrom(clas)) {
                    ENTITY_TYPE.put(this.getClass().getName(), 1);
                    IS_DELETE.put(this.getClass().getName(), true);
                } else {
                    IS_DELETE.put(this.getClass().getName(), false);
                }
                //获取所有的字段
                Field[] fields = clas.getDeclaredFields();
                for (Field field : fields) {
                    //判断字段是不是ID
                    Annotation an = field.getAnnotation(TableId.class);
                    if (an != null) {
                        TableId tableId = (TableId) an;
                        if (tableId.value() != null && !"".equals(tableId.value().trim())) {
                            ENTITY_ID_CL.put(claz.getTypeName(), tableId.value());
                        } else {
                            String idKey = field.getName();
                            char[] chars = idKey.toCharArray();
                            Set<String> set = new HashSet<>();
                            for (int i = 1; i < chars.length; i++) {
                                char aChar = chars[i];
                                if (Character.isUpperCase(aChar)) {
                                    set.add(String.valueOf(aChar));
                                }
                            }
                            if (set.size() > 0) {
                                for (String s : set) {
                                    idKey = idKey.replaceAll(s, "_" + s.toLowerCase());
                                }
                            }
                            ENTITY_ID_CL.put(claz.getTypeName(), idKey);
                        }
                        //放入map中
                        ENTITY_ID.put(claz.getTypeName(), field.getName());
                    }
                }
            }
        } catch (Exception e) {
            ENTITY_MAP.remove(this.getClass().getName());
            IS_DELETE.put(this.getClass().getName(), false);
            log.error("获取泛型类型失败:", e);
        }
    }

    /**
     * 转entity
     *
     * @param map
     * @return
     * @throws ClassNotFoundException
     */
    private T getEntity(Map map) throws ClassNotFoundException {
        String entityUrl = ENTITY_MAP.get(this.getClass().getName());
        Class cla = Class.forName(entityUrl);
        if (1 == ENTITY_TYPE.get(this.getClass().getName())) {
            T t = JSON.parseObject(JSON.toJSONString(map), (Type) cla);
            BaseEntity baseEntity = (BaseEntity) t;
            baseEntity.setIsDelete(1);
            return t;
        }
        return JSON.parseObject(JSON.toJSONString(map), (Type) cla);
    }


    @Override
    public boolean deleteById(Serializable id) {
        if (StringUtils.isEmpty(id)) {
            throw new RuntimeException("id不能为空！");
        }
        try {
            Boolean boo = IS_DELETE.get(this.getClass().getName());
            if (boo != null && boo) {
                String entityUrl = ENTITY_MAP.get(this.getClass().getName());
                if (!StringUtils.isEmpty(entityUrl)) {
                    String entityId = ENTITY_ID.get(entityUrl);
                    Map map = new HashMap();
                    map.put(entityId, id);
                    T t = getEntity(map);
                    return super.updateById(t);
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return super.deleteById(id);
    }

    @Override
    public boolean deleteByMap(Map<String, Object> columnMap) {
        if (columnMap == null || columnMap.isEmpty()) {
            throw new RuntimeException("columnMap不能为空！如果需要全部删除请自行写sql");
        }
        try {
            Boolean boo = IS_DELETE.get(this.getClass().getName());
            if (boo != null && boo) {
                String entityUrl = ENTITY_MAP.get(this.getClass().getName());
                if (!StringUtils.isEmpty(entityUrl)) {
                    int i = 0;
                    if (1 == ENTITY_TYPE.get(this.getClass().getName())) {
                        i = deleteMapper.deleteByMap(ENTITY_TABLE.get(this.getClass().getName()), null, columnMap);
                    } else if (2 == ENTITY_TYPE.get(this.getClass().getName())) {
                        i = deleteMapper.deleteByMap(ENTITY_TABLE.get(this.getClass().getName()), null, columnMap);
                    }
                    return i > 0 ? true : false;
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return super.deleteByMap(columnMap);
    }

    @Override
    public boolean delete(Wrapper<T> wrapper) {
        if (wrapper == null || StringUtils.isEmpty(wrapper.getSqlSegment())) {
            throw new RuntimeException("wrapper不能为空！如果需要全部删除请自行写sql");
        }
        try {
            Boolean boo = IS_DELETE.get(this.getClass().getName());
            if (boo != null && boo) {
                String entityUrl = ENTITY_MAP.get(this.getClass().getName());
                if (!StringUtils.isEmpty(entityUrl)) {
                    int i = 0;
                    String sql = "update " + ENTITY_TABLE.get(this.getClass().getName()) + " set is_delete = 1,update_time = now()";
                    if (1 == ENTITY_TYPE.get(this.getClass().getName())) {
                    } else if (2 == ENTITY_TYPE.get(this.getClass().getName())) {
                        sql += " , update_uid =   #{gw.operationUid}";
                        sql += " , update_uname = #{gw.operationUname} ";
                    }
                    sql += wrapper.getSqlSegment();
                    sql = sql.replaceFirst("AND", "  where").replaceAll("ew.paramNameValuePairs", "gw.paramNameValuePairs");
                    Map map = new HashMap<>();
                    map.put("sql", sql);
                    map.put("paramNameValuePairs", wrapper.getParamNameValuePairs());
                    i = deleteMapper.delete(map);
                    return i > 0 ? true : false;
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return super.delete(wrapper);
    }

    @Override
    public boolean deleteBatchIds(List<? extends Serializable> idList) {
        if (idList == null || idList.isEmpty()) {
            throw new RuntimeException("idList不能为空！");
        }
        try {
            Boolean boo = IS_DELETE.get(this.getClass().getName());
            if (boo != null && boo) {
                String entityUrl = ENTITY_MAP.get(this.getClass().getName());
                if (!StringUtils.isEmpty(entityUrl)) {
                    String entityId = ENTITY_ID_CL.get(entityUrl);
                    int i = 0;
                    if (1 == ENTITY_TYPE.get(this.getClass().getName())) {
                        i = deleteMapper.deleteBatchIds(ENTITY_TABLE.get(this.getClass().getName()), null,
                                entityId, idList);
                    } else if (2 == ENTITY_TYPE.get(this.getClass().getName())) {
                        i = deleteMapper.deleteBatchIds(ENTITY_TABLE.get(this.getClass().getName()), null,
                                entityId, idList);
                    }
                    return i > 0 ? true : false;
                }
            }
        } catch (Exception e) {
            log.error("", e);
        }
        return super.deleteBatchIds(idList);
    }


    @Override
    public boolean physicsDeleteById(Serializable id) {
        if (StringUtils.isEmpty(id)) {
            throw new RuntimeException("id不能为空！");
        }
        Boolean boo = IS_DELETE.get(this.getClass().getName());
        if (boo != null && boo) {
            String entityUrl = ENTITY_MAP.get(this.getClass().getName());
            if (!StringUtils.isEmpty(entityUrl)) {
                String entityId = ENTITY_ID_CL.get(entityUrl);
                String sql = "delete from `" + ENTITY_TABLE.get(this.getClass().getName()) + "` where `" + entityId + "` = #{gw.idValue} ";
                Map map = new HashMap();
                map.put("sql", sql);
                map.put("idValue", id);
                int i = deleteMapper.physicsDelete(map);
                log.warn("根据ID进行物理删除 {} ", map);
                return i > 0 ? true : false;
            }
        }
        //当前面条件都不满足 则证明此表中不存在逻辑删除字段 直接调用父类的删除方法
        return super.deleteById(id);
    }

    @Override
    public boolean physicsDeleteByMap(Map<String, Object> columnMap) {
        if (columnMap == null || columnMap.isEmpty()) {
            throw new RuntimeException("columnMap不能为空！如果需要全部删除请自行写sql");
        }
        Boolean boo = IS_DELETE.get(this.getClass().getName());
        if (boo != null && boo) {
            int i = deleteMapper.physicsDeleteByMap(ENTITY_TABLE.get(this.getClass().getName()), columnMap);
            log.warn("根据map进行物理删除 {} ", columnMap);
            return i > 0 ? true : false;
        }
        //当前面条件都不满足 则证明此表中不存在逻辑删除字段 直接调用父类的删除方法
        return super.deleteByMap(columnMap);
    }

    @Override
    public boolean physicsDelete(Wrapper<T> wrapper) {
        if (wrapper == null || StringUtils.isEmpty(wrapper.getSqlSegment())) {
            throw new RuntimeException("wrapper不能为空！如果需要全部删除请自行写sql");
        }
        Boolean boo = IS_DELETE.get(this.getClass().getName());
        if (boo != null && boo) {
            String sql = "delete from " + ENTITY_TABLE.get(this.getClass().getName()) + " ";
            sql += wrapper.getSqlSegment();
            sql = sql.replaceFirst("AND", "  where").replaceAll("ew.paramNameValuePairs", "gw.paramNameValuePairs");
            Map map = new HashMap<>();
            map.put("sql", sql);
            map.put("paramNameValuePairs", wrapper.getParamNameValuePairs());
            int i = deleteMapper.physicsDelete(map);
            return i > 0 ? true : false;
        }
        return super.delete(wrapper);
    }

    @Override
    public boolean physicsDeleteBatchIds(List<? extends Serializable> idList) {
        if (idList == null || idList.isEmpty()) {
            throw new RuntimeException("idList不能为空！");
        }
        Boolean boo = IS_DELETE.get(this.getClass().getName());
        if (boo != null && boo) {
            String entityUrl = ENTITY_MAP.get(this.getClass().getName());
            if (!StringUtils.isEmpty(entityUrl)) {
                String entityId = ENTITY_ID_CL.get(entityUrl);
                int i = deleteMapper.physicsDeleteBatchIds(ENTITY_TABLE.get(this.getClass().getName()), entityId, idList);
                log.warn("根据IDS进行物理删除 {} ", idList);
                return i > 0 ? true : false;
            }
        }
        //当前面条件都不满足 则证明此表中不存在逻辑删除字段 直接调用父类的删除方法
        return super.deleteBatchIds(idList);
    }


    @Override
    public Map<Serializable, T> selectByWrapper(Wrapper<T> wrapper) {
        Boolean boo = IS_DELETE.get(this.getClass().getName());
        if (boo != null && boo) {
            String entityUrl = ENTITY_MAP.get(this.getClass().getName());
            if (!StringUtils.isEmpty(entityUrl)) {
                String entityId = ENTITY_ID.get(entityUrl);
                List<T> list = selectList(wrapper);
                if (list != null) {
                    if (list.isEmpty()) {
                        return new HashMap<>();
                    }
                    Map<Serializable, T> map = new HashMap<>();
                    for (T t : list) {
                        Map<String, Object> jsonMap = JSON.parseObject(JSON.toJSONString(t));
                        map.put((Serializable) jsonMap.get(entityId), t);
                    }
                    return map;
                }
            }
        }
        throw new RuntimeException("该实体类不支持此方法");
    }

    @Override
    public Map<Serializable, T> selectByIdList(List<Serializable> idList) {
        Boolean boo = IS_DELETE.get(this.getClass().getName());
        if (boo != null && boo) {
            String entityUrl = ENTITY_MAP.get(this.getClass().getName());
            if (!StringUtils.isEmpty(entityUrl)) {
                String entityId = ENTITY_ID.get(entityUrl);
                List<T> list = selectBatchIds(idList);
                if (list != null) {
                    if (list.isEmpty()) {
                        return new HashMap<>();
                    }
                    Map<Serializable, T> map = new HashMap<>();
                    for (T t : list) {
                        Map<String, Object> jsonMap = JSON.parseObject(JSON.toJSONString(t));
                        map.put((Serializable) jsonMap.get(entityId), t);
                    }
                    return map;
                }
            }
        }
        throw new RuntimeException("该实体类不支持此方法");
    }
}
