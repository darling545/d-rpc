package com.dongs.drpc.fault.tolerant;


import com.dongs.drpc.model.RpcResponse;

import java.util.Map;

/**
 * 容错策略
 *
 * @author dongs
 */
public interface TolerantStrategy {


    /**
     * 容错
     * @param context
     * @param e
     * @return
     * @throws Exception
     */
    RpcResponse doTolerant(Map<String,Object> context,Exception e) throws Exception;
}
