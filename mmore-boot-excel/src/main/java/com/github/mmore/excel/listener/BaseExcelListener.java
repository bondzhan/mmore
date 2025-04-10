package com.github.mmore.excel.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>@description
 * <p>@date 2025-02-13  10:00
 *
 * @author 蒋昊宇
 */
public class BaseExcelListener <T> extends AnalysisEventListener<T> {

    private List<T> dataList = new ArrayList<>();

    @Override
    public void invoke(T data, AnalysisContext context) {
        dataList.add(data);
    }

    /**
     * 数据解析完成之后需要做的操作
     *
     * @param context
     */
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }

    /**
     * 获取解析结果
     *
     * @return
     */
    public List<T> getDataList() {
        return dataList;
    }
}
