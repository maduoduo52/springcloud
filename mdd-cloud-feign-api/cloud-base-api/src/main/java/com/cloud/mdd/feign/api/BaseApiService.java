package com.cloud.mdd.feign.api;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @param <M> Dto
 * @param <T> Vo
 */
public interface BaseApiService<M, T> {

    /**
     * 添加
     *
     * @param t
     * @return
     */
    @PostMapping("insert")
    boolean insert(@RequestBody M t);

    /**
     * 批量添加
     *
     * @param list
     * @return
     */
    @PostMapping("insertBatch")
    boolean insertBatch(@RequestBody List<M> list);

    /**
     * 修改
     *
     * @param t
     * @return
     */
    @PutMapping("update")
    boolean updateById(M t);

    /**
     * 批量修改
     *
     * @param list
     * @return
     */
    @PutMapping("updateBatchById")
    boolean updateBatchById(@RequestBody List<M> list);

    /**
     * 单个删除
     *
     * @param id
     * @return
     */
    @DeleteMapping("{id}/delete")
    boolean delete(@PathVariable("id") String id);

    /**
     * 批量删除 TODO 此方法未测试
     *
     * @param ids
     * @return
     */
    @PostMapping("deleteBatchIds")
    boolean deleteBatchIds(@RequestBody List<String> ids);

    /**
     * 查询
     *
     * @param id
     * @return
     */
    @GetMapping("selectById")
    T selectById(@RequestParam("id") String id);

    /**
     * 查询全部 以list形式返回
     *
     * @return
     */
    @GetMapping(value = "selectAll")
    List<T> selectAll();

    /**
     * 查询全部 以map形式返回
     *
     * @return
     */
    @GetMapping("selectAllMap")
    Map<String, T> selectAllMap();

    /**
     * 统计数量
     *
     * @return
     */
    @GetMapping("selectCount")
    int selectCount();

    /**
     * 根据id集合批量查询
     *
     * @param ids
     * @return
     */
    @PostMapping("selectBatchIds")
    List<T> selectBatchIds(@RequestBody List<String> ids);

    /**
     * 根据 对象查询  null字段不会查询
     *
     * @param m
     * @return
     */
    @PostMapping("selectByDtoNotNull")
    T selectByDtoNotNull(@RequestBody M m);


    /**
     * 根据 对象查询  null字段不会查询
     *
     * @param m
     * @return
     */
    @PostMapping("selectListByDtoNotNull")
    List<T> selectListByDtoNotNull(@RequestBody M m);

}
