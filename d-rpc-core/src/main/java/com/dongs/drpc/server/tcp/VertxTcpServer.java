package com.dongs.drpc.server.tcp;

import com.dongs.drpc.server.HttpServer;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.net.NetServer;

/**
 * TCP服务器实现
 */
public class VertxTcpServer implements HttpServer {

    private byte[] handleRequest(byte[] requestData) {
        return "hello client".getBytes();
    }


    /**
     * @param port
     */
    @Override
    public void doStart(int port) {
        // 创建Vertx实例
        Vertx vertx = Vertx.vertx();

        NetServer netServer = vertx.createNetServer();
        netServer.connectHandler(new TcpServerHandler()).listen(port, result -> {
            if (result.succeeded()) {
                System.out.println("TCP服务器启动成功，监听端口：" + port);
            } else {
                System.err.println("TCP服务器启动失败：" + result.cause());
            }
        });
//        // 创建TCP服务器
//        vertx.createNetServer().connectHandler(socket -> {
//            // 处理TCP连接
//            socket.handler(buffer -> {
//                // 处理TCP数据
//                byte[] requestData = buffer.getBytes();
//                byte[] responseData = handleRequest(requestData);
//                socket.write(Buffer.buffer(responseData));
//            });
//        }).listen(port,result -> {
//            if (result.succeeded()) {
//                System.out.println("TCP服务器启动成功，监听端口：" + port);
//            } else {
//                System.err.println("TCP服务器启动失败：" + result.cause());
//            }
//        });
    }

    public static void main(String[] args) {
        new VertxTcpServer().doStart(8080);
    }
}
