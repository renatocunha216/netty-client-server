package br.com.rbcti.common.messages;

import static br.com.rbcti.common.messages.Messages.ACK;
import static br.com.rbcti.common.messages.Messages.END_FILE_TRANSFER;
import static br.com.rbcti.common.messages.Messages.FILE_TRANSFER_DATA;
import static br.com.rbcti.common.messages.Messages.KEEP_ALIVE;
import static br.com.rbcti.common.messages.Messages.LOGIN;
import static br.com.rbcti.common.messages.Messages.LOGIN_RESULT;
import static br.com.rbcti.common.messages.Messages.LOGOUT;
import static br.com.rbcti.common.messages.Messages.NACK;
import static br.com.rbcti.common.messages.Messages.START_FILE_TRANSFER;

import java.nio.ByteBuffer;

import br.com.rbcti.common.util.ByteBufferWorker;

/**
 *
 *
 * @author Renato Cunha
 *
 */
public class MessageFactory {

    public static final KeepAliveMessage KEEP_ALIVE_MESSAGE = new KeepAliveMessage();

    private MessageFactory() {
    }

    public static SimpleMessage getMessageInstance(ByteBuffer buffer) {

        ByteBufferWorker.getUnsignedShort(buffer);          // length
        int id = ByteBufferWorker.getUnsignedShort(buffer);
        ByteBufferWorker.getUnsignedByte(buffer);           // version

        byte [] data = buffer.array();

        SimpleMessage message = null;

        switch (id) {
            case KEEP_ALIVE: //KeepAlive
                message = KEEP_ALIVE_MESSAGE;
                break;
            case ACK:
                message = new AckMessage(data);
                break;
            case NACK:
                message = new NackMessage(data);
                break;
            case LOGIN:
                message = new LoginMessage(data);
                break;
            case LOGIN_RESULT:
                message = new LoginResultMessage(data);
                break;
            case LOGOUT:
                message = new LogoutMessage(data);
                break;
            case START_FILE_TRANSFER:
                message = new StartFileTransferMessage(data);
                break;
            case FILE_TRANSFER_DATA:
                message = new FileTransferDataMessage(data);
                break;
            case END_FILE_TRANSFER:
                message = new EndFileTransferMessage(data);
                break;
            default:
                 throw new IllegalArgumentException("Unknown message.");
        }

        return message;
    }

    public static SimpleMessage getMessageInstance(byte [] buffer) {
        return getMessageInstance(ByteBuffer.wrap(buffer));
    }

}
