package com.dongs.drpc.server.tcp;

import com.dongs.drpc.protocol.ProtocolConstant;
import io.vertx.core.Handler;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.parsetools.RecordParser;

/**
 * TCP消息处理器
 * 使用装饰者模式，对原生的TCP消息处理器进行装饰
 *
 * @author dongs
 */
public class TcpBufferHandlerWrapper implements Handler<Buffer> {


    /**
     * 解析器，用于处理半包、粘包问题
     */
    private final RecordParser recordParser;


    public TcpBufferHandlerWrapper(Handler<Buffer> bufferHandler) {
        recordParser = initRecordParser(bufferHandler);
    }

    @Override
    public void handle(Buffer buffer) {
        recordParser.handle(buffer);
    }

    /**
     * 初始化解析器
     * @param bufferHandler
     * @return
     */
    private RecordParser initRecordParser(Handler<Buffer> bufferHandler) {
        // 构造parser
        RecordParser parser = RecordParser.newFixed(ProtocolConstant.MESSAGE_HEADER_LENGTH);

        parser.setOutput(new Handler<Buffer>() {

            // 初始化
            int size = -1;

            // 读取完整的消息（头 + 体）
            Buffer resultBuffer = Buffer.buffer();
            @Override
            public void handle(Buffer buffer) {
                // 每次循环读取消息头
                if (-1 == size){
                    // 读取消息头的总长
                    size = buffer.getInt(13);
                    parser.fixedSizeMode(size);
                    // 写入头信息到结果中
                    resultBuffer.appendBuffer(buffer);
                } else {
                     // 读取消息体
                    resultBuffer.appendBuffer(buffer);
                    // 消息体读取完毕，将结果传递给下一个处理器
                    bufferHandler.handle(resultBuffer);
                    // 重置
                    parser.fixedSizeMode(ProtocolConstant.MESSAGE_HEADER_LENGTH);
                    resultBuffer = Buffer.buffer();
                    size = -1;
                }
            }
        });
        return parser;
    }
}
