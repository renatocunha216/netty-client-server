package br.com.rbcti.common.messages;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.testng.annotations.Test;

/**
 * Unit test for the LogoutMessage class<br>
 *
 * @author Renato Cunha
 * @see LogoutMessage
 *
 */
public class LogoutMessageTest {

    @Test
    public void testCorrectLength() {

        System.out.println(getClass().getSimpleName() + ".testCorrectLength");

        LogoutMessage logoutMessage = new LogoutMessage((long) (Math.random() * 10000000L));

        if (logoutMessage.getVersion() == (short) 1) {
            assertEquals(logoutMessage.getLength(), 13);
        }
    }

    @Test
    public void testCorrectContent1() {

        System.out.println(getClass().getSimpleName() + ".testCorrectContent1");

        LogoutMessage logoutMessage = new LogoutMessage(Long.MAX_VALUE);

        // Correct content
        assertEquals(Arrays.equals(logoutMessage.getData(),
                new byte[] { (byte) 0x00, (byte) 0x0B, (byte) 0x00, (byte) 0x06, (byte) 0x01, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }),
                true);
    }

    @Test
    public void testCorrectContent2() {

        System.out.println(getClass().getSimpleName() + ".testCorrectContent2");

        long usn = (long) (Math.random() * 1000000000000000L);

        LogoutMessage logoutMessage1 = new LogoutMessage(usn);
        byte[] logoutData1 = logoutMessage1.getData();

        LogoutMessage logoutMessage2 = new LogoutMessage(logoutData1);

        assertEquals(Arrays.equals(logoutMessage2.getData(), logoutData1), true);

        assertEquals(logoutMessage1.getId(), logoutMessage2.getId());
        assertEquals(logoutMessage1.getVersion(), logoutMessage2.getVersion());
        assertEquals(logoutMessage1.getUsn(), logoutMessage2.getUsn());
    }

}
