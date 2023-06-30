package br.com.rbcti.client;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;


public class NettyTestesClient {

    private static final Logger LOGGER = LogManager.getLogger(NettyTestesClient.class);

    private final int port;
    private String host;
    private Channel channel;
    private Bootstrap bootstrap;
    EventLoopGroup workerGroup;

    public NettyTestesClient(String host) {
        this.host = host;
        this.port = 10079;
    }

    public NettyTestesClient(String host, int port) {
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

    public static void main(String[] args) {

        NettyTestesClient client = new NettyTestesClient("127.0.0.1", 10079);

        try {
            long i = System.currentTimeMillis();
            client.start();
            System.out.println("Connected.");

            // TODO:

            long f = System.currentTimeMillis();

            System.out.println("Tempo::" + (f-i));


        } catch(Exception e) {
            e.printStackTrace();

        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //System.exit(1);
        }
        System.out.println("End.");
    }

}
