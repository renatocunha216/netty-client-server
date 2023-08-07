package br.com.rbcti.common.messages;

/**
 *
 * Message structure:<br>
 *
 * [len][id][version][code][uuid]<br>
 * [len]            2 bytes - uint<br>
 * [id]             2 bytes - uint<br>
 * [version]        1 bytes - uint<br>
 * [usn]            8 bytes - ulong (unique sequential number)<br>
 * [?]              4 bytes - uint<br>
 * [?]              n bytes - string ascii
 *
 * @author Renato Cunha
 * @see StartFileTransferMessage
 * @see EndFileTransferMessage
 *
 */
public class FileTransferDataMessage implements SimpleMessage {

    private static final int ID = Messages.FILE_TRANSFER_DATA;
    private static final short VERSION = 0x01;

    @Override
    public int getLength() {
        return 0;
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
        return null;
    }

}
