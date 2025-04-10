package com.github.mmore.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @Author Bond
 * @Date 2025/1/13
 * @Description TODO
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> records;  // 数据列表
    private long total;       // 总记录数
    private long current;     // 当前页
    private long size;        // 每页显示条数
    private long totalPages;  // 总页数
    private boolean hasNext;  // 是否有下一页
}

