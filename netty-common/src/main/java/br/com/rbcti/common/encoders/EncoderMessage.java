package br.com.rbcti.common.encoders;

import java.nio.ByteBuffer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.rbcti.common.messages.SimpleMessage;
import br.com.rbcti.common.util.ByteBufferWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *
 * @author Renato Cunha
 *
 */
public class EncoderMessage extends MessageToByteEncoder<SimpleMessage> {

    private static final Logger LOGGER = LogManager.getLogger(EncoderMessage.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, SimpleMessage msg, ByteBuf out) throws Exception {

        if (LOGGER.isDebugEnabled()) {
            synchronized (EncoderMessage.class) {
                LOGGER.debug("Sending message: {}", msg);
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Sending data: {}", ByteBufferWorker.getDumpString(ByteBuffer.wrap(msg.getData())));
                }
            }
        }

        out.writeBytes(msg.getData());
    }
}
