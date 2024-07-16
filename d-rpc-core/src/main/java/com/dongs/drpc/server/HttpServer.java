package com.dongs.drpc.server;


/**
 * HTTP服务器接口
 *
 * @author dongs
 */
public interface HttpServer {


    /**
     * 启动服务器
     */
    void doStart(int port);

}
