package br.com.rbcti.client;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.rbcti.client.handlers.ClientHandler;
import br.com.rbcti.common.messages.LoginMessage;
import br.com.rbcti.common.messages.LoginResultMessage;
import br.com.rbcti.common.messages.LogoutMessage;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 *
 *
 *
 * @author Renato Cunha
 *
 */
public class NettyClientTest {

    private static final Logger LOGGER = LogManager.getLogger(NettyClientTest.class);

    private final int port;
    private String host;
    private Channel channel;
    private Bootstrap bootstrap;
    private EventLoopGroup workerGroup;

    public NettyClientTest(String host) {
        this.host = host;
        this.port = 10079;
    }

    public NettyClientTest(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public synchronized void start() throws Exception {

        workerGroup = new NioEventLoopGroup();

        bootstrap = new Bootstrap();
        bootstrap.group(workerGroup);
        bootstrap.channel(NioSocketChannel.class);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        //bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

        bootstrap.handler(new ClientChannelInitializer());

        ChannelFuture future = bootstrap.connect(host, port).sync();

        future.awaitUninterruptibly(5000);

        if (!future.isSuccess()) {
          throw new Exception(future.cause());
        }
        setChannel(future.channel());
        // Wait until the connection is closed.
        //future.channel().closeFuture().sync();
    }

    public boolean isActive() {
        return channel.isActive();
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public synchronized void stop() {
        if (channel.isOpen()) {
            channel.close().awaitUninterruptibly();
        }
        bootstrap.clone();
        workerGroup.shutdownGracefully().awaitUninterruptibly(5000);
    }

    public LoginResultMessage loginRequest(LoginMessage request) throws Exception {

        long startTime = System.currentTimeMillis();
        ClientHandler clientHandler = (ClientHandler) getChannel().pipeline().get("clientHandler");
        LoginResultMessage response = clientHandler.loginRequest(request);
        long endTime = System.currentTimeMillis();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{} message processing time {} ms", LoginMessage.class.getSimpleName(), Long.valueOf(endTime - startTime));
        }
        return response;
    }

    public void logoutRequest(LogoutMessage request) throws Exception {

        long startTime = System.currentTimeMillis();
        ClientHandler clientHandler = (ClientHandler) getChannel().pipeline().get("clientHandler");
        clientHandler.logoutRequest(request);
        long endTime = System.currentTimeMillis();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("{} message processing time {} ms", LogoutMessage.class.getSimpleName(), Long.valueOf(endTime - startTime));
        }
    }

    public void sendFile(String filePath, long usn) throws Exception {

        long startTime = System.currentTimeMillis();
        ClientHandler clientHandler = (ClientHandler) getChannel().pipeline().get("clientHandler");
        clientHandler.sendFile(filePath, usn);
        long endTime = System.currentTimeMillis();

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("SendFile message processing time {} ms", Long.valueOf(endTime - startTime));
        }
        LOGGER.info("SendFile message processing time {} ms", Long.valueOf(endTime - startTime));
    }

}
