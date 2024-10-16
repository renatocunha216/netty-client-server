package br.com.rbcti.common.encoders;

import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.rbcti.common.messages.SimpleMessage;
import br.com.rbcti.common.util.ByteBufferWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 *
 * @author Renato Cunha
 *
 */
@Sharable
public class EncoderMessage extends MessageToByteEncoder<SimpleMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncoderMessage.class);

    @Override
    protected void encode(ChannelHandlerContext ctx, SimpleMessage msg, ByteBuf out) throws Exception {

        if (LOGGER.isDebugEnabled()) {
            synchronized (EncoderMessage.class) {
                LOGGER.debug("Sending message: {}", msg);
                if (LOGGER.isTraceEnabled()) {
                    LOGGER.trace("Sending data:{}{}", System.lineSeparator(), ByteBufferWorker.getDumpString(ByteBuffer.wrap(msg.getData())));
                }
            }
        }

        out.writeBytes(msg.getData());
    }
}
