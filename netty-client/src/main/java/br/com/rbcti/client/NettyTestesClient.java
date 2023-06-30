package br.com.rbcti.client;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.rbcti.client.handlers.ClientHandler;
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

    static {
         //Configuração do log4j
         //DOMConfigurator.configureAndWatch("log4j.xml", 10000);
         //LOGGER = Logger.getLogger(NettyTestesClient.class);
    }

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

    public String revert (String texto) throws Exception {
        ClientHandler clientHandler = (ClientHandler)getChannel().pipeline().get("handler");
        if ((texto == null) || (texto.length() == 0)) {
            texto = " ";
        }
        String reverted = clientHandler.revert(texto);
        return reverted;
    }

    public static void main(String[] args) {
        //System.out.println("::" + args[0]);
        //String ip = args[0] == null ? "127.0.0.1" : args[0];

        NettyTestesClient client = new NettyTestesClient("127.0.0.1", 10079);

        try {
            long i = System.currentTimeMillis();
            client.start();
            System.out.println("Conectou");

            String ret = client.revert("renato barbosa da cunha   ");
            for (int c=0; c<10; c++) {
                ret = client.revert("renato barbosa da cunha " + c);
                System.out.println("Retorno::" + ret);
                Thread.sleep(1000);
            }

            //System.out.println("ret=" + ret);
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
        System.out.println("FIM");
    }

}
