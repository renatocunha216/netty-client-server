package br.com.rbcti.common.util;

/**
 * Utility class to handle bytes.
 *
 * @author Renato Cunha
 * @version 1.0
 */
public class ByteUtil {


    private final static char[] hexArray = "0123456789abcdef".toCharArray();

    public static String encodeHex(byte _byte) {
        return encodeHex(new byte[] {_byte});
    }

    public static String encodeHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    public static String encodeHexSpaced(byte[] bytes) {

        if (bytes == null) {
            return null;
        }

        final int dataLen = bytes.length;
        int index = 0;

        char[] hexChars = new char[dataLen * 2];

        if (dataLen > 1) {
            hexChars = new char[(dataLen * 2) + (dataLen - 1)];
        }

        for (int j = 0, i = 0; j < dataLen; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[i] = hexArray[v >>> 4];
            hexChars[i + 1] = hexArray[v & 0x0F];
            index++;

            if ((dataLen > 1) && index < dataLen) {
                hexChars[i + 2] = '\u0020'; // space
                i += 3;
            }
        }

        return new String(hexChars);
    }

    public static byte[] decodeHex(String src) {

        String srcTemp = src.replaceAll(" ", "");

        int len = srcTemp.length();

        if (len % 2 != 0) {
            throw new IllegalArgumentException("Invalid format. The length of the hexadecimal string is not a multiple of 2.");
        }

        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(srcTemp.charAt(i), 16) << 4) + Character.digit(srcTemp.charAt(i + 1), 16));
        }
        return data;
    }

    public static long fromBigEndian(byte[] buffer) {

        if (buffer == null) {
            throw new NullPointerException("buffer is null.");
        }

        if ((buffer.length > 8) || (buffer.length == 0)) {
            throw new IllegalArgumentException("invalid buffer length.");
        }

        long value = 0L;

        for (int ct = 0; ct < buffer.length; ct++) {
            value |= (buffer[buffer.length - ct - 1] & 0xFFL) << (8L * ct);
        }

        return value & 0xFFFFFFFFFFFFFFFFL;
    }

}