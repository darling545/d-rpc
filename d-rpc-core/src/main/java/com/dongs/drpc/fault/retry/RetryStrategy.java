package com.dongs.drpc.fault.retry;

import com.dongs.drpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 重试机制
 *
 * @author dongs
 */
public interface RetryStrategy {


    /**
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    RpcResponse daRetry(Callable<RpcResponse> callable) throws Exception;
}
