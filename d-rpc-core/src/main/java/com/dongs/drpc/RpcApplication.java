package com.dongs.drpc;


import com.dongs.drpc.config.RegistryConfig;
import com.dongs.drpc.config.RpcConfig;
import com.dongs.drpc.constant.RpcConstant;
import com.dongs.drpc.registry.Registry;
import com.dongs.drpc.registry.RegistryFactory;
import com.dongs.drpc.utils.ConfigUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * Rpc框架应用
 * 存放全局用到的变量。双检锁单例设计模式
 *
 * @author dongs
 */
@Slf4j
public class RpcApplication {

    private static volatile RpcConfig rpcConfig;


    /**
     * 框架初始化，支持传入自定义配置
     *
     * @param newRpcConfig
     */
    public static void init(RpcConfig newRpcConfig){
        rpcConfig = newRpcConfig;
        log.info("rpc init,config = {}",newRpcConfig.toString());
        // 注册中心初始化
        RegistryConfig registryConfig = rpcConfig.getRegistryConfig();
        Registry registry = RegistryFactory.getInstance(registryConfig.getRegistryType());
        registry.init(registryConfig);
        log.info("registry init ,config = {}",registryConfig);
    }

    /**
     * 初始化
     */
    public static void init(){
        RpcConfig newRpcConfig;
        try{
            newRpcConfig = ConfigUtils.loadConfig(RpcConfig.class, RpcConstant.DEFAULT_CONFIG_PREFIX);
        }catch (Exception e){
            newRpcConfig = new RpcConfig();
        }
        init(newRpcConfig);
    }

    /**
     * 获取配置
     * @return
     */
    public static RpcConfig getRpcConfig(){
        if (rpcConfig == null){
            synchronized (RpcConfig.class){
                if (rpcConfig == null){
                    init();
                }
            }
        }
        return rpcConfig;
    }


}
