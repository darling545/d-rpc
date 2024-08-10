package com.dongs.drpc.model;


import cn.hutool.core.util.StrUtil;
import lombok.Data;

/**
 * 服务元信息类（注册信息类）
 *
 * @author dongs
 */
@Data
public class ServiceMetaInfo {


    /**
     * 服务名称
     */
    private String serviceName;

    /**
     * 服务版本
     */
    private String serviceVersion = "1.0.0";

    /**
     * 服务域名
     */
    private String serviceHost;
    /**
     * 服务端口
     */
    private Integer servicePort;

    /**
     * 服务分组
     */
    private String serviceGroup = "default";


    public String getServiceKey(){
        return String.format("%s:%s",serviceName,serviceVersion);
    }

    public String getServiceNodeKey(){
        return String.format("%s/%s:%s",getServiceKey(),serviceHost,servicePort);
    }

    public String getServiceAddress(){
        if (!StrUtil.contains(serviceHost,"http")){
            return String.format("http://%s:%s",serviceHost,servicePort);
//            return String.format("http://%s",serviceAddress);
        }
        return String.format("%s:%s",serviceHost,servicePort);
//        return String.format("%s",serviceAddress);
    }

}
