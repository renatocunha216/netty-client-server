package br.com.rbcti.server.handlers;

import java.net.SocketAddress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.rbcti.common.commands.Command;
import br.com.rbcti.common.messages.SimpleMessage;
import br.com.rbcti.server.commands.CommandFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


public class ServerHandler extends SimpleChannelInboundHandler<SimpleMessage> {

    private static final Logger LOGGER = LogManager.getLogger(ServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SimpleMessage msg) throws Exception {

        LOGGER.debug("Message received: {}", msg);

        //Channel channel = ctx.channel();

        Command command = CommandFactory.getCommand(msg.getId());
        if (command != null) {
            command.execute(ctx, null, msg);
        }
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        SocketAddress address = ctx.channel().remoteAddress();
        LOGGER.info("Connected with {}.", address);
    }



    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        SocketAddress address = ctx.channel().remoteAddress();
        LOGGER.info("Disconnected from {}.", address);
    }

}