package br.com.rbcti.common.messages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import org.apache.commons.lang3.StringUtils;
import org.testng.annotations.Test;

/**
 *
 * @author Renato Cunha
 *
 */
public class EndFileTransferMessageTest {

    @Test
    public void testCorrectLength() {

        System.out.println(getClass().getSimpleName() + ".testCorrectLength");

        final int FIRST_FIELDS_LENGTH = 33;

        long usn = 18382732L;
        String fileName = "events-2023-08-09-095343.dat";

        EndFileTransferMessage endFileTransferMessage = new EndFileTransferMessage(usn, fileName, new byte[20]);

        if (endFileTransferMessage.getVersion() == (short) 1) {
            assertEquals(endFileTransferMessage.getLength(), FIRST_FIELDS_LENGTH + fileName.length());
        }
    }

    @Test
    public void testCorrectContent() {

        System.out.println(getClass().getSimpleName() + ".testCorrectContent");

        byte[] hash = new byte[20];
        long usn = (long) (Math.random() * 1000000000000000L);

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(hash);

        String fileName = "events-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss")) + ".dat";

        EndFileTransferMessage endFileTransferMessage1 = new EndFileTransferMessage(usn, fileName, hash);

        byte[] endData1 = endFileTransferMessage1.getData();

        EndFileTransferMessage endFileTransferMessag2 = new EndFileTransferMessage(endData1);

        assertEquals(Arrays.equals(endFileTransferMessag2.getData(), endData1), true);

        assertEquals(endFileTransferMessage1.getId(), endFileTransferMessag2.getId());
        assertEquals(endFileTransferMessage1.getVersion(), endFileTransferMessag2.getVersion());
        assertEquals(endFileTransferMessage1.getUsn(), endFileTransferMessag2.getUsn());

        assertEquals(Arrays.equals(endFileTransferMessage1.getHash(), endFileTransferMessag2.getHash()), true);
        assertEquals(endFileTransferMessage1.getFileName(), endFileTransferMessag2.getFileName());
    }

    @Test
    public void testConstructorThrowException() {

        System.out.println(getClass().getSimpleName() + ".testConstructorThrowException");

        String maxFileName = "longFileName_" + StringUtils.leftPad("1", 242, "0");
        String longFileName = "longFileName_" + StringUtils.leftPad("1", 243, "0");

        EndFileTransferMessage endFileTransferMessage = new EndFileTransferMessage(1L, "events-2023-08-07-0000000113101.dat", new byte[20]);
        byte[] endFileTransferMessageData = endFileTransferMessage.getData();

        byte[] invalidLength = new byte[endFileTransferMessageData.length];
        byte[] invalidId = new byte[endFileTransferMessageData.length];
        byte[] invalidVersion = new byte[endFileTransferMessageData.length];

        System.arraycopy(endFileTransferMessageData, 0, invalidLength, 0, endFileTransferMessageData.length);
        System.arraycopy(endFileTransferMessageData, 0, invalidId, 0, endFileTransferMessageData.length);
        System.arraycopy(endFileTransferMessageData, 0, invalidVersion, 0, endFileTransferMessageData.length);

        invalidLength[0] = 0x00;
        invalidLength[1] = 0x11;

        invalidId[2] = 0x00;
        invalidId[3] = 0x7f;

        invalidVersion[4] = 0x07;

        assertThrows(IllegalArgumentException.class, () -> new EndFileTransferMessage(invalidLength));
        assertThrows(IllegalArgumentException.class, () -> new EndFileTransferMessage(invalidId));
        assertThrows(IllegalArgumentException.class, () -> new EndFileTransferMessage(invalidVersion));
        assertThrows(IllegalArgumentException.class, () -> new EndFileTransferMessage(1L, "events-2023-08-07-0000000113101.dat", new byte[19]));
        assertThrows(IllegalArgumentException.class, () -> new EndFileTransferMessage(1L, "events-2023-08-07-0000000113101.dat", new byte[21]));
        assertThrows(IllegalArgumentException.class, () -> new EndFileTransferMessage(1L, longFileName, new byte[20]));

        EndFileTransferMessage endFileTransferMessageLimitSize = new EndFileTransferMessage(1L, maxFileName, new byte[20]);
        byte[] dataLimitSize = endFileTransferMessageLimitSize.getData();
        byte[] sizeLimitExceeded = new byte[dataLimitSize.length + 1];
        System.arraycopy(dataLimitSize, 0, sizeLimitExceeded, 0, dataLimitSize.length);

        // Increments one byte in the length field
        sizeLimitExceeded[1]++;
        // Insert letter A at the end of the array
        sizeLimitExceeded[sizeLimitExceeded.length - 1] = 'A';

        assertThrows(IllegalArgumentException.class, () -> new EndFileTransferMessage(sizeLimitExceeded));
    }

}
