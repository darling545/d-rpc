package com.dongs.drpc.fault.retry;

/**
 * 重试策略键名常量
 *
 * @author dongs
 */
public interface RetryStrategyKeys {

    /**
     * 不重试
     */
    String NO_RETRY = "noRetry";

    /**
     * 固定时间间隔
     */
    String FIXED_INTERVAL = "fixedInterval";
}
