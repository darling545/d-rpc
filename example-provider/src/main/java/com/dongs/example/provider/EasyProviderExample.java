package com.dongs.example.provider;

import com.dongs.drpc.registry.LocalRegistry;
import com.dongs.drpc.server.HttpServer;
import com.dongs.drpc.server.VertxHttpServer;
import com.dongs.example.common.service.UserService;

public class EasyProviderExample {

    public static void main(String[] args) {

        // 注册服务
        LocalRegistry.registry(UserService.class.getName(),UserServiceImpl.class);

        // 启动服务
        HttpServer httpServer = new VertxHttpServer();

        httpServer.doStart(8080);





    }
}
