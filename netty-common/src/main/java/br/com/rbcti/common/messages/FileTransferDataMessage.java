package br.com.rbcti.common.messages;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

import br.com.rbcti.common.util.ByteBufferWorker;

/**
 *
 * Message structure:<br>
 *
 * [len][id][version][usn][fileNameLength][fileName][fileData]<br>
 * [len]             2 bytes - uint<br>
 * [id]              2 bytes - uint<br>
 * [version]         1 bytes - uint<br>
 * [usn]             8 bytes - ulong (unique sequential number)<br>
 * [fileNameLength]  1 bytes - uint<br>
 * [fileName]        n bytes - string ascii<br>
 * [fileData]        n bytes - raw data
 *
 * @author Renato Cunha
 * @see StartFileTransferMessage
 * @see EndFileTransferMessage
 *
 */
public class FileTransferDataMessage implements SimpleMessage {

    private static final int ID = Messages.FILE_TRANSFER_DATA;
    private static final short VERSION = 0x01;

    private static final int MAX_MESSAGE_LENGTH = 65535;

    private static final int HEADER_LENGTH = 13;
    private static final int LENGTH_FIELD_FILE_LENGTH = 1;

    private long usn;
    private String fileName;
    private byte[] fileData;
    private byte[] data;

    public FileTransferDataMessage(long _usn, String _fileName,  byte[] _fileData) {

        byte[] bytesFileName = _fileName.getBytes(Charset.forName("US-ASCII"));

        if (bytesFileName.length > 255) {
            throw new IllegalArgumentException("max file name length is 255");
        }

        if (_fileData.length > (MAX_MESSAGE_LENGTH - HEADER_LENGTH - LENGTH_FIELD_FILE_LENGTH + 2)) {
            throw new IllegalArgumentException("maximum data size exceeded");
        }

        int dataLen = HEADER_LENGTH + LENGTH_FIELD_FILE_LENGTH + bytesFileName.length + _fileData.length;

        ByteBuffer buffer = ByteBuffer.allocate(dataLen);

        buffer.putShort((short) (dataLen - 2));
        buffer.putShort((short) ID);
        buffer.put((byte) VERSION);
        buffer.putLong(_usn);

        ByteBufferWorker.putUnsignedByte(buffer, bytesFileName.length);
        buffer.put(bytesFileName);
        buffer.put(_fileData);

        this.data = buffer.array();

        this.usn = _usn;
        this.fileName = _fileName;
        this.fileData = _fileData;
    }

    public FileTransferDataMessage(byte[] _data) {

        ByteBuffer buffer = ByteBuffer.wrap(_data);

        int _len = ByteBufferWorker.getUnsignedShort(buffer);
        int _id = ByteBufferWorker.getUnsignedShort(buffer);
        short _version = ByteBufferWorker.getUnsignedByte(buffer);

        if ((_len != (_data.length - 2)) || (_id != ID) || (_version != VERSION)) {
            throw new IllegalArgumentException("invalid fields.");
        }

        this.usn = buffer.getLong();

        int fileNameLength = ByteBufferWorker.getUnsignedByte(buffer);
        byte[] fileNameBytes = new byte[fileNameLength];
        buffer.get(fileNameBytes);
        byte[] dst = new byte[_data.length - (HEADER_LENGTH + LENGTH_FIELD_FILE_LENGTH + fileNameLength)];
        buffer.get(dst);

        this.fileName = new String(fileNameBytes, Charset.forName("US-ASCII"));
        this.fileData = dst;

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
        return this.data;
    }

    public byte[] getFileData() {
        return fileData;
    }

    @Override
    public long getUsn() {
        return usn;
    }

    public String getFileName() {
        return fileName;
    }

}
