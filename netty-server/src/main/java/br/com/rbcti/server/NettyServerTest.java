package br.com.rbcti.server;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 *
 * @author Renato Cunha
 *
 */
public class NettyServerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerTest.class);

    public int port;
    private boolean started;

    public NettyServerTest() {
        this.port = 10079;
        this.started= false;
    }
    public NettyServerTest(int _port) {
        this.port = _port;
        this.started= false;
    }

    public synchronized void start() throws Exception {

        if (this.started) {
            LOGGER.warn("The server is already started.");
            return;
        }

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup);

        bootstrap.channel(NioServerSocketChannel.class);
        bootstrap.childHandler(new ServerChannelInitializer());

        bootstrap.option(ChannelOption.SO_BACKLOG, 128);

        bootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        bootstrap.childOption(ChannelOption.TCP_NODELAY, true);

        ChannelFuture channelFuture = bootstrap.bind(this.port);
        channelFuture.sync();

        LOGGER.info("TCP/IP server started on port {}.", this.port);

        this.started = true;
    }

    public static void main(String[] args) {

        LOGGER.debug("Starting Netty TCP/IP server...");

        Integer portNumber = Integer.valueOf(10079);

        try {
            if (args.length > 0) {
                portNumber = Integer.valueOf(args[0]);
            }
            LOGGER.info("TCP port number parameter: {}", portNumber);

        } catch (Exception e) {
            LOGGER.warn("Invalid TCP port number parameter.", e);
        }

        NettyServerTest server = new NettyServerTest(portNumber.intValue());

        try {
            server.start();

        } catch (Exception e) {
            LOGGER.error("Erro: {}", e.getMessage());
        }

        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                System.out.println("Stopping the server...");
            }
        }));

    }

}