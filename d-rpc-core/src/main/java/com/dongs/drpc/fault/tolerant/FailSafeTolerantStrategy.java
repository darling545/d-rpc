package com.dongs.drpc.fault.tolerant;

import com.dongs.drpc.model.RpcResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * 静默处理-容错策略（不通知外层调用）
 *
 * @author dongs
 */
@Slf4j
public class FailSafeTolerantStrategy implements TolerantStrategy{
    /**
     * @param context
     * @param e
     * @return
     * @throws Exception
     */
    @Override
    public RpcResponse doTolerant(Map<String, Object> context, Exception e) throws Exception {
        log.info("静默处理-容错策略（不通知外层调用）",e);
        return new RpcResponse();
    }
}
