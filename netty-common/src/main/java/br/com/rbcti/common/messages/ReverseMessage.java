package br.com.rbcti.common.messages;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import br.com.rbcti.common.util.ByteBufferWorker;


/**
* Mensagem para inverter uma string
*
* [tam][id][versao][texto]
* [tam]                2 bytes - uint
* [id]                 2 bytes - uint
* [versao]             1 bytes - uint
* [usuarioSenha]       n bytes - string ascii
*
* @author Renato Cunha
*
*/
public class ReverseMessage implements SimpleMessage {

    private static final int ID = Messages.REVERSE;
    private static final short VERSION = 0x01;

    private String text;
    private byte[] data;


    public ReverseMessage(String text) {

        final int TAMANHO_HEADER = 5;
        byte[] bytesText = text.getBytes(Charset.forName("US-ASCII"));

        int dataLen = TAMANHO_HEADER + bytesText.length;
        ByteBuffer buffer = ByteBuffer.allocate(dataLen);

        buffer.putShort((short)(dataLen-2));
        buffer.putShort((short)ID);
        buffer.put((byte)VERSION);
        buffer.put(bytesText);

        this.data = buffer.array();
        this.text = text;

    }

    public ReverseMessage(byte [] _data) {

        final int TAMANHO_HEADER = 5;

        ByteBuffer buffer = ByteBuffer.wrap(_data);

        int len = ByteBufferWorker.getUnsignedShort(buffer);
        int id = ByteBufferWorker.getUnsignedShort(buffer);
        short version = ByteBufferWorker.getUnsignedByte(buffer);
        byte[] dst = new byte[_data.length - TAMANHO_HEADER];
        buffer.get(dst);

        this.text = new String(dst, Charset.forName("US-ASCII"));

        this.data = new byte[_data.length];
        System.arraycopy(this.data, 0, this.data, 0, _data.length);

    }

    @Override
    public int getLength() {
        return this.data.length;
    }

    @Override
    public int getId() {
        return ID;
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

    public String getText() {
        return text;
    }


    @Override
    public String toString() {
        return "ReverseMessage [id=" + getId() + ", version=" + getVersion() + ", text=" + getText() + "]";
    }

}
