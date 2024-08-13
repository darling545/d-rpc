package com.dongs.drpc.fault.retry;

import com.dongs.drpc.model.RpcResponse;

import java.util.concurrent.Callable;

/**
 * 不重试策略
 *
 * @author dongs
 */
public class NoRetryStrategy implements RetryStrategy{


    /**
     * @param callable
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse daRetry(Callable<RpcResponse> callable) throws Exception {
        return callable.call();
    }
}
