package br.com.rbcti.common.messages;

import static org.testng.Assert.assertEquals;

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

}
