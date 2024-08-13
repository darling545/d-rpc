package com.dongs.drpc.fault.retry;


import com.dongs.drpc.model.RpcResponse;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

/**
 * 固定时间间隔-重试策略
 *
 * @author dongs
 */
@Slf4j
public class FixedIntervalRetryStrategy implements RetryStrategy{


    /**
     *
     * 重试
     * @param callable
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse daRetry(Callable<RpcResponse> callable) throws Exception {
        Retryer<RpcResponse> retryer = RetryerBuilder.<RpcResponse>newBuilder()
                .retryIfExceptionOfType(Exception.class)
                .withWaitStrategy(WaitStrategies.fixedWait(3L, TimeUnit.SECONDS))
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .withRetryListener(new RetryListener() {
                    @Override
                    public <V> void onRetry(Attempt<V> attempt) {
                        if (attempt.hasException()){
                            log.info("重试次数：{}",attempt.getAttemptNumber());
                        }
                    }
                })
                .build();
        return retryer.call(callable);
    }
}
