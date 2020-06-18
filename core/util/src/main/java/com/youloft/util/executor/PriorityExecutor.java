package com.youloft.util.executor;


import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 优先级线程池
 *
 * @VERSION 1.0.0
 * @AUTHOR: xll create 2018/5/14 10:17
 */
public class PriorityExecutor extends ThreadPoolExecutor {
    private AtomicLong lowAto = new AtomicLong(20000000);
    private AtomicLong highAto = new AtomicLong(0);
    private AtomicLong normalAto = new AtomicLong(10000000);

    public PriorityExecutor(int corePoolSize) {
        this(corePoolSize, corePoolSize, 0L, TimeUnit.SECONDS);
    }

    public PriorityExecutor(int corePoolSize,
                            int maximumPoolSize,
                            long keepAliveTime,
                            TimeUnit unit) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, createQueue());
    }

    /**
     * 创建一个不限线程池大小
     *
     * @return
     */
    public static PriorityExecutor newCachedThreadPool() {
        return new PriorityExecutor(0, Integer.MAX_VALUE,
                60L, TimeUnit.SECONDS);
    }

    /**
     * 创建指定线程池大小的
     *
     * @param nThreads
     * @return
     */
    public static PriorityExecutor newFixedThreadPool(int nThreads) {
        return new PriorityExecutor(nThreads);
    }

    @Override
    public void execute(Runnable command) {
        execute(command, Priority.NORMAL);
    }

    /**
     * 获取最后的基准值
     *
     * @return
     */
    private long getPriority(Priority priority) {
        switch (priority) {
            case LOW:
                return lowAto.getAndIncrement();
            case HIGH:
                return highAto.getAndIncrement();
            default:
                return normalAto.getAndIncrement();
        }
    }

    /**
     * 处理线程
     *
     * @param command
     * @param priority
     */
    public void execute(Runnable command, Priority priority) {
        if (command == null) {
            return;
        }
        if (priority == null) {
            priority = Priority.NORMAL;
        }
        if (command instanceof PriorityRunnable) {
            ((PriorityRunnable) command).setPriority(getPriority(priority));
            super.execute(command);
        } else {
            super.execute(new PriorityRunnable(getPriority(priority), command));
        }
    }

    /**
     * 创建队列
     *
     * @return
     */
    private static PriorityBlockingQueue<Runnable> createQueue() {
        return new PriorityBlockingQueue<>(20, new NetComparator());
    }

    /**
     * 排序~优先级越小越先执行
     */
    private static class NetComparator implements Comparator<Runnable> {
        @Override
        public int compare(Runnable lhs, Runnable rhs) {
            Long lhsPriority = (lhs instanceof PriorityRunnable) ? ((PriorityRunnable) lhs).priority : 0;
            Long rhsPriority = (rhs instanceof PriorityRunnable) ? ((PriorityRunnable) rhs).priority : 0;
            return lhsPriority.compareTo(rhsPriority);
        }
    }

    /**
     * 优先级runnable
     */
    private class PriorityRunnable implements Runnable {
        /**
         * 任务优先级
         */
        public long priority;
        /**
         * 任务真正执行者
         */
        private final Runnable runnable;

        public PriorityRunnable(long priority, Runnable runnable) {
            this.priority = priority;
            this.runnable = runnable;
        }

        /**
         * 设置级别
         *
         * @param priority
         */
        public void setPriority(long priority) {
            this.priority = priority;
        }

        @Override
        public final void run() {
            this.runnable.run();
        }
    }
}
