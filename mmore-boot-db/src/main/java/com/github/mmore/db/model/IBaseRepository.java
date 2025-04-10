package com.github.mmore.db.model;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.service.IService;
import com.github.mmore.common.model.PageResponse;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

/**
 * @Author Bond
 * @Date 2025/1/13
 * @Description TODO
 */
public interface IBaseRepository<T> extends IService<T> {

    PageResponse<T> getPage(long pageNum, long pageSize, LambdaQueryWrapper<T> queryWrapper);

    List<T> getPageList(long pageNum, long pageSize, LambdaQueryWrapper<T> queryWrapper);

    boolean existById(Serializable id);

    boolean notExistById(Serializable id);

    <V> boolean existByColumn(SFunction<T, V> column, V value);

    <V> boolean notExistByColumn(SFunction<T, V> column, V value);

    <V> List<?> listColumnValue(SFunction<T, V> column);

    <V> List<?> listUniqueColumnValue(SFunction<T, V> column);

    <V> List<?> listColumnValue(SFunction<T, V> column, boolean unique, boolean nonNull);

    <V> List<T> listByColumnValues(SFunction<T, V> column, Collection<V> values);

    List<T> listByIds(Collection<? extends Serializable> ids);

    List<T> listByIds(Collection<? extends Serializable> ids, int batchSize);

    <V> T getByColumnValue(SFunction<T, V> column, V value);

    <V> List<T> listByColumnValues(SFunction<T, V> column, Collection<V> values, int batchSize);

    boolean removeByColumnValue(SFunction<T, ?> column, Object value);

    <V> boolean removeBatchByColumns(SFunction<T, V> column, Collection<V> values, int batchSize);


}
