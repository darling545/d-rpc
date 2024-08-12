package com.dongs.drpc.server.tcp;

import io.vertx.core.Vertx;

public class VertxTcpClient {

    // 创建Vert.x实例
    public void start() {
        Vertx vertx = Vertx.vertx();
        vertx.createNetClient().connect(8888,"localhost",result-> {
            if (result.succeeded()){
                System.out.println("Connected to server");
                io.vertx.core.net.NetSocket socket = result.result();
                // 发送数据
                socket.write("Hello, server!");
                // 接受响应
                socket.handler(buffer -> {
                    System.out.println("Received data:" + buffer.toString());
                });
            }else {
                System.err.println("Failed to connect to server");
            }
        });
    }


    public static void main(String[] args) {
        new VertxTcpClient().start();
    }
}
