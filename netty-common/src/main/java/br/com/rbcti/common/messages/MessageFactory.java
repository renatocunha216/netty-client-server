package br.com.rbcti.common.messages;

import static br.com.rbcti.common.messages.Messages.ACK;
import static br.com.rbcti.common.messages.Messages.KEPP_ALIVE;
import static br.com.rbcti.common.messages.Messages.LOGIN;
import static br.com.rbcti.common.messages.Messages.LOGIN_RESULT;
import static br.com.rbcti.common.messages.Messages.LOGOUT;
import static br.com.rbcti.common.messages.Messages.NACK;

import java.nio.ByteBuffer;

import br.com.rbcti.common.util.ByteBufferWorker;

/**
 *
 *
 * @author Renato Cunha
 *
 */
public class MessageFactory {

    public static final KeepAliveMessage KEPP_ALIVE_MESSAGE = new KeepAliveMessage();

    private MessageFactory() {
    }

    public static SimpleMessage getMessageInstance(ByteBuffer buffer) {

        ByteBufferWorker.getUnsignedShort(buffer);          // length
        int id = ByteBufferWorker.getUnsignedShort(buffer);
        ByteBufferWorker.getUnsignedByte(buffer);           // version

        byte [] data = buffer.array();

        SimpleMessage response = null;

        switch (id) {
            case KEPP_ALIVE: //KeepAlive
                response = KEPP_ALIVE_MESSAGE;
                break;
            case ACK:
                response = new AckMessage(data);
                break;
            case NACK:
                response = new NackMessage(data);
                break;
            case LOGIN:
                response = new LoginMessage(data);
                break;
            case LOGIN_RESULT:
                response = new LoginResultMessage(data);
                break;
            case LOGOUT:
                response = new LogoutMessage(data);
                break;
            default:
                 throw new IllegalArgumentException("Unknown message.");
        }
        return response;

    }

    public static SimpleMessage getMessageInstance(byte [] buffer) {
        return getMessageInstance(ByteBuffer.wrap(buffer));
    }

}
