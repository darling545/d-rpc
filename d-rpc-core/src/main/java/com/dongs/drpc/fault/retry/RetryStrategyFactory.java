package com.dongs.drpc.fault.retry;

import com.dongs.drpc.spi.SpiLoader;

public class RetryStrategyFactory {

    static {
        SpiLoader.load(RetryStrategy.class);
    }

    /**
     * 默认重试策略
     */
    private static final RetryStrategy DEFAULT_RETRY_STRATEGY = new NoRetryStrategy();

    /**
     * 获取实例
     * @param retryStrategyName
     * @return
     */
    public static RetryStrategy getRetryStrategy(String retryStrategyName) {
        return SpiLoader.getInstance(RetryStrategy.class, retryStrategyName);
    }
}
