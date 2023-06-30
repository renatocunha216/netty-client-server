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
 * [versao]        1 bytes - uint<br>
 * [code]          1 bytes - uint - 1-LOGIN OK  2-LOGIN NOT OK<br>
 * [uuid]          n bytes - string ascii (session id, will be filled in case code 1)<br>
 *
 * @author Renato Cunha
 *
 */
public class LoginResultMessage implements SimpleMessage {

    private static final int ID = Messages.LOGIN_RESULT;
    private static final short VERSION = 0x01;

    public static final short OK = 0x01;
    public static final short NAO_OK = 0x02;

    private String uuid;
    private short returnCode;

    private byte[] data;

    public LoginResultMessage(byte[] _data) {

        ByteBuffer buffer = ByteBuffer.wrap(_data);

        int len = ByteBufferWorker.getUnsignedShort(buffer);
        ByteBufferWorker.getUnsignedShort(buffer); // id

        ByteBufferWorker.getUnsignedByte(buffer); // version
        returnCode = ByteBufferWorker.getUnsignedByte(buffer);

        if(len > 4) {
            byte[] dst = new byte[len - 4];
            buffer.get(dst);
            this.uuid = new String(dst, Charset.forName("US-ASCII"));
        }

        this.data = new byte[_data.length];
        System.arraycopy(this.data, 0, this.data, 0, _data.length);
    }

    public LoginResultMessage(short _returnCode, String _uuid) {
        final int TAMANHO_HEADER = 5;
        final int TAMANHO_CODE = 1;

        byte[] dataUUID = null;
        if(_uuid != null) {
            dataUUID = _uuid.getBytes(Charset.forName("US-ASCII"));
            setUuid(_uuid);
        }
        int tamanho = TAMANHO_HEADER + TAMANHO_CODE;
        if(dataUUID != null) {
            tamanho += dataUUID.length;
        }

        ByteBuffer buffer = ByteBuffer.allocate(tamanho);

        buffer.putShort((short)(tamanho-2));
        buffer.putShort((short)ID);
        buffer.put((byte)VERSION);
        buffer.put((byte)_returnCode);

        if(dataUUID != null) {
            buffer.put(dataUUID);
        }
        this.data = buffer.array();
        setReturnCode(_returnCode);

    }

    @Override
    public int getId() {
        return ID;
    }

    public String getUuid() {
        return uuid;
    }

    private void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public short getReturnCode() {
        return returnCode;
    }

    private void setReturnCode(short returnCode) {
        this.returnCode = returnCode;
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
