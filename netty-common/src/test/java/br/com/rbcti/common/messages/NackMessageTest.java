package br.com.rbcti.common.messages;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.testng.annotations.Test;

/**
 * Unit test for the NackMessage class<br>
 *
 * @author Renato Cunha
 * @see NackMessage
 *
 */
public class NackMessageTest {

    @Test
    public void testCorrectLength() {

        System.out.println(getClass().getSimpleName() + ".testCorrectLength");

        String message = "Message 1 2 3";

        NackMessage nack = new NackMessage((long) (Math.random() * 10000000L), 1, message);

        if (nack.getVersion() == (short) 1) {
            assertEquals(nack.getLength(), 15 + message.length());
        }
    }

    @Test
    public void testCorrectContentFull() {

        System.out.println(getClass().getSimpleName() + ".testCorrectContent");

        long usn = (long) (Math.random() * 1000000000000000L);
        String message = "Message 1 2 3 " + usn;

        NackMessage nack1 = new NackMessage(usn, 1, message);
        byte[] nackData1 = nack1.getData();

        NackMessage nack2 = new NackMessage(nackData1);

        assertEquals(Arrays.equals(nack2.getData(), nackData1), true);

        assertEquals(nack1.getId(), nack2.getId());
        assertEquals(nack1.getVersion(), nack2.getVersion());
        assertEquals(nack1.getUsn(), nack2.getUsn());
        assertEquals(nack1.getReturnCode(), nack2.getReturnCode());
        assertEquals(nack1.getReturnMessage(), nack2.getReturnMessage());
    }

    @Test
    public void testCorrectContentNoReturnMessage() {

        System.out.println(getClass().getSimpleName() + ".testCorrectContentNoReturnMessage");

        long usn = (long) (Math.random() * 1000000000000000L);

        NackMessage nack1 = new NackMessage(usn, 99);
        byte[] nackData1 = nack1.getData();

        NackMessage nack2 = new NackMessage(nackData1);

        assertEquals(Arrays.equals(nack2.getData(), nackData1), true);

        assertEquals(nack1.getId(), nack2.getId());
        assertEquals(nack1.getVersion(), nack2.getVersion());
        assertEquals(nack1.getUsn(), nack2.getUsn());
        assertEquals(nack1.getReturnCode(), nack2.getReturnCode());
        assertEquals(nack1.getReturnMessage(), nack2.getReturnMessage());
    }

}
