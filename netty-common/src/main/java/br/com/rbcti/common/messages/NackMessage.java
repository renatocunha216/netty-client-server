package br.com.rbcti.common.messages;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import br.com.rbcti.common.util.ByteBufferWorker;

/**
 *
 * Message structure:<br>
 *
 * [len][id][version][usn][code][msg]<br>
 * [len]           2 bytes - uint<br>
 * [id]            2 bytes - uint<br>
 * [version]       1 bytes - uint<br>
 * [usn]           8 bytes - ulong (unique sequential number)<br>
 * [code]          2 bytes - uint<br>
 * [msg]           n bytes - string ascii (optional)<br>
 *
 * @author Renato Cunha
 *
 */
public class NackMessage implements SimpleMessage {

    private static final int HEADER_LENGTH = 5;
    private static final int FIELD_USN_LENGTH = 8;
    private static final int FIELD_CODE_LENGTH = 2;

    private static final int ID = Messages.NACK;
    private static final short VERSION = 0x01;

    private long usn;
    private int returnCode;
    private String returnMessage;
    private byte[] data;

    public NackMessage(long usn, int code) {
        this(usn, code, null);
    }

    public NackMessage(long usn, int code, String msg) {

        int msgLen = 0;
        byte[] msgBytes = null;

        if (msg != null && msg.trim().length() > 0) {
            msgBytes = msg.getBytes(Charset.forName("US-ASCII"));
            msgLen = msgBytes.length;
        }

        int bufferLen = HEADER_LENGTH + FIELD_USN_LENGTH + FIELD_CODE_LENGTH + msgLen;

        ByteBuffer buffer = ByteBuffer.allocate(bufferLen);
        buffer.putShort((short) (bufferLen - 2));
        buffer.putShort((short) ID);
        buffer.put((byte) VERSION);
        buffer.putLong(usn);
        buffer.putShort((short) code);

        if (msgLen > 0) {
            buffer.put(msgBytes);
        }

        this.usn = usn;
        this.returnCode = code;
        this.returnMessage = msg;

        this.data = buffer.array();
    }

    public NackMessage(byte[] _data) {

        ByteBuffer buffer = ByteBuffer.wrap(_data);

        int _len = ByteBufferWorker.getUnsignedShort(buffer);
        int _id = ByteBufferWorker.getUnsignedShort(buffer);
        int _version = ByteBufferWorker.getUnsignedByte(buffer);

        if ((_len != (_data.length - 2)) || (_id != ID) || (_version != VERSION)) {
            throw new IllegalArgumentException("invalid fields.");
        }

        this.usn = buffer.getLong();
        this.returnCode = ByteBufferWorker.getUnsignedShort(buffer);

        int _partialLength = HEADER_LENGTH + FIELD_USN_LENGTH + FIELD_CODE_LENGTH;

        if (_data.length > _partialLength) {
            byte[] msgBytes = new byte[_data.length - _partialLength];
            buffer.get(msgBytes);
            String retMsg = new String(msgBytes, Charset.forName("US-ASCII"));
            this.returnMessage = retMsg;
        }

        this.data = new byte[_data.length];
        System.arraycopy(_data, 0, this.data, 0, _data.length);
    }

    @Override
    public int getId() {
        return ID;
    }

    public int getReturnCode() {
        return returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public long getUsn() {
        return usn;
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

    @Override
    public String toString() {
        return "NackMessage [returnCode=" + returnCode + ", returnMessage=" + returnMessage + "]";
    }
}
