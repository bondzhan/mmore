package com.github.mmore.common.model;

import java.io.Serializable;

/**
 * @author zhj
 * @date 2025/1/21
 */
public class PageParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 每页数据
     */
    private long size;

    /**
     *
     */
    private long start;

    /**
     * 目标页码
     */
    private long current;

    private final long DEFAULT_CURRENT = 1L;
    private final long DEFAULT_SIZE = 10L;

    public PageParam() {
        this.current = DEFAULT_CURRENT;
        this.size = DEFAULT_SIZE;
    }

    public PageParam(long current, long size) {
        setCurrent(current);
        setSize(size);
    }

    public PageParam(Integer current, Integer size, Integer start) {
        setCurrent(current);
        setSize(size);
        setStart(start);
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size<1?DEFAULT_SIZE:size;
    }

    public long getCurrent() {
        return current;
    }

    public void setCurrent(long current) {
        this.current = current<=0?DEFAULT_CURRENT:current;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }
}
