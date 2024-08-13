package com.dongs.drpc.fault.tolerant;

/**
 * 容错策略的keys
 */
public interface TolerantStrategyKeys {


    /**
     * 快速失败-容错策略（立刻通知外层调用）
     */
    String FAIL_FAST = "failFast";

    /**
     * 静默处理-容错策略（不通知外层调用）
     */
    String FAIL_SAFE = "failSafe";
}
