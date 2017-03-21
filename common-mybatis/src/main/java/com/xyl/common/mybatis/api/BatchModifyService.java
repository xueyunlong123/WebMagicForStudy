package com.xyl.common.mybatis.api;

import com.alpha.common.exception.BasicErrorCodeEnum;
import com.alpha.common.exception.BusinessException;
import com.alpha.common.utils.CollectionUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by chenwen on 17/2/14.
 */
public abstract class BatchModifyService<A,B,C> {
    /**
     * 批量修改
     * @param id 需要修改的ID
     * @param set 需要修改的列表
     */
    public void modify(A id, Set<B> set, boolean needValidate){
        /**
         * step 1: 查找现在覆盖区域
         */
        if (CollectionUtils.isEmpty(set)){
            return;
        }

        Set<B> currSets = getCurrList(id).stream().collect(Collectors.toSet());


        /**
         * step 3: 找出需要删除的
         */

        Set<B> deleteSets = findDeleteSet(currSets,set);


        /**
         * step 4: 找出需要添加的
         */
        Set<B> insertSets = findInsertSet(currSets,set);

        if (needValidate && deleteSets.size() == 0 && insertSets.size() == 0) {
            throw new BusinessException(BasicErrorCodeEnum.NOT_UPDATE_ERROR);
        }

        /**
         * step 5: 如果不需要添加和删除,则抛出异常
         */
        if (deleteSets.size() > 0 || insertSets.size() > 0) {

            /**
             * step 6: 批量添加
             */
            if (insertSets.size() > 0) {
                insertList(getList(id, insertSets));
            }

            /**
             * step 7: 批量删除
             */
            if (deleteSets.size() > 0) {
                deleteList(id, deleteSets);
            }
        }
    }

    /**
     * 需要插入的元素
     * @param currSets 当前元素
     * @param set 新的元素
     * @return
     */
    public Set<B> findInsertSet(Set<B> currSets, Set<B> set){
        Set<B> result = set.stream().collect(Collectors.toSet());
        result.removeAll(currSets);
        return result;
    }

    /**
     * 需要删除的元素
     * @param currSets 当前元素
     * @param set 新的元素
     * @return
     */
    public Set<B> findDeleteSet(Set<B> currSets, Set<B> set){
        return findInsertSet(set, currSets);
    }

    /**
     * 获取当前列表
     * @param id ID
     * @return
     */
    public abstract List<B> getCurrList(A id);

    /**
     * 批量插入列表
     * @param list 列表
     */
    public abstract void insertList(List<C> list);

    /**
     * 获取 存储对象列表
     * @param id ID
     * @param list 列表
     * @return
     */
    public abstract List<C> getList(A id, Set<B> list);

    /**
     * 批量删除
     * @param id 删除对象
     * @param list 删除列表
     */
    public abstract void deleteList(A id, Set<B> list);
}
