package br.com.rbcti.common.messages;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import br.com.rbcti.common.util.ByteBufferWorker;

/**
 *
 * [tam][id][versao][code][msg]
 * [tam]           2 bytes - uint
 * [id]            2 bytes - uint
 * [versao]        1 bytes - uint
 * [code]          2 bytes - uint
 * [msg]           n bytes - string ascii (opcional)
 *
 * @author Renato Cunha
 *
 */
public class NackMessage implements SimpleMessage {

    private static final int ID = Messages.NACK;
    private static final short VERSION = 0x01;

    private Integer returnCode;
    private String returnMessage;
    private byte[] data;

    public NackMessage(int code, String msg) {

        final int HEADER_LENGTH = 5;
        final int FIELD_CODE_LENGTH = 2;
        int msgLen = 0;
        byte[]msgBytes = null;

        if(msg != null && msg.trim().length() > 0) {
            msgBytes = msg.getBytes(Charset.forName("US-ASCII"));
            msgLen = msgBytes.length;
        }

        int tamanho = HEADER_LENGTH + FIELD_CODE_LENGTH + msgLen;

        ByteBuffer buffer = ByteBuffer.allocate(tamanho);
        buffer.putShort((short)(tamanho-2));
        buffer.putShort((short)ID);
        buffer.put((byte)VERSION);
        buffer.putShort((short)code);
        if(msgLen > 0) {
            buffer.put(msgBytes);
        }

        this.data = buffer.array();
        setReturnCode(Integer.valueOf(code));
        setReturnMessage(msg);
    }

    public NackMessage(byte[] _data) {

        final int HEADER_LENGTH = 5;
        final int FIELD_CODE_LENGTH = 2;

        ByteBuffer buffer = ByteBuffer.wrap(_data);

        int len = ByteBufferWorker.getUnsignedShort(buffer);
        int id = ByteBufferWorker.getUnsignedShort(buffer);
        short version = ByteBufferWorker.getUnsignedByte(buffer);
        int returnCode = ByteBufferWorker.getUnsignedShort(buffer);

        int _length = HEADER_LENGTH + FIELD_CODE_LENGTH;

        if(_data.length > _length) {
            byte[] msgBytes = new byte[_data.length - _length];
            buffer.get(msgBytes);
            String retMsg = new String(msgBytes, Charset.forName("US-ASCII"));
            setReturnMessage(retMsg);
        }

        this.data = new byte[_data.length];
        System.arraycopy(this.data, 0, this.data, 0, _data.length);
        setReturnCode(Integer.valueOf(returnCode));

    }

    @Override
    public int getId() {
        return ID;
    }

    public Integer getReturnCode() {
        return returnCode;
    }

    private void setReturnCode(Integer returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    private void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    @Override
    public String toString() {
        return "NackMessage [returnCode=" + returnCode + ", returnMessage="
                + returnMessage + "]";
    }

    @Override
    public int getLength() {
        return this.data.length;
    }

    @Override
    public short getVersion() {
        return VERSION;
    }

    @Override
    public byte[] getData() {
        byte[] ret = new byte[this.data.length];
        System.arraycopy(this.data, 0, ret, 0, this.data.length);
        return ret;
    }
}
