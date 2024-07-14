package com.dongs.example.consumer;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.dongs.drpc.model.RpcRequest;
import com.dongs.drpc.model.RpcResponse;
import com.dongs.drpc.serializer.JdkSerializer;
import com.dongs.drpc.serializer.Serializer;
import com.dongs.example.common.model.User;
import com.dongs.example.common.service.UserService;

/**
 * 静态代理
 *
 * @author dongs
 */
public class UserServiceProxy implements UserService {


    /**
     *
     * 获取用户名称
     * @param user
     * @return
     */
    @Override
    public User getUser(User user) {
        Serializer serializer = new JdkSerializer();

        RpcRequest rpcRequest = RpcRequest.builder()
                .serviceName(UserService.class.getName())
                .methodName("getUser")
                .args(new Object[]{user})
                .parameterTypes(new Class[]{User.class})
                .build();


        try{
            byte[] bytes = serializer.serialize(rpcRequest);
            byte[] result;
            try(HttpResponse httpResponse = HttpRequest.post("http://localhost:8080")
                    .body(bytes)
                    .execute()){
                result = httpResponse.bodyBytes();
            }
            RpcResponse rpcResponse = serializer.deserialize(result,RpcResponse.class);
            return (User) rpcResponse.getData();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }


}
