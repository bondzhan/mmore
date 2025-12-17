package com.github.mmore.db.model;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.mmore.common.model.PageResponse;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class BaseRepositoryImpl<M extends BaseMapper<T>, T> extends ServiceImpl<M, T> implements IBaseRepository<T> {
    protected static final int DEFAULT_LIST_BATCH_SIZE = 500;

    // 分页查询（返回分页对象）
    @Override
    public PageResponse<T> getPage(long pageNum, long pageSize, LambdaQueryWrapper<T> queryWrapper) {
        // 创建分页对象
        Page<T> page = new Page<>(pageNum, pageSize);
        Page<T> resultPage = this.page(page, queryWrapper); // 执行分页查询
        return new PageResponse<T>(page.getRecords(),page.getTotal(), page.getCurrent(),page.getSize(),page.getPages(), page.hasNext());  // 返回分页查询后的数据列表
    }

    // 分页查询（返回当前页的数据列表）z
    @Override
    public List<T> getPageList(long pageNum, long pageSize, LambdaQueryWrapper<T> queryWrapper) {
        Page<T> page = new Page<>(pageNum, pageSize);  // 创建分页对象
        Page<T> resultPage = this.page(page, queryWrapper);  // 执行分页查询
        return resultPage.getRecords();  // 返回分页查询后的数据列表
    }


    // 根据id判断结果是否存在
    @Override
    public boolean existById(Serializable id) {
        return Objects.nonNull(getById(id));
    }

    // 根据id判断结果是否不存在
    @Override
    public boolean notExistById(Serializable id) {
        return !existById(id);
    }

    // 根据列判断结果是否存在
    @Override
    public <V> boolean existByColumn(SFunction<T, V> column, V value) {
        Assert.notNull(column, "column 不可为空");
        if (Objects.isNull(value)) {
            return lambdaQuery()
                    .isNull(column)
                    .exists();
        }
        return lambdaQuery()
                .eq(column, value)
                .exists();
    }

    // 根据列判断结果是否不存在
    @Override
    public <V> boolean notExistByColumn(SFunction<T, V> column, V value) {
        Assert.notNull(column, "column 不可为空");
        if (Objects.isNull(value)) {
            return lambdaQuery()
                    .isNotNull(column)
                    .exists();
        }
        return !lambdaQuery()
                .eq(column, value)
                .exists();
    }

    // 根据列名和值查询单条结果（值可以为null）
    @Override
    public <V> T getByColumnValue(SFunction<T, V> column, V value) {
        // 断言列名是否为空
        Assert.notNull(column, "column 不可为空");
        // 判断值是否为null，为nul则使用is null作为条件查询
        if (Objects.isNull(value)) {
            return lambdaQuery()
                    .isNull(column)
                    .one();
        }
        return lambdaQuery()
                .eq(column, value)
                .one();
    }

    // 根据列名获取某一列的全部结果，结果可能包含重复项，不包含为null的项
    @Override
    public <V> List<?> listColumnValue(SFunction<T, V> column) {
        return listColumnValue(column, false, true);
    }

    // 根据列名获取某一列的全部结果，结果不包含重复项，不包含为null的项
    @Override
    public <V> List<?> listUniqueColumnValue(SFunction<T, V> column) {
        return listColumnValue(column, true, true);
    }

    //根据列名获取某一列的全部结果，根据入参决定是否包含重复项和为null的项
    @Override
    public <V> List<?> listColumnValue(SFunction<T, V> column, boolean unique, boolean nonNull) {
        Assert.notNull(column, "column 不可为空");
        var queryWrapper = lambdaQuery().select(column);
        if (nonNull) {
            queryWrapper = queryWrapper.isNotNull(column);
        }
        var stream = queryWrapper.list()
                .stream()
                .map(column);
        if (unique) {
            stream = stream.distinct();
        }
        return stream.collect(Collectors.toList());
    }

    //根据列名和列值集合查询多条结果
    @Override
    public <V> List<T> listByColumnValues(SFunction<T, V> column, Collection<V> values) {
        return listByColumnValues(column, values, DEFAULT_LIST_BATCH_SIZE);
    }

    //根据列名和列值集合查询多条结果，根据入参决定批量查询的大小
    @Override
    public <V> List<T> listByColumnValues(SFunction<T, V> column, Collection<V> values, int batchSize) {
        if (CollectionUtils.isEmpty(values)) {
            return Collections.emptyList();
        }
        return partitionList(values, batchSize)
                .stream()
                .map(batch -> lambdaQuery().in(column, batch).list())
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    // 根据默认结果集大小进行分片
    @Override
    public List<T> listByIds(Collection<? extends Serializable> ids) {
        return listByIds(ids, DEFAULT_LIST_BATCH_SIZE);
    }

    //根据id查询结果
    @Override
    public List<T> listByIds(Collection<? extends Serializable> ids, int batchSize) {
        if (CollectionUtils.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return partitionList(ids, batchSize)
                .stream()
                .map(super::listByIds)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean removeByColumnValue(SFunction<T, ?> column, Object value) {
        Assert.notNull(column, "Column must not be null");
        return lambdaUpdate().eq(value != null, column, value).or().isNull(value == null, column).remove();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public <V> boolean removeBatchByColumns(SFunction<T, V> column, Collection<V> values, int batchSize) {
        if (CollectionUtils.isEmpty(values)) {
            return false;
        }
        return partitionList(values, batchSize)
                .stream()
                .allMatch(batch -> lambdaUpdate().in(column, batch).remove());
    }

    private <E> List<List<E>> partitionList(Collection<E> list, int size) {
        return new ArrayList<>(list).stream()
                .collect(Collectors.collectingAndThen(Collectors.toList(),
                        fullList -> new ArrayList<>(new LinkedList<>(fullList)).stream()
                                .collect(Collectors.groupingBy(item -> (fullList.indexOf(item) / size), Collectors.toList()))
                                .values()
                                .stream()
                                .toList()));
    }
}
