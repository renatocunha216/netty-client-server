package br.com.rbcti.common.messages;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import br.com.rbcti.common.util.ByteBufferWorker;

/**
 *
 * Message structure:<br>
 *
 * [len][id][version][usn][fileHash][fileName]<br>
 * [len]            2 bytes - uint<br>
 * [id]             2 bytes - uint<br>
 * [version]        1 bytes - uint<br>
 * [usn]            8 bytes - ulong (unique sequential number)<br>
 * [fileHash]      20 bytes - file sha1 hash<br>
 * [fileName]       n bytes - string ascii
 *
 * @author Renato Cunha
 * @see StartFileTransferMessage
 * @see FileTransferDataMessage
 *
 */
public class EndFileTransferMessage implements SimpleMessage {

    private static final int ID = Messages.END_FILE_TRANSFER;
    private static final short VERSION = 0x01;

    private static final int FIRST_FIELDS_LENGTH = 33;

    private long usn;
    private String fileName;
    private byte[] hash;
    private byte[] data;

    public EndFileTransferMessage(long _usn, String _fileName,  byte[] _hash) {

        byte[] bytesFileName = _fileName.getBytes(Charset.forName("US-ASCII"));

        if (bytesFileName.length > 255) {
            throw new IllegalArgumentException("max file name length is 255");
        }

        if (_hash.length != 20) {
            throw new IllegalArgumentException("hash is not 20 bytes");
        }

        int dataLen = FIRST_FIELDS_LENGTH + bytesFileName.length;

        ByteBuffer buffer = ByteBuffer.allocate(dataLen);

        buffer.putShort((short) (dataLen - 2));
        buffer.putShort((short) ID);
        buffer.put((byte) VERSION);
        buffer.putLong(_usn);
        buffer.put(_hash);
        buffer.put(bytesFileName);

        this.data = buffer.array();

        this.usn = _usn;
        this.fileName = _fileName;

        this.hash = new byte[_hash.length];
        System.arraycopy(_hash, 0, this.hash, 0, _hash.length);
    }

    public EndFileTransferMessage(byte[] _data) {

        ByteBuffer buffer = ByteBuffer.wrap(_data);

        int _len = ByteBufferWorker.getUnsignedShort(buffer);
        int _id = ByteBufferWorker.getUnsignedShort(buffer);
        short _version = ByteBufferWorker.getUnsignedByte(buffer);

        if ((_len != (_data.length - 2)) || (_id != ID) || (_version != VERSION)) {
            throw new IllegalArgumentException("invalid fields.");
        }

        this.usn = buffer.getLong();
        this.hash = new byte[20];
        buffer.get(this.hash);

        int fileNameLength = _data.length - FIRST_FIELDS_LENGTH;

        if (fileNameLength > 255) {
            throw new IllegalArgumentException("max file name length is 255");
        }

        byte[] fileNameData = new byte[fileNameLength];
        buffer.get(fileNameData);

        this.fileName = new String(fileNameData, Charset.forName("US-ASCII"));

        this.data = new byte[_data.length];
        System.arraycopy(_data, 0, this.data, 0, _data.length);
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

    public long getUsn() {
        return usn;
    }

    public String getFileName() {
        return fileName;
    }

    public byte[] getHash() {
        return hash;
    }

}
