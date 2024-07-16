package com.dongs.drpc.server;


import io.vertx.core.Vertx;

public class VertxHttpServer implements HttpServer{
    @Override
    public void doStart(int port) {

        // 创建vert.x实例
        Vertx vertx = Vertx.vertx();

        // 创建HTTP服务器
        io.vertx.core.http.HttpServer httpServer = vertx.createHttpServer();

        // 监听接口并处理请求
//        httpServer.requestHandler(request -> {
//
//            // 处理http请求
//            System.out.println("Received request:" + request.method() + "  " + request.uri());
//
//            // 发送http相应
//            request.response()
//                    .putHeader("content-type", "text/plain")
//                    .end("Hello World");
//        });
        httpServer.requestHandler(new HttpServerHandler());

        // 启动HTTP服务器并监听指定端口
        httpServer.listen(port,result -> {
           if (result.succeeded()){
               System.out.println("Server is listening on port:" + port);
           }else {
               System.out.println("Server failed to start:" + result.cause());
           }
        });
    }
}
