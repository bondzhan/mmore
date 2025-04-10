package com.github.mmore.id;

import com.github.mmore.id.properties.MmoreIdProperties;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Component;

/**
 * <p>@description
 * <p>@date 2025-02-18  11:07
 *
 * @author 蒋昊宇
 */
@Component
public class MmoreIdGenerator {
    @Resource
    private MmoreIdProperties mmoreIdProperties;

    // 2015-01-01
    private final long START_DATE = 1432483200000L;
    private final long NUM_WORKER_ID_BITS = 4L;
    // 序列号位数：6， 一个毫秒单位内可单个实例承受并发64个ID
    private final long NUM_SEQUENCE_ID_BITS = 6L;

    private final long NUM_WORKER_ID_SHIFTS = NUM_SEQUENCE_ID_BITS;
    private final long NUM_TIMESTAMP_ID_SHIFTS = NUM_SEQUENCE_ID_BITS + NUM_WORKER_ID_BITS;
    private final long SEQUENCE_MASK = ~(-1L << NUM_SEQUENCE_ID_BITS);
    private static final int MAX_CLOCK_BACK_MS = 5; // 最大容忍时钟回拨毫秒数

    // 集群部署，配置worker-id
    private long workerId;
    private long sequenceId=0L;
    private long lastTimestamp=-1L;

    public MmoreIdGenerator(MmoreIdProperties mmoreIdProperties) {
        this.mmoreIdProperties = mmoreIdProperties;
    }

    public synchronized String nextId(String prefix) {
        return String.join("-", prefix, nextId());
    }

    public synchronized String nextId() {
        long currentTimestamp = getCurrentTimestamp();
        // 时钟回拨处理
        if (currentTimestamp < lastTimestamp) {
            long offset = lastTimestamp - currentTimestamp;
            if (offset <= MAX_CLOCK_BACK_MS) {
                try {
                    // 等待两倍的时钟回拨时间
                    wait(offset << 1);
                    currentTimestamp = getCurrentTimestamp();
                    if (currentTimestamp < lastTimestamp) {
                        throw new RuntimeException("时钟回拨异常");
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("ID生成中断", e);
                }
            } else {
                throw new RuntimeException("时钟回拨超过阈值");
            }
        }

        // 如果是同一时间生成，则序列号+1
        if ( lastTimestamp == currentTimestamp ) {
            sequenceId = (sequenceId + 1) & SEQUENCE_MASK;
            // 序列溢出
            if ( sequenceId == 0 ) {
                currentTimestamp = tilNextMillis(lastTimestamp);
            }
        } else {
            // 否则，从0开始
            sequenceId = 0L;
        }
        lastTimestamp = currentTimestamp;
        // 往14位填充
        return String.format("%14s", Long.toHexString(
                ((currentTimestamp - START_DATE) << NUM_TIMESTAMP_ID_SHIFTS)
                        | (getWorkerId() << NUM_WORKER_ID_SHIFTS)
                        | sequenceId)
        ).replace(" ", "0");
    }

    /**
     * 阻塞到下一个毫秒，直到获得新的时间戳
     * @param lastTimestamp
     * @return
     */
    protected long tilNextMillis(long lastTimestamp) {
        long time_stamp = getCurrentTimestamp();
        while ( time_stamp <= lastTimestamp ) {
            time_stamp = getCurrentTimestamp();
        }
        return time_stamp;
    }

    protected long getCurrentTimestamp() {
        return System.currentTimeMillis();
    }

    private long getWorkerId() {
        return mmoreIdProperties.getWorkerId();
    }
}
