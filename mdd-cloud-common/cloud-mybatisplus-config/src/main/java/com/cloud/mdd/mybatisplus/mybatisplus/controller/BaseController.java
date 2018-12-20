package com.cloud.mdd.mybatisplus.mybatisplus.controller;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.IService;
import com.cloud.mdd.annotations.DisableMVC;
import com.cloud.mdd.enums.MVCEnum;
import com.cloud.mdd.mybatisplus.mybatisplus.exception.CloudException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @Desc:
 * @Author Maduo
 * @Create 2018/12/9 15:41
 */
@Validated
public abstract class BaseController<M extends IService, T> {

    @Autowired
    private M iService;

    private Set<MVCEnum> disableMVCSet = null;

    /**
     * 无参构造器
     */
    public BaseController() {
        // 获取 DisableMVC注解
        DisableMVC disableMVC = this.getClass().getAnnotation(DisableMVC.class);
        if (disableMVC != null) {
            //获取禁用的地址信息
            MVCEnum[] address = disableMVC.address();
            if (address != null && address.length > 0) {
                disableMVCSet = new HashSet<>();
                for (MVCEnum mvcEnum : address) {
                    //添加进 disableMVCSet
                    disableMVCSet.add(mvcEnum);
                }
            }
        }
    }

    /**
     * 添加
     * @param t
     * @return
     */
    @PostMapping("insert")
    public boolean insert(@RequestBody T t){
        if(disableMVCSet!=null && disableMVCSet.contains(MVCEnum.insert)){
            throw  new CloudException("该接口禁止访问");
        }
        return iService.insert(t);
    }

    /**
     * 批量添加
     * @param list
     * @return
     */
    @PostMapping("insertBatch")
    public boolean insertBatch(@RequestBody List<T> list){
        if(disableMVCSet!=null &&disableMVCSet.contains(MVCEnum.insertBatch)){
            throw  new CloudException("该接口禁止访问");
        }
        return iService.insertBatch(list);
    }

    /**
     * 修改
     * @param t
     * @return
     */
    @PutMapping("update")
    public boolean updateById(@RequestBody T t){
        if(disableMVCSet!=null &&disableMVCSet.contains(MVCEnum.updateById)){
            throw  new CloudException("该接口禁止访问");
        }
        return  iService.updateById(t);
    }

    /**
     * 批量修改
     * @param list
     * @return
     */
    @PutMapping("updateBatchById")
    public boolean updateBatchById(@RequestBody List<T> list){
        if(disableMVCSet!=null && disableMVCSet.contains(MVCEnum.updateBatchById)){
            throw  new CloudException("该接口禁止访问");
        }
        return iService.updateBatchById(list);
    }

    /**
     * 单个删除
     * @param id
     * @return
     */
    @DeleteMapping("{id}/delete")
    public boolean delete(@NotNull @PathVariable("id") String id){
        if(disableMVCSet!=null && disableMVCSet.contains(MVCEnum.delete)){
            throw  new CloudException("该接口禁止访问");
        }
        return  iService.deleteById(id);
    }

    /**
     * 批量删除
     * @param ids
     * @return
     */
    @PostMapping("deleteBatchIds")
    public boolean deleteBatchIds(@RequestBody List<String> ids){
        if(disableMVCSet!=null && disableMVCSet.contains(MVCEnum.deleteBatchIds)){
            throw  new CloudException("该接口禁止访问");
        }
        return iService.deleteBatchIds(ids);
    }

    /**
     * 查询
     * @param id
     * @return
     */
    @GetMapping("selectById")
    public Object selectById(@NotNull String id){
        if(disableMVCSet!=null && disableMVCSet.contains(MVCEnum.selectById)){
            throw  new CloudException("该接口禁止访问");
        }
        return iService.selectById(id);
    }

    /**
     * 查询全部 以list形式返回
     * @return
     */
    @GetMapping(value = "selectAll")
    public List selectAll(){
        if(disableMVCSet!=null && disableMVCSet.contains(MVCEnum.selectAll)){
            throw  new CloudException("该接口禁止访问");
        }
        return iService.selectList(new EntityWrapper());
    }

    /**
     * 查询全部 以map形式返回
     * @return
     */
    @GetMapping("selectAllMap")
    public Map selectAllMap(){
        if(disableMVCSet!=null && disableMVCSet.contains(MVCEnum.selectAllMap)){
            throw  new CloudException("该接口禁止访问");
        }
        return iService.selectMap(new EntityWrapper());
    }

    /**
     * 统计数量
     * @return
     */
    @GetMapping("selectCount")
    public int selectCount(){
        if(disableMVCSet!=null && disableMVCSet.contains(MVCEnum.selectCount)){
            throw  new CloudException("该接口禁止访问");
        }
        return iService.selectCount(null);
    }

    /**
     * 根据ID批量查询
     * @param ids
     * @return
     */
    @PostMapping("selectBatchIds")
    public List<T> selectBatchIds(@RequestBody List<String> ids){
        if(disableMVCSet!=null && disableMVCSet.contains(MVCEnum.selectBatchIds)){
            throw  new CloudException("该接口禁止访问");
        }
        return iService.selectBatchIds(ids);
    }

    /**
     * 根据 对象查询  null字段不会查询
     * @param t
     * @return
     */
    @PostMapping("selectByDtoNotNull")
    public Object selectByDtoNotNull(@RequestBody T t){
        if(disableMVCSet!=null && disableMVCSet.contains(MVCEnum.selectByDtoNotNull)){
            throw  new CloudException("该接口禁止访问");
        }
        EntityWrapper<T> ew = new EntityWrapper();
        ew.setEntity(t);
        return iService.selectOne(ew);
    }

    /**
     * 根据 对象查询  null字段不会查询
     * @param t
     * @return
     */
    @PostMapping("selectListByDtoNotNull")
    public List<T> selectListByDtoNotNull(@RequestBody T t){
        if(disableMVCSet!=null && disableMVCSet.contains(MVCEnum.selectListByDtoNotNull)){
            throw  new CloudException("该接口禁止访问");
        }
        EntityWrapper<T> ew = new EntityWrapper();
        ew.setEntity(t);
        return iService.selectList(ew);
    }



    /**
     * get方法
     * @return
     */
    public Set<MVCEnum> getDisableMVCSet() {
        return disableMVCSet;
    }

    /**
     * set方法 空实现 不允许修改
     * @param disableMVCSet
     */
    private void setDisableMVCSet(Set<MVCEnum> disableMVCSet) {
    }
}
