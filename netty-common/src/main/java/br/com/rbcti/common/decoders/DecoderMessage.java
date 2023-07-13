package br.com.rbcti.common.decoders;

import java.nio.ByteBuffer;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.rbcti.common.messages.MessageFactory;
import br.com.rbcti.common.messages.SimpleMessage;
import br.com.rbcti.common.util.ByteBufferWorker;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 *
 * @author Renato Cunha
 *
 */
@Sharable
public class DecoderMessage extends MessageToMessageDecoder<ByteBuf> {

    private static final Logger LOGGER = LogManager.getLogger(DecoderMessage.class);

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) throws Exception {

        byte[] readables = new byte[msg.readableBytes()];
        msg.readBytes(readables);

        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Data received: {}", ByteBufferWorker.getDumpString(ByteBuffer.wrap(readables)));
        }

        SimpleMessage simpleMessage = MessageFactory.getMessageInstance(readables);
        out.add(simpleMessage);
    }

}
