package br.com.rbcti.common.messages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.testng.annotations.Test;

/**
*
* @author Renato Cunha
*
*/
public class StartFileTransferMessageTest {

    @Test
    public void testCorrectLength() {

        System.out.println(getClass().getSimpleName() + ".testCorrectLength");

        final int FIRST_FIELDS_LENGTH = 17;

        long usn = 37482L;
        int fileLength = 73829192;
        String fileName = "events-2023-08-07-105625.dat";

        StartFileTransferMessage startFileTransferMessage = new StartFileTransferMessage(usn, fileLength, fileName);

        if (startFileTransferMessage.getVersion() == (short) 1) {
            assertEquals(startFileTransferMessage.getLength(), FIRST_FIELDS_LENGTH + fileName.length());
        }
    }

    @Test
    public void testCorrectContent() {

        System.out.println(getClass().getSimpleName() + ".testCorrectContent");

        long usn = (long) (Math.random() * 1000000000000000L);
        int fileLength = (int) (Math.random() * 1000000000);

        String fileName = "events-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss")) + ".dat";

        StartFileTransferMessage startFileTransferMessage1 = new StartFileTransferMessage(usn, fileLength, fileName);

        byte[] startData1 = startFileTransferMessage1.getData();

        StartFileTransferMessage startFileTransferMessage2 = new StartFileTransferMessage(startData1);

        assertEquals(Arrays.equals(startFileTransferMessage2.getData(), startData1), true);

        assertEquals(startFileTransferMessage1.getId(), startFileTransferMessage2.getId());
        assertEquals(startFileTransferMessage1.getVersion(), startFileTransferMessage2.getVersion());
        assertEquals(startFileTransferMessage1.getUsn(), startFileTransferMessage2.getUsn());

        assertEquals(startFileTransferMessage1.getFileLength(), startFileTransferMessage2.getFileLength());
        assertEquals(startFileTransferMessage1.getFileName(), startFileTransferMessage2.getFileName());
    }

    @Test
    public void testConstructorThrowException() {

        System.out.println(getClass().getSimpleName() + ".testConstructorThrowException");

        StartFileTransferMessage startFileTransferMessage = new StartFileTransferMessage(1L, 1024, "events-2023-08-07-113101.dat");
        byte[] startFileTransferData = startFileTransferMessage.getData();

        byte[] invalidLength = new byte[startFileTransferData.length];
        byte[] invalidId = new byte[startFileTransferData.length];
        byte[] invalidVersion = new byte[startFileTransferData.length];

        System.arraycopy(startFileTransferData, 0, invalidLength, 0, startFileTransferData.length);
        System.arraycopy(startFileTransferData, 0, invalidId, 0, startFileTransferData.length);
        System.arraycopy(startFileTransferData, 0, invalidVersion, 0, startFileTransferData.length);

        invalidLength[0] = 0x00;
        invalidLength[1] = 0x01;

        invalidId[2] = 0x00;
        invalidId[3] = 0x00;

        invalidVersion[4] = 0x07;

        assertThrows(IllegalArgumentException.class, () -> new StartFileTransferMessage(invalidLength));
        assertThrows(IllegalArgumentException.class, () -> new StartFileTransferMessage(invalidId));
        assertThrows(IllegalArgumentException.class, () -> new StartFileTransferMessage(invalidVersion));
    }

}
