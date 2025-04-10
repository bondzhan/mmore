package com.github.mmore.async.model;


import cn.idev.excel.annotation.ExcelIgnore;
import cn.idev.excel.annotation.ExcelProperty;
import lombok.Data;

/**
 * 导入
 */
@Data
public class AsyncTaskParseImportDto extends AsyncTaskParseDto {

    /**
     * 行号
     */
    @ExcelIgnore
    private Integer rowNum;

    /**
     * 如果不为空，则代表该条数据有错误
     */
    @ExcelProperty(value = "错误信息")
    private String errorMsg;
}
