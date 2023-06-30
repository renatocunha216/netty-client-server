package br.com.rbcti.common.messages;

import java.nio.ByteBuffer;

/**
 * Confirmation message (generic use)<br>
 *
 * Message structure:<br>
 *
 * [len][id][version]<br>
 * [len]           2 bytes - uint<br>
 * [id]            2 bytes - uint<br>
 * [version]       1 bytes - uint<br>
 *
 * @author Renato Cunha
 *
 */
public class AckMessage implements SimpleMessage {

    private static final int ID = Messages.ACK;
    private static final short VERSION = 0x01;
    private byte[] data;

    public AckMessage() {

        ByteBuffer buffer = ByteBuffer.allocate(5);
        buffer.putShort((short)3);
        buffer.putShort((short)ID);
        buffer.put((byte)VERSION);

        this.data = buffer.array();
    }

    @Override
    public byte[] getData() {
        byte[] ret = new byte[this.data.length];
        System.arraycopy(this.data, 0, ret, 0, this.data.length);
        return ret;
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

}
