package br.com.rbcti.common.messages;

import static org.testng.Assert.assertEquals;

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
public class FileTransferDataMessageTest {

    @Test
    public void testCorrectLength() {

        System.out.println(getClass().getSimpleName() + ".testCorrectLength");

        final int FIELDS_LENGTH = 14;
        final int DATA_LENGTH = 16384;

        long usn = 57949191L;
        String fileName = "events-2023-08-07-112207.dat";

        FileTransferDataMessage fileTransferDataMessage = new FileTransferDataMessage(usn, fileName, new byte[DATA_LENGTH]);

        if (fileTransferDataMessage.getVersion() == (short) 1) {
            assertEquals(fileTransferDataMessage.getLength(), FIELDS_LENGTH + fileName.length() + DATA_LENGTH);
        }
    }


    @Test
    public void testCorrectRandomContent() {

        System.out.println(getClass().getSimpleName() + ".testCorrectRandomContent");

        long usn = (long) (Math.random() * 1000000000000000L);
        int fileLength = (int) (Math.random() * 10000);

        byte[] fileData = new byte[fileLength];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(fileData);

        String fileName = "events-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss")) + ".dat";

        FileTransferDataMessage fileTransferDataMessage1 = new FileTransferDataMessage(usn, fileName, fileData);

        byte[] fileData1 = fileTransferDataMessage1.getData();

        FileTransferDataMessage fileTransferDataMessage2 = new FileTransferDataMessage(fileData1);

        assertEquals(Arrays.equals(fileTransferDataMessage2.getData(), fileData1), true);

        assertEquals(fileTransferDataMessage1.getId(), fileTransferDataMessage2.getId());
        assertEquals(fileTransferDataMessage1.getVersion(), fileTransferDataMessage2.getVersion());
        assertEquals(fileTransferDataMessage1.getUsn(), fileTransferDataMessage2.getUsn());
        assertEquals(fileTransferDataMessage1.getFileName(), fileTransferDataMessage2.getFileName());
        assertEquals(Arrays.equals(fileTransferDataMessage1.getFileData(), fileTransferDataMessage2.getFileData()), true);
    }

    @Test
    public void testCorrectMaxContent() {

        System.out.println(getClass().getSimpleName() + ".testCorrectMaxContent");

        final long USN = Long.MAX_VALUE;
        final int MAX_MESSAGE_LENGTH = 65535;
        final int MESSAGE_FIELDS_LENGTH = 12;

        // Build file name
        long fileId = (int) (Math.random() * 100000000000000000L);

        String bigFileName = "events-" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HHmmss")) + "_";
        int missing = 255 - 4 - bigFileName.length();
        bigFileName += StringUtils.leftPad(Long.valueOf(fileId).toString(), missing, "0") + ".dat";

        // Build data file
        int fileLength = MAX_MESSAGE_LENGTH - MESSAGE_FIELDS_LENGTH - bigFileName.length();

        byte[] fileData = new byte[fileLength];

        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(fileData);

        FileTransferDataMessage fileTransferDataMessage1 = new FileTransferDataMessage(USN, bigFileName, fileData);

        byte[] fileData1 = fileTransferDataMessage1.getData();

        FileTransferDataMessage fileTransferDataMessage2 = new FileTransferDataMessage(fileData1);

        assertEquals(Arrays.equals(fileTransferDataMessage2.getData(), fileData1), true);

        assertEquals(fileTransferDataMessage1.getId(), fileTransferDataMessage2.getId());
        assertEquals(fileTransferDataMessage1.getVersion(), fileTransferDataMessage2.getVersion());
        assertEquals(fileTransferDataMessage1.getUsn(), fileTransferDataMessage2.getUsn());
        assertEquals(fileTransferDataMessage1.getFileName(), fileTransferDataMessage2.getFileName());
        assertEquals(Arrays.equals(fileTransferDataMessage1.getFileData(), fileTransferDataMessage2.getFileData()), true);

        assertEquals(fileTransferDataMessage2.getFileName(), bigFileName);
        assertEquals(fileTransferDataMessage2.getUsn(), USN);
        assertEquals(Arrays.equals(fileTransferDataMessage2.getFileData(), fileData), true);
    }

}
