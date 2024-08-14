package com.dongs.dongsrpcspringbootstarter.annotation;


import com.dongs.dongsrpcspringbootstarter.bootstrap.RpcConsumerBootstrap;
import com.dongs.dongsrpcspringbootstarter.bootstrap.RpcInitBootstrap;
import com.dongs.dongsrpcspringbootstarter.bootstrap.RpcProviderBootstrap;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 启用RPC框架
 *
 * @author dongs
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import({RpcInitBootstrap.class,RpcProviderBootstrap.class, RpcConsumerBootstrap.class})
public @interface EnableRpc {


    /**
     * 需要启动server
     */
    boolean needServer() default true;
}
