package br.com.rbcti.common.messages;

/**
 *
 *
 * @author Renato Cunha
 *
 */
public final class Messages {

    public static final int KEEP_ALIVE = 0x0001;
    public static final int ACK = 0x0002;
    public static final int NACK = 0x0003;

    public static final int LOGIN = 0x0004;
    public static final int LOGIN_RESULT = 0x0005;
    public static final int LOGOUT = 0x0006;

    public static final int START_FILE_TRANSFER = 0x0007;
    public static final int FILE_TRANSFER_DATA = 0x0008;
    public static final int END_FILE_TRANSFER = 0x0009;

    // Private constructor to prevent objects of this class from being created
    private Messages() {
        throw new AssertionError();
    }

}