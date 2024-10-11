package br.com.rbcti.client.handlers;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.rbcti.common.messages.EndFileTransferMessage;
import br.com.rbcti.common.messages.FileTransferDataMessage;
import br.com.rbcti.common.messages.LoginMessage;
import br.com.rbcti.common.messages.LoginResultMessage;
import br.com.rbcti.common.messages.LogoutMessage;
import br.com.rbcti.common.messages.Messages;
import br.com.rbcti.common.messages.SimpleMessage;
import br.com.rbcti.common.messages.StartFileTransferMessage;
import br.com.rbcti.common.util.Sha1;
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

    private static final Logger LOGGER = LoggerFactory.getLogger(ClientHandler.class);

    private volatile Channel channel;
    private final BlockingQueue<SimpleMessage> messagesReceived = new LinkedBlockingQueue<SimpleMessage>();


    private SimpleMessage getMessage() throws Exception {
        SimpleMessage msg = null;
        try {
            msg = messagesReceived.poll(TIMEOUT, TimeUnit.MILLISECONDS);
        } catch (InterruptedException e) {
        }

        if (msg == null) {
            throw new Exception("Timeout exception");
        }
        return msg;
    }

    private SimpleMessage getMessageAndCheck(long usn) throws Exception {
        SimpleMessage msg = getMessage();
        if (usn != msg.getUsn()) {
            throw new Exception("Invalid response");
        }
        return msg;
    }

    public synchronized LoginResultMessage loginRequest(LoginMessage request) throws Exception {

        LoginResultMessage response = null;

        Channel _channel = getChannel();

        if ((_channel != null) && (_channel.isActive())) {
            _channel.writeAndFlush(request);
            SimpleMessage simpleMessage = getMessageAndCheck(request.getUsn());

            response = (LoginResultMessage) simpleMessage;

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

    public synchronized void sendFile(String filePath, long usn) throws Exception {

        Channel _channel = getChannel();

        if ((_channel != null) && (_channel.isActive())) {

            File file = new File(filePath);

            if (!file.exists()) {
                throw new Exception("File not found.");
            }

            String fileName = file.getName();
            long size = Files.size(file.toPath());

            int maxSliceSize = 65535 - 12 - fileName.getBytes().length;

            List<byte[]> slices = sliceFile(file, size, maxSliceSize);

            LOGGER.info("Sending the {} file in {} slices.", fileName, Integer.valueOf(slices.size()));

            if (slices.size() > 0) {

                StartFileTransferMessage startRequest = new StartFileTransferMessage(usn, (int) size, fileName);
                _channel.writeAndFlush(startRequest);

                if (getMessageAndCheck(usn).getId() != Messages.ACK) {
                    throw new Exception("Error starting transfer.");
                }

                usn++;

                Sha1 sha1 = new Sha1();

                for (byte[] slice : slices) {
                    FileTransferDataMessage fileTransferDataMessage = new FileTransferDataMessage(usn, fileName, slice);
                    _channel.writeAndFlush(fileTransferDataMessage);

                    if (getMessageAndCheck(usn).getId() != Messages.ACK) {
                        throw new Exception("File transfer error.");
                    }

                    usn++;

                    sha1.update(slice);
                }

                EndFileTransferMessage endFileTransferMessage = new EndFileTransferMessage(usn, fileName, sha1.digest());
                _channel.writeAndFlush(endFileTransferMessage);

                if (getMessageAndCheck(usn).getId() != Messages.ACK) {
                    throw new Exception("Error at the end of file transfer.");
                }
            }

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
        super.channelInactive(ctx);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        super.exceptionCaught(ctx, cause);
    }

    /**
     * Reads the file and stores it in memory in slices.<br>
     * Do not use for large files.
     *
     * @param fileName
     * @param length
     * @return
     * @throws IOException
     */
    private List<byte[]> sliceFile(File file, long fileSize, int length) throws IOException {

        int totalSlice = (int) (fileSize / length);
        int remainder = (int) (fileSize % length);

        List<byte[]> slices = new ArrayList<>();

        InputStream input = null;

        try {
            input = new BufferedInputStream(new FileInputStream(file));

            for (int i = 0; i < totalSlice; i++) {
                byte[] buffer = new byte[length];
                input.read(buffer);
                slices.add(buffer);
            }

            if (remainder > 0) {
                byte[] bufferRemainder = new byte[remainder];
                input.read(bufferRemainder);
                slices.add(bufferRemainder);
            }

        } finally {
            if (input != null) {
                input.close();
            }
        }

        return slices;
    }

}
