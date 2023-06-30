package br.com.rbcti.common.messages;

import java.nio.ByteBuffer;

/**
* Mensagem que o cliente deve enviar quando quiser se desconectar
* <p>
*
* [tam][id][versao]<br>
* [tam]           2 bytes - uint<br>
* [id]            2 bytes - uint<br>
* [versao]        1 bytes - uint<br>
*
* @author Renato Cunha
*
*/
public class LogoutMessage implements SimpleMessage {

    private static final int ID = Messages.LOGOUT;
    private static final short VERSION = 0x01;

    private byte[] data;

    public LogoutMessage() {

    	ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putShort((short)3);
        buffer.putShort((short)ID);
        buffer.put((byte)VERSION);
        this.data = buffer.array();
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

}
