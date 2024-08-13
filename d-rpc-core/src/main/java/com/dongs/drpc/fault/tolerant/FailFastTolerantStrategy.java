package com.dongs.drpc.fault.tolerant;

import com.dongs.drpc.model.RpcResponse;

import java.util.Map;

/**
 * 快速失败-容错策略（立刻通知外层调用）
 *
 * @author dongs
 */
public class FailFastTolerantStrategy implements TolerantStrategy{
    /**
     * @param context
     * @param e
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception {
        throw new RuntimeException("服务报错",e);
    }
}
