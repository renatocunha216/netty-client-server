package br.com.rbcti.server;

import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.rbcti.common.util.ByteBufferWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

/**
 *
 * @author Renato Cunha
 *
 */
public class DebugReceive extends ByteToMessageDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(DebugReceive.class);

    @Override
    protected void decode(ChannelHandlerContext paramChannelHandlerContext, ByteBuf paramByteBuf, List<Object> paramList) throws Exception {

        synchronized (DebugReceive.class) {
            LOGGER.info("Receiving bytes: {}", paramByteBuf.readableBytes());
            byte [] buf = new byte[paramByteBuf.readableBytes()];
            paramByteBuf.readBytes(buf);
            LOGGER.info(ByteBufferWorker.getDumpString(ByteBuffer.wrap(buf)));
            paramList.add(buf);
        }

    }

}
