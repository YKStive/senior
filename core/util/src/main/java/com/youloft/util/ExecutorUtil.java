package com.youloft.util;

import com.youloft.util.executor.PriorityExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

/**
 * 各种线程池
 *
 * @author xll
 * @date 2018/8/23 14:19
 */
public class ExecutorUtil {
    public static final PriorityExecutor IO_EXECUTOR = PriorityExecutor.newFixedThreadPool(2);
    public static final PriorityExecutor NET_EXECUTOR = PriorityExecutor.newFixedThreadPool(3);
    public static final PriorityExecutor AD_EXECUTOR = PriorityExecutor.newFixedThreadPool(2);
    public static final PriorityExecutor COM_EXECUTOR = PriorityExecutor.newCachedThreadPool();
    public static final PriorityExecutor DB_EXECUTOR = PriorityExecutor.newFixedThreadPool(1);
    public static final PriorityExecutor VIEW_EXCUTORS = PriorityExecutor.newFixedThreadPool(1);
}
