package br.com.rbcti.common.messages;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import br.com.rbcti.common.util.ByteBufferWorker;

/**
 *
 * [tam][id][versao][usuarioSenha]
 * [tam]                2 bytes - uint
 * [id]                 2 bytes - uint
 * [versao]             1 bytes - uint
 * [usuarioSenha]       n bytes - string ascii
 *
 * @author Renato Cunha
 *
 */
public class LoginMessage implements SimpleMessage {

    private static final int ID = Messages.LOGIN;
    private static final short VERSION = 0x01;

    private String login;
    private String password;

    private byte[] data;

    public LoginMessage(String _login, String _password) {
        final int TAMANHO_HEADER = 5;
        byte[]bytesUser = _login.getBytes(Charset.forName("US-ASCII"));
        byte[]bytesPass = _password.getBytes(Charset.forName("US-ASCII"));
        int dataLen = TAMANHO_HEADER + bytesUser.length + bytesPass.length + 1;

        ByteBuffer buffer = ByteBuffer.allocate(dataLen);

        buffer.putShort((short)(dataLen-2));
        buffer.putShort((short)ID);
        buffer.put((byte)VERSION);
        buffer.put(bytesUser);
        buffer.putChar('\n');
        buffer.put(bytesPass);

        this.data = buffer.array();
        setLogin(_login);
        setPassword(_password);

    }

    public LoginMessage(byte[] _data) {
        final int TAMANHO_HEADER = 5;

        ByteBuffer buffer = ByteBuffer.wrap(_data);

        int len = ByteBufferWorker.getUnsignedShort(buffer);
        int id = ByteBufferWorker.getUnsignedShort(buffer);
        short version = ByteBufferWorker.getUnsignedByte(buffer);
        byte[] dst = new byte[_data.length - TAMANHO_HEADER];
        buffer.get(dst);

        String _loginPass = new String(dst, Charset.forName("US-ASCII"));
        String[] _loginPassArr = _loginPass.split("\n");

        this.data = new byte[_data.length];
        System.arraycopy(this.data, 0, this.data, 0, _data.length);
        setLogin(_loginPassArr[0]);
        setPassword(_loginPassArr[1]);
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

    public String getLogin() {
        return login;
    }

    private void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    private void setPassword(String password) {
        this.password = password;
    }

}