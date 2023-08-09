package br.com.rbcti.common.messages;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.testng.annotations.Test;

/**
 * Unit test for the MessageFactory class<br>
 *
 * @author Renato Cunha
 * @see MessageFactory
 *
 */
public class MessageFactoryTest {

    @Test
    public void testAckMessageFactory() {

        System.out.println(getClass().getSimpleName() + ".testAckMessageFactory");

        byte[] ackData = new byte[] { (byte) 0x00, (byte) 0x0b,
                                      (byte) 0x00, (byte) 0x02,
                                      (byte) 0x01,
                                      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xff };

        AckMessage ackMessage = (AckMessage) MessageFactory.getMessageInstance(ackData);

        assertEquals(ackMessage.getId(), Messages.ACK);
        assertEquals(ackMessage.getUsn(), 255L);
    }

    @Test
    public void testNackMessageFactory() {

        System.out.println(getClass().getSimpleName() + ".testNackMessageFactory");

        byte[] nackData = new byte[] { (byte) 0x00, (byte) 0x12,
                                      (byte) 0x00, (byte) 0x03,
                                      (byte) 0x01,
                                      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0x00, (byte) 0x00,
                                      (byte) 0xff, (byte) 0xff,
                                      (byte) 0x45, (byte) 0x52, (byte) 0x52, (byte) 0x4F, (byte) 0x52 };

        NackMessage nackMessage = (NackMessage) MessageFactory.getMessageInstance(nackData);

        assertEquals(nackMessage.getId(), Messages.NACK);
        assertEquals(nackMessage.getUsn(), 65536L);
        assertEquals(nackMessage.getReturnCode(), 65535);
    }

    @Test
    public void testLoginMessageFactory() {

        System.out.println(getClass().getSimpleName() + ".testLoginMessageFactory");

        byte[] loginData = new byte[] { (byte) 0x00, (byte) 0x0e,
                                      (byte) 0x00, (byte) 0x04,
                                      (byte) 0x01,
                                      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x01, (byte) 0xff,
                                      (byte) 0x41, (byte) 0x0A, (byte) 0x42 };

        LoginMessage loginMessage = (LoginMessage) MessageFactory.getMessageInstance(loginData);

        assertEquals(loginMessage.getId(), Messages.LOGIN);
        assertEquals(loginMessage.getUsn(), 511L);
        assertEquals(loginMessage.getUser(), "A");
        assertEquals(loginMessage.getPassword(), "B");
    }

    @Test
    public void testLogoutMessageFactory() {

        System.out.println(getClass().getSimpleName() + ".testLogoutMessageFactory");

        byte[] ackData = new byte[] { (byte) 0x00, (byte) 0x0b,
                                      (byte) 0x00, (byte) 0x06,
                                      (byte) 0x01,
                                      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x03, (byte) 0x5a, (byte) 0x66, (byte) 0xea };

        LogoutMessage logoutMessage = (LogoutMessage) MessageFactory.getMessageInstance(ackData);

        assertEquals(logoutMessage.getId(), Messages.LOGOUT);
        assertEquals(logoutMessage.getUsn(), 56256234L);
    }

    @Test
    public void testStartFileTransferMessageFactory() {

        System.out.println(getClass().getSimpleName() + ".testStartFileTransferMessageFactory");

        byte[] startFileTransferData = new byte[] { (byte) 0x00, (byte) 0x14, // LENGTH
                                      (byte) 0x00, (byte) 0x07,               // ID
                                      (byte) 0x01,                            // VERSION
                                      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x92, (byte) 0x6a, // USN
                                      (byte) 0x04, (byte) 0x66, (byte) 0x8b, (byte) 0x48,                 // FILE LENGTH
                                      (byte) 0x61, (byte) 0x2e, (byte) 0x64, (byte) 0x61, (byte) 0x74 };  // FILE NAME

        StartFileTransferMessage startFileTransferMessage = (StartFileTransferMessage) MessageFactory.getMessageInstance(startFileTransferData);

        assertEquals(startFileTransferMessage.getId(), Messages.START_FILE_TRANSFER);
        assertEquals(startFileTransferMessage.getUsn(), 37482L);
        assertEquals(startFileTransferMessage.getFileLength(), 73829192);
        assertEquals(startFileTransferMessage.getFileName(), "a.dat");
    }

    @Test
    public void testFileTransferDataMessageFactory() {

        System.out.println(getClass().getSimpleName() + ".testFileTransferDataMessageFactory");

        byte[] fileTransferData = new byte[] { (byte) 0x00, (byte) 0x12, // LENGTH
                                      (byte) 0x00, (byte) 0x08,               // ID
                                      (byte) 0x01,                            // VERSION
                                      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x31, (byte) 0x52, (byte) 0x3a, (byte) 0xfa, // USN
                                      (byte) 0x05,                                                      // FILE NAME LENGTH
                                      (byte) 0x62, (byte) 0x2e, (byte) 0x64, (byte) 0x61, (byte) 0x74,  // FILE NAME
                                      (byte) 0xf1 };                                                    // FILE DATA

        FileTransferDataMessage fileTransferDataMessage = (FileTransferDataMessage) MessageFactory.getMessageInstance(fileTransferData);

        assertEquals(fileTransferDataMessage.getId(), Messages.FILE_TRANSFER_DATA);
        assertEquals(fileTransferDataMessage.getUsn(), 827472634L);
        assertEquals(fileTransferDataMessage.getFileName(), "b.dat");
        assertEquals(Arrays.equals(fileTransferDataMessage.getFileData(), new byte[] { (byte) 0xf1 }), true);
    }

    @Test
    public void testEndFileTransferMessageFactory() {

        System.out.println(getClass().getSimpleName() + ".testEndFileTransferMessageFactory");

        byte[] endFileTransferData = new byte[] { (byte) 0x00, (byte) 0x24,   // LENGTH
                                      (byte) 0x00, (byte) 0x09,               // ID
                                      (byte) 0x01,                            // VERSION
                                      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x1a, (byte) 0x7f, (byte) 0x61, (byte) 0x92, // USN
                                      (byte) 0xa1, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                                      (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xa2,    // HASH
                                      (byte) 0x63, (byte) 0x2e, (byte) 0x64, (byte) 0x61, (byte) 0x74 };  // FILE NAME

        EndFileTransferMessage endFileTransferMessage = (EndFileTransferMessage) MessageFactory.getMessageInstance(endFileTransferData);

        assertEquals(endFileTransferMessage.getId(), Messages.END_FILE_TRANSFER);
        assertEquals(endFileTransferMessage.getUsn(), 444555666L);
        assertEquals(endFileTransferMessage.getFileName(), "c.dat");

        byte[] hashExpected = new byte[20];
        hashExpected[0] = (byte) 0xa1;
        hashExpected[19] = (byte) 0xa2;
        assertEquals(Arrays.equals(endFileTransferMessage.getHash(), hashExpected), true);
    }

}
