package com.yunshu.id;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * <p>@description
 * <p>@date 2025-02-18  11:48
 *
 * @author 蒋昊宇
 */
public class ConcurrencyTester {

    public static void testConcurrency(int threadCount, int executionCountPerThread, Runnable task) throws InterruptedException {
        // 创建一个线程池
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        // 创建一个 CountDownLatch 用于等待所有线程执行完毕
        CountDownLatch latch = new CountDownLatch(threadCount);

        long i = 0l;
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 1000) {
            executorService.submit(() -> {
                for (int j = 0; j < executionCountPerThread; j++) {
                    task.run();
                }
                // 每个线程执行完毕后，CountDownLatch 计数减 1
                latch.countDown();
            });
        }

        // 等待所有线程执行完毕
        latch.await();

        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;

        // 关闭线程池
        executorService.shutdown();

        System.out.println("Total execution time: " + totalTime + " ms");
    }
}
