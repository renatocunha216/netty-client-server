package br.com.rbcti.common.messages;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import br.com.rbcti.common.util.ByteBufferWorker;

/**
 * Start of file transfer.
 *
 * Message structure:<br>
 *
 * [len][id][version][usn][fileLength][fileName]<br>
 * [len]            2 bytes - uint<br>
 * [id]             2 bytes - uint<br>
 * [version]        1 bytes - uint<br>
 * [usn]            8 bytes - ulong (unique sequential number)<br>
 * [fileLength]     4 bytes - uint<br>
 * [fileName]       n bytes - string ascii
 *
 * @author Renato Cunha
 * @see FileTransferDataMessage
 * @see EndFileTransferMessage
 *
 */
public class StartFileTransferMessage implements SimpleMessage {

    private static final int ID = Messages.START_FILE_TRANSFER;
    private static final short VERSION = 0x01;

    private static final int FIRST_FIELDS_LENGTH = 17;

    private long usn;
    private long fileLength;
    private String fileName;
    private byte[] data;

    public StartFileTransferMessage(long _usn, int _fileLength, String _fileName) {

        byte[] bytesFileName = _fileName.getBytes(Charset.forName("US-ASCII"));

        int dataLen = FIRST_FIELDS_LENGTH + bytesFileName.length;

        ByteBuffer buffer = ByteBuffer.allocate(dataLen);

        buffer.putShort((short) (dataLen - 2));
        buffer.putShort((short) ID);
        buffer.put((byte) VERSION);
        buffer.putLong(_usn);
        buffer.putInt(_fileLength);
        buffer.put(bytesFileName);

        this.data = buffer.array();

        this.usn = _usn;
        this.fileLength = _fileLength;
        this.fileName = _fileName;
    }

    public StartFileTransferMessage(byte[] _data) {

        ByteBuffer buffer = ByteBuffer.wrap(_data);

        int _len = ByteBufferWorker.getUnsignedShort(buffer);
        int _id = ByteBufferWorker.getUnsignedShort(buffer);
        short _version = ByteBufferWorker.getUnsignedByte(buffer);

        if ((_len != (_data.length - 2)) || (_id != ID) || (_version != VERSION)) {
            throw new IllegalArgumentException("invalid fields.");
        }

        this.usn = buffer.getLong();
        this.fileLength = ByteBufferWorker.getUnsignedInt(buffer);

        byte[] fileNameData = new byte[_data.length - FIRST_FIELDS_LENGTH];
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

    @Override
    public long getUsn() {
        return usn;
    }

    public long getFileLength() {
        return fileLength;
    }

    public String getFileName() {
        return fileName;
    }

    @Override
    public String toString() {
        return "StartFileTransferMessage [id=" + getId() + ", version=" + getVersion() + ", usn=" + usn + ", fileLength=" + fileLength + ", fileName=" + fileName + "]";
    }

}
