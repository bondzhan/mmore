package com.yunshu.id;

import org.junit.jupiter.api.Test;

/**
 * <p>@description
 * <p>@date 2025-02-18  11:49
 *
 * @author 蒋昊宇
 */
public class ConcurrencyTest {
    @Test
    public void testCounterConcurrency() throws InterruptedException {
        MmoreIdProperties mmoreIdProperties = new MmoreIdProperties();
        mmoreIdProperties.setWorkerId(2);
        MmoreIdGenerator idGenerator = new MmoreIdGenerator(mmoreIdProperties);
        int threadCount = 10;
        int executionCountPerThread = 1000;

        ConcurrencyTester.testConcurrency(threadCount, executionCountPerThread, idGenerator::nextId);

//        System.out.println("=================="+ idGenerator.getCount());
    }
}
