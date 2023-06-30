package br.com.rbcti.client;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import br.com.rbcti.client.handlers.ClientHandler;
import br.com.rbcti.common.decoders.DecoderMessage;
import br.com.rbcti.common.encoders.EncoderMessage;

public class ClientChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {

        final int TIMEOUT = 10000; //em segundos
        final int MAX_LENGTH_FRAME = 2 + 65535;

        ChannelPipeline pipe = socketChannel.pipeline();

        //pipe.addLast("0", new CatchException());
        //pipe.addLast("1", new ReadTimeoutHandler(new HashedWheelTimer(), TIMEOUT));
        pipe.addLast("2", new LengthFieldBasedFrameDecoder(MAX_LENGTH_FRAME, 0, 2));
        pipe.addLast("3", new DecoderMessage());
        pipe.addLast("4", new EncoderMessage());
        pipe.addLast("handler", new ClientHandler());

    }

}
