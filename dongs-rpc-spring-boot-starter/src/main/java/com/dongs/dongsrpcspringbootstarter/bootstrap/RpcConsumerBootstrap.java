package com.dongs.dongsrpcspringbootstarter.bootstrap;

import com.dongs.dongsrpcspringbootstarter.annotation.RpcReference;
import com.dongs.drpc.proxy.ServiceProxyFactory;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import java.lang.reflect.Field;

public class RpcConsumerBootstrap implements BeanPostProcessor {


    /**
     * @param bean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        Class<?> beanClass = bean.getClass();
        // 遍历对象的所有属性
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields){
            RpcReference rpcReference = field.getAnnotation(RpcReference.class);
            if (rpcReference!= null){
                // 为属性生成代理对象
                Class<?> proxyClass = rpcReference.interfaceClass();
                if (proxyClass == void.class){
                    proxyClass = field.getType();
                }
                field.setAccessible(true);
                Object proxy = ServiceProxyFactory.getProxy(proxyClass);
                try {
                    field.set(bean,proxy);
                    field.setAccessible(false);
                }catch (Exception e){
                    throw new RuntimeException("为字段注入代理对象失败",e);
                }
            }
        }
        return BeanPostProcessor.super.postProcessAfterInitialization(bean, beanName);
    }
}
