package br.com.rbcti.client.handlers;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.rbcti.common.messages.LoginMessage;
import br.com.rbcti.common.messages.LoginResultMessage;
import br.com.rbcti.common.messages.LogoutMessage;
import br.com.rbcti.common.messages.SimpleMessage;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 *
 *
 * @author Renato Cunha
 *
 */
public class ClientHandler extends SimpleChannelInboundHandler<SimpleMessage> {

    private static final long TIMEOUT = 1000L * 10;

    private static final Logger LOGGER = LogManager.getLogger(ClientHandler.class);

    private volatile Channel channel;
    private final BlockingQueue<SimpleMessage> messagesReceived = new LinkedBlockingQueue<SimpleMessage>();


    private SimpleMessage getMessage() throws Exception {
        SimpleMessage msg = null;
        try {
            msg = messagesReceived.poll(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }
        if(msg == null) {
            throw new Exception("Timeout exception");
        }
        return msg;
    }

    public synchronized LoginResultMessage loginRequest(LoginMessage request) throws Exception {

        LoginResultMessage response = null;

        Channel _channel = getChannel();

        if ((_channel != null) && (_channel.isActive())) {
            _channel.writeAndFlush(request);
            SimpleMessage simpleMessage = getMessage();

            response = (LoginResultMessage) simpleMessage;

            if (request.getUsn() != response.getUsn()) {
                throw new Exception("Invalid response.");
            }

        } else {
            throw new Exception("Client is disconnected.");
        }

        return response;
    }

    public synchronized void logoutRequest(LogoutMessage request) throws Exception {

        Channel _channel = getChannel();

        if ((_channel != null) && (_channel.isActive())) {
            _channel.writeAndFlush(request);

        } else {
            throw new Exception("Client is disconnected.");
        }
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, SimpleMessage msg) throws Exception {
        LOGGER.debug("Message received: {}", msg);
        messagesReceived.add(msg);
    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        setChannel(ctx.channel());
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        // TODO Auto-generated method stub
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

}
