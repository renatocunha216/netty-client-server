package br.com.rbcti.common.messages;

import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import java.util.Arrays;

/**
 * Unit test for the LoginMessage class<br>
 *
 * @author Renato Cunha
 * @see LoginMessage
 *
 */
public class LoginMessageTest {

    @Test
    public void testCorrectLength() {

        System.out.println(getClass().getSimpleName() + ".testCorrectLength");

        final int HEADER_LENGTH = 13;
        final int SEPARATOR_LENGTH = 1;

        String user = "user123";
        String password = "password#123";

        LoginMessage loginMessage = new LoginMessage(user, password, (long) (Math.random() * 10000000L));

        if (loginMessage.getVersion() == (short) 1) {
            assertEquals(loginMessage.getLength(), HEADER_LENGTH + user.length() + SEPARATOR_LENGTH + password.length());
        }
    }

    @Test
    public void testCorrectContent() {

        System.out.println(getClass().getSimpleName() + ".testCorrectContent");

        long usn = (long) (Math.random() * 1000000000000000L);

        String user = "user123";
        String password = "password#123";

        LoginMessage loginMessage1 = new LoginMessage(user, password, usn);
        byte[] loginData1 = loginMessage1.getData();

        LoginMessage loginMessage2 = new LoginMessage(loginData1);

        assertEquals(Arrays.equals(loginMessage2.getData(), loginData1), true);

        assertEquals(loginMessage1.getId(), loginMessage2.getId());
        assertEquals(loginMessage1.getVersion(), loginMessage2.getVersion());
        assertEquals(loginMessage1.getUsn(), loginMessage2.getUsn());
    }

    @Test
    public void testConstructorThrowException() {

        LoginMessage loginMessage = new LoginMessage("a", "b", 0);
        byte[] loginData = loginMessage.getData();

        byte[] loginDataInvalidLength = new byte[loginData.length];
        byte[] loginDataInvalidId = new byte[loginData.length];
        byte[] loginDataInvalidVersion = new byte[loginData.length];

        System.arraycopy(loginData, 0, loginDataInvalidLength, 0, loginData.length);
        System.arraycopy(loginData, 0, loginDataInvalidId, 0, loginData.length);
        System.arraycopy(loginData, 0, loginDataInvalidVersion, 0, loginData.length);

        loginDataInvalidLength[0] = 0x00;
        loginDataInvalidLength[1] = 0x01;

        loginDataInvalidId[2] = 0x00;
        loginDataInvalidId[3] = 0x00;

        loginDataInvalidVersion[4] = 0x07;

        assertThrows(IllegalArgumentException.class, () -> new LoginMessage(loginDataInvalidLength));
        assertThrows(IllegalArgumentException.class, () -> new LoginMessage(loginDataInvalidId));
        assertThrows(IllegalArgumentException.class, () -> new LoginMessage(loginDataInvalidVersion));
    }

}
