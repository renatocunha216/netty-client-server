package br.com.rbcti.server.commands;

import static br.com.rbcti.common.messages.Messages.END_FILE_TRANSFER;
import static br.com.rbcti.common.messages.Messages.FILE_TRANSFER_DATA;
import static br.com.rbcti.common.messages.Messages.START_FILE_TRANSFER;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Arrays;

import br.com.rbcti.common.Session;
import br.com.rbcti.common.commands.Command;
import br.com.rbcti.common.messages.AckMessage;
import br.com.rbcti.common.messages.EndFileTransferMessage;
import br.com.rbcti.common.messages.FileTransferDataMessage;
import br.com.rbcti.common.messages.NackMessage;
import br.com.rbcti.common.messages.SimpleMessage;
import br.com.rbcti.common.messages.StartFileTransferMessage;
import br.com.rbcti.common.util.Sha1;
import br.com.rbcti.server.handlers.UserSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 *
 * @author Renato Cunha
 *
 */
public class ReceiveFileCommand implements Command {

    // private static final Logger LOGGER = LogManager.getLogger(ReceiveFileCommand.class);

    private static final String PATH = "C:\\temp\\files\\";

    @Override
    public void execute(ChannelHandlerContext ctx, Session session, SimpleMessage message) {

        final int messageId = message.getId();

        SimpleMessage response = null;

        UserSession userSession = (UserSession) ctx.channel().attr(AttributeKey.valueOf("userSession")).get();

        if (userSession == null) {
            response = new NackMessage(message.getUsn(), 2, "Unauthorized user");

        } else {
            try {

                if (messageId == START_FILE_TRANSFER) {
                    StartFileTransferMessage _msg = (StartFileTransferMessage) message;

                    File path = new File(PATH);
                    File file = new File(path, _msg.getFileName());

                    if (!file.exists()) {
                        boolean success = createNewFile(_msg.getFileName());

                        if (success) {
                            response = new AckMessage(message.getUsn());

                        } else {
                            response = new NackMessage(message.getUsn(), 3, "Error writing file");
                        }

                    } else {
                        response = new NackMessage(message.getUsn(), 4, "File already exists");
                    }

                } else if (messageId == FILE_TRANSFER_DATA) {
                    FileTransferDataMessage _msg = (FileTransferDataMessage) message;
                    writeFile(_msg.getFileName(), _msg.getFileData());
                    response = new AckMessage(message.getUsn());

                } else if (messageId == END_FILE_TRANSFER) {
                    EndFileTransferMessage _msg = (EndFileTransferMessage) message;

                    boolean success = endsAndCheckFile(_msg.getFileName(), _msg.getHash());

                    if (success) {
                        response = new AckMessage(message.getUsn());

                    } else {
                        response = new NackMessage(message.getUsn(), 5, "Check error");
                    }

                } else {
                    response = new NackMessage(message.getUsn(), 98, "Invalid message");
                }


            } catch (Exception e) {
                response = new NackMessage(message.getUsn(), 99, "Internal server error");
            }
        }

        ctx.writeAndFlush(response);
    }

    private boolean endsAndCheckFile(String fileName, byte[] hash) throws IOException {
        File path = new File(PATH);
        File tmpFile = new File(path, fileName + ".tmp");
        byte[] dataFile = Files.readAllBytes(tmpFile.toPath());

        Sha1 sha1 = new Sha1();
        sha1.update(dataFile);
        byte[] calculatedHash = sha1.digest();

        if (Arrays.equals(hash, calculatedHash)) {
            File newFileName = new File(path, fileName);
            return tmpFile.renameTo(newFileName);
        }

        return false;
    }

    /**
     *
     * @param fileName
     * @return
     * @throws IOException
     */
    private boolean createNewFile(String fileName) throws IOException {
        File path = new File(PATH);
        File tmpFile = new File(path, fileName + ".tmp");
        return tmpFile.createNewFile();
    }

    /**
     *
     * @param fileName
     * @param dataFile
     * @throws IOException
     */
    private void writeFile(String fileName, byte[] dataFile) throws IOException {
        File path = new File(PATH);
        File tmpFile = new File(path, fileName + ".tmp");
        OutputStream output = new BufferedOutputStream(new FileOutputStream(tmpFile, true));
        output.write(dataFile);
        output.close();
    }

}
