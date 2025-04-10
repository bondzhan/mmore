package com.github.mmore.db.util;

import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.function.Consumer;

/**
 * @author zhj
 * @date 2025/3/12
 */
public class TransactionAfterCommitHelper {

    /**
     * 注册一个在事务提交后执行的任务
     * @param task
     */
    public static <T> void executeAfterCommit(Consumer<T> task, T param) {
        if (TransactionSynchronizationManager.isActualTransactionActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    task.accept(param);
                }
            });
        } else {
            task.accept(param);
        }
    }
}
