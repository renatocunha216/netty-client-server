package br.com.rbcti.common.messages;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import br.com.rbcti.common.util.ByteBufferWorker;

/**
 *
 * Message structure:<br>
 *
 * [len][id][version][code][uuid]<br>
 * [tam]           2 bytes - uint<br>
 * [id]            2 bytes - uint<br>
 * [version]       1 bytes - uint<br>
 * [usn]           8 bytes - ulong (unique sequential number)<br>
 * [code]          1 bytes - uint - 1-LOGIN OK  2-LOGIN NOT OK<br>
 * [uuid]          n bytes - string ascii (session id, will be filled in case code 1)<br>
 *
 * @author Renato Cunha
 *
 */
public class LoginResultMessage implements SimpleMessage {

    private static final int ID = Messages.LOGIN_RESULT;
    private static final short VERSION = 0x01;

    public static final short LOGIN_OK = 0x01;
    public static final short LOGIN_NOK = 0x02;

    private long usn;
    private String uuid;
    private short returnCode;

    private byte[] data;

    public LoginResultMessage(byte[] _data) {

        ByteBuffer buffer = ByteBuffer.wrap(_data);

        int _len = ByteBufferWorker.getUnsignedShort(buffer);
        int _id = ByteBufferWorker.getUnsignedShort(buffer);
        short _version = ByteBufferWorker.getUnsignedByte(buffer);

        if ((_len != (_data.length - 2)) || (_id != ID) || (_version != VERSION)) {
            throw new IllegalArgumentException("invalid fields.");
        }

        this.usn = buffer.getLong();
        this.returnCode = ByteBufferWorker.getUnsignedByte(buffer);

        if(_len > 12) {
            byte[] dst = new byte[_len - 12];
            buffer.get(dst);
            this.uuid = new String(dst, Charset.forName("US-ASCII"));
        }

        this.data = new byte[_data.length];
        System.arraycopy(_data, 0, this.data, 0, _data.length);
    }

    public LoginResultMessage(short _returnCode, String _uuid, long _usn) {

        final int HEADER_LENGTH = 5;
        final int FIELDS_LENGTH = 9;

        byte[] dataUUID = null;

        if (_uuid != null) {
            dataUUID = _uuid.getBytes(Charset.forName("US-ASCII"));
            this.uuid = _uuid;
        }

        int dataLen = HEADER_LENGTH + FIELDS_LENGTH;

        if (dataUUID != null) {
            dataLen += dataUUID.length;
        }

        ByteBuffer buffer = ByteBuffer.allocate(dataLen);

        buffer.putShort((short) (dataLen - 2));
        buffer.putShort((short) ID);
        buffer.put((byte) VERSION);
        buffer.putLong(_usn);
        buffer.put((byte) _returnCode);

        if (dataUUID != null) {
            buffer.put(dataUUID);
        }
        this.data = buffer.array();
        this.usn = _usn;
        this.returnCode = _returnCode;
    }

    @Override
    public int getId() {
        return ID;
    }

    public long getUsn() {
        return usn;
    }

    public String getUuid() {
        return uuid;
    }

    public short getReturnCode() {
        return returnCode;
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
        return "LoginResultMessage [usn=" + usn + ", uuid=" + uuid + ", returnCode=" + returnCode + "]";
    }

}
