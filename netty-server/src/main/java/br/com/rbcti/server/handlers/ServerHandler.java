package br.com.rbcti.server.handlers;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.rbcti.common.commands.Command;
import br.com.rbcti.common.messages.SimpleMessage;
import br.com.rbcti.server.ServerManager;
import br.com.rbcti.server.UserManager;
import br.com.rbcti.server.commands.CommandFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.AttributeKey;

/**
 *
 * @author Renato Cunha
 *
 */
public class ServerHandler extends SimpleChannelInboundHandler<SimpleMessage> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SimpleMessage msg) throws Exception {

        LOGGER.debug("Message received: {}", msg);

        UserSession userSession = (UserSession) ctx.channel().attr(AttributeKey.valueOf("userSession")).get();

        Command command = CommandFactory.getCommand(msg.getId());

        if (command != null) {
            command.execute(ctx, userSession, msg);
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

        UserManager userManager = ServerManager.getInstance().getUserManager();
        userManager.removeUser(ctx.channel());

        // Remove userSession
        ctx.channel().attr(AttributeKey.valueOf("userSession")).set(null);

        SocketAddress address = ctx.channel().remoteAddress();
        LOGGER.info("Disconnected from {}.", address);
    }

}