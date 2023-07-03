package br.com.rbcti.common.messages;

import java.nio.ByteBuffer;

import br.com.rbcti.common.util.ByteBufferWorker;

/**
 * Confirmation message (generic use)<br>
 *
 * Message structure:<br>
 *
 * [len][id][version][usn]<br>
 * [len]           2 bytes - uint<br>
 * [id]            2 bytes - uint<br>
 * [version]       1 bytes - uint<br>
 * [usn]           8 bytes - ulong (unique sequential number)<br>
 *
 * @author Renato Cunha
 *
 */
public class AckMessage implements SimpleMessage {

    private static final int ID = Messages.ACK;
    private static final short VERSION = 0x01;
    private static final short LENGTH = (short) 11;

    private long usn;
    private byte[] data;

    public AckMessage(long usn) {

        ByteBuffer buffer = ByteBuffer.allocate(13);
        buffer.putShort(LENGTH);
        buffer.putShort((short) ID);
        buffer.put((byte) VERSION);
        buffer.putLong(usn);

        this.usn = usn;
        this.data = buffer.array();
    }

    public AckMessage(byte[] _data) {

        ByteBuffer buffer = ByteBuffer.wrap(_data);

        int _len = ByteBufferWorker.getUnsignedShort(buffer);      // len
        int _id = ByteBufferWorker.getUnsignedShort(buffer);       // id
        short _version = ByteBufferWorker.getUnsignedByte(buffer); // version

        if ((_len != LENGTH) || (_id != ID) || (_version != VERSION)) {
            throw new IllegalArgumentException("invalid fields.");
        }

        this.usn = buffer.getLong();
        this.data = new byte[_data.length];
        System.arraycopy(_data, 0, this.data, 0, _data.length);
    }

    public long getUsn() {
        return usn;
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
