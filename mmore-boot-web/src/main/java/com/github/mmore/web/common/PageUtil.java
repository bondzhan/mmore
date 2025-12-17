package com.github.mmore.web.common;

import com.github.mmore.common.model.PageParam;
import com.github.mmore.common.model.PageResponse;

import java.util.ArrayList;


public class PageUtil {

    private static final Integer DEFAULT_CURRENT = 1;

    private static final Integer DEFAULT_SIZE = 10;

    private static final int LARGE_SIZE = 5000;

    public static PageParam getDefaultPageParam() {
        return new PageParam(DEFAULT_CURRENT, DEFAULT_SIZE);
    }

    public static <T> PageResponse<T> emptyPageResponse() {
        return new PageResponse<>(new ArrayList<>(), 0, DEFAULT_CURRENT, DEFAULT_SIZE, 0, false);
    }
}
