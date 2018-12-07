package com.cloud.mdd.feign.api.hystric;

import com.cloud.mdd.feign.api.BaseApiService;
import com.cloud.mdd.feign.exception.AdminException;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/7 23:50
 */
@Slf4j
public abstract class BaseApiHystric<M, T> implements BaseApiService<M, T> {

    public abstract String getLogName();

    @Override
    public boolean insert(M t) {
        log.info("{}:添加进入熔断:,{}", getLogName(), t);
        throw new AdminException(getLogName() + "添加进入熔断");
    }

    @Override
    public boolean insertBatch(List<M> list) {
        log.info("{}:批量添加进入熔断:{}", getLogName(), list);
        throw new AdminException(getLogName() + "批量添加进入熔断");
    }

    @Override
    public boolean updateById(M t) {
        log.info("{}:修改进入熔断:{}", getLogName(), t);
        throw new AdminException(getLogName() + "修改进入熔断");
    }

    @Override
    public boolean updateBatchById(List<M> kdPostList) {
        log.info("{}:批量修改进入熔断:{}", getLogName(), kdPostList);
        throw new AdminException(getLogName() + "批量修改进入熔断");
    }

    @Override
    public boolean delete(String id) {
        log.info("{}:删除进入熔断:{}", getLogName(), id);
        throw new AdminException(getLogName() + "logName");
    }

    @Override
    public boolean deleteBatchIds(List<String> ids) {
        log.info("{}:批量删除进入熔断:{}", getLogName(), ids);
        throw new AdminException(getLogName() + "批量删除进入熔断");
    }

    @Override
    public T selectById(String id) {
        log.info("{}:根据id查询进入熔断:{}", getLogName(), id);
        throw new AdminException(getLogName() + "根据id查询进入熔断");
    }

    @Override
    public List<T> selectAll() {
        log.info("{}:查询全部list进入熔断", getLogName());
        throw new AdminException(getLogName() + "查询全部list进入熔断");
    }

    @Override
    public Map<String, T> selectAllMap() {
        log.info("{}:查询全部map进入熔断", getLogName());
        throw new AdminException(getLogName() + "查询全部map进入熔断");
    }

    @Override
    public int selectCount() {
        log.info("{}:统计数量进入熔断", getLogName());
        throw new AdminException(getLogName() + "统计数量进入熔断");
    }

    @Override
    public List<T> selectBatchIds(List<String> ids) {
        log.info("{}:批量查询进入熔断", getLogName());
        throw new AdminException(getLogName() + "批量查询进入熔断");
    }

    @Override
    public T selectByDtoNotNull(M m) {
        log.info("{}:根据 对象查询  null字段不会查询：{} ", getLogName(), m);
        throw new AdminException(getLogName() + "根据 对象查询  null字段不会查询");
    }

    @Override
    public List<T> selectListByDtoNotNull(M m) {
        log.info("{}:根据 对象查询  null字段不会查询：{} ", getLogName(), m);
        throw new AdminException(getLogName() + "根据 对象查询  null字段不会查询");
    }
}
