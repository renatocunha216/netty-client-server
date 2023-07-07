package br.com.rbcti.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import br.com.rbcti.common.decoders.DecoderMessage;
import br.com.rbcti.common.encoders.EncoderMessage;
import br.com.rbcti.server.handlers.ServerHandler;

/**
 *
 * @author Renato Cunha
 *
 */
public class ServerChannelInitializer extends ChannelInitializer<SocketChannel> {

    private static final int MAX_LENGTH_FRAME = 2 + 65535;

    private static DecoderMessage DECODER = new DecoderMessage();
    private static EncoderMessage ENCODER = new EncoderMessage();

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        ChannelPipeline pipe = socketChannel.pipeline();

        // pipe.addLast("0", new CatchException());
        // pipe.addLast("1", new ReadTimeoutHandler(new HashedWheelTimer(), TIMEOUT));
        // pipe.addLast("0", new DebugReceive());
        pipe.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(MAX_LENGTH_FRAME, 0, 2));
        pipe.addLast("decoderMessage", DECODER);
        pipe.addLast("encoderMessage", ENCODER);
        pipe.addLast("serverHandler", new ServerHandler());
    }

}
