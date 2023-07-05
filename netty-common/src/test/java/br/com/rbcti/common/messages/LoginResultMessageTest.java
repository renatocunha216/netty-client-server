package br.com.rbcti.common.messages;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertThrows;

import java.util.Arrays;

import org.testng.annotations.Test;

/**
 * Unit test for the LoginResultMessage class<br>
 *
 * @author Renato Cunha
 * @see LoginResultMessage
 *
 */
public class LoginResultMessageTest {

    @Test
    public void testCorrectLength() {

        System.out.println(getClass().getSimpleName() + ".testCorrectLength");

        final int FIELDS_LENGTH = 14;

        String uuid = "a19598b8-1b67-11ee-be56-0242ac120002";

        LoginResultMessage loginResultMessage = new LoginResultMessage((short) 1, uuid, (long) (Math.random() * 10000000L));

        if (loginResultMessage.getVersion() == (short) 1) {
            assertEquals(loginResultMessage.getLength(), FIELDS_LENGTH + uuid.length());
        }
    }

    @Test
    public void testCorrectContent() {

        System.out.println(getClass().getSimpleName() + ".testCorrectContent");

        String uuid = "741c20f1-e409-4803-89c4-3ebc824e0248";

        LoginResultMessage loginResultMessage1 = new LoginResultMessage((short) 1, uuid, (long) (Math.random() * 100000000000L));
        byte[] loginResultData1 = loginResultMessage1.getData();

        LoginResultMessage loginResultMessage2 = new LoginResultMessage(loginResultData1);

        assertEquals(Arrays.equals(loginResultMessage2.getData(), loginResultData1), true);

        assertEquals(loginResultMessage1.getId(), loginResultMessage2.getId());
        assertEquals(loginResultMessage1.getVersion(), loginResultMessage2.getVersion());
        assertEquals(loginResultMessage1.getUsn(), loginResultMessage2.getUsn());
        assertEquals(loginResultMessage1.getReturnCode(), loginResultMessage2.getReturnCode());
        assertEquals(loginResultMessage1.getUuid(), loginResultMessage2.getUuid());
    }

    @Test
    public void testConstructorThrowException() {

        LoginResultMessage loginResultMessage = new LoginResultMessage((short) 0, "123", 0);
        byte[] loginResultData = loginResultMessage.getData();

        byte[] loginResultDataInvalidLength = new byte[loginResultData.length];
        byte[] loginResultDataInvalidId = new byte[loginResultData.length];
        byte[] loginResultDataInvalidVersion = new byte[loginResultData.length];

        System.arraycopy(loginResultData, 0, loginResultDataInvalidLength, 0, loginResultData.length);
        System.arraycopy(loginResultData, 0, loginResultDataInvalidId, 0, loginResultData.length);
        System.arraycopy(loginResultData, 0, loginResultDataInvalidVersion, 0, loginResultData.length);

        loginResultDataInvalidLength[0] = 0x00;
        loginResultDataInvalidLength[1] = 0x01;

        loginResultDataInvalidId[2] = 0x00;
        loginResultDataInvalidId[3] = 0x00;

        loginResultDataInvalidVersion[4] = 0x0a;

        assertThrows(IllegalArgumentException.class, () -> new LoginResultMessage(loginResultDataInvalidLength));
        assertThrows(IllegalArgumentException.class, () -> new LoginResultMessage(loginResultDataInvalidId));
        assertThrows(IllegalArgumentException.class, () -> new LoginResultMessage(loginResultDataInvalidVersion));
    }

}
