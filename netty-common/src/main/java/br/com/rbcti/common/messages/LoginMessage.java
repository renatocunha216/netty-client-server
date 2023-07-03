package br.com.rbcti.common.messages;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import br.com.rbcti.common.util.ByteBufferWorker;

/**
 *
 * Message structure:<br>
 *
 * [len][id][version][userPassword]<br>
 * [len]                2 bytes - uint<br>
 * [id]                 2 bytes - uint<br>
 * [version]            1 bytes - uint<br>
 * [usn]                8 bytes - ulong (unique sequential number)<br>
 * [userAndPassword]    n bytes - string ascii<br>
 *
 * @author Renato Cunha
 *
 */
public class LoginMessage implements SimpleMessage {

    private static final int ID = Messages.LOGIN;
    private static final short VERSION = 0x01;

    private static final int HEADER_LENGTH = 13;
    private static final byte SEPARATOR = (byte) 0x0A; // \n line feed
    private static final String CHAR_SEPARATOR = "\n";

    private String user;
    private String password;
    private long usn;
    private byte[] data;

    public LoginMessage(String _user, String _password, long _usn) {

        byte[] bytesUser = _user.getBytes(Charset.forName("US-ASCII"));
        byte[] bytesPass = _password.getBytes(Charset.forName("US-ASCII"));
        int dataLen = HEADER_LENGTH + bytesUser.length + 1 + bytesPass.length;

        ByteBuffer buffer = ByteBuffer.allocate(dataLen);

        buffer.putShort((short) (dataLen - 2));
        buffer.putShort((short) ID);
        buffer.put((byte) VERSION);
        buffer.putLong(_usn);

        buffer.put(bytesUser);
        buffer.put(SEPARATOR);
        buffer.put(bytesPass);

        this.data = buffer.array();

        this.user = _user;
        this.password = _password;
        this.usn = _usn;
    }

    public LoginMessage(byte[] _data) {

        ByteBuffer buffer = ByteBuffer.wrap(_data);

        int _len = ByteBufferWorker.getUnsignedShort(buffer);
        int _id = ByteBufferWorker.getUnsignedShort(buffer);
        short _version = ByteBufferWorker.getUnsignedByte(buffer);

        if ((_len != (_data.length - 2)) || (_id != ID) || (_version != VERSION)) {
            throw new IllegalArgumentException("invalid fields.");
        }

        this.usn = buffer.getLong();

        byte[] dst = new byte[_data.length - HEADER_LENGTH];
        buffer.get(dst);

        String _userPass = new String(dst, Charset.forName("US-ASCII"));
        String[] _userPassArr = _userPass.split(CHAR_SEPARATOR);

        this.data = new byte[_data.length];
        this.user = _userPassArr[0];
        this.password = _userPassArr[1];

        System.arraycopy(_data, 0, this.data, 0, _data.length);
    }

    @Override
    public int getId() {
        return ID;
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

    public String getUser() {
        return user;
    }

    public String getPassword() {
        return password;
    }

    public long getUsn() {
        return usn;
    }

}