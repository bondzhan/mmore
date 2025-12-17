package com.github.mmore.excel.listener;

import cn.idev.excel.context.AnalysisContext;
import cn.idev.excel.event.AnalysisEventListener;

import java.util.ArrayList;
import java.util.List;

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
