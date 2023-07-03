package br.com.rbcti.common.messages;

import static org.testng.Assert.assertEquals;

import java.util.Arrays;

import org.testng.annotations.Test;

/**
 * Unit test for the AckMessage class<br>
 *
 * @author Renato Cunha
 * @see AckMessage
 *
 */
public class AckMessageTest {

    @Test
    public void testCorrectLength() {

        System.out.println(getClass().getSimpleName() + ".testCorrectLength");

        AckMessage ackMessage = new AckMessage((long) (Math.random() * 10000000L));

        if (ackMessage.getVersion() == (short) 1) {
            assertEquals(ackMessage.getLength(), 13);
        }
    }

    @Test
    public void testCorrectContent1() {

        System.out.println(getClass().getSimpleName() + ".testCorrectContent1");

        AckMessage ackMessage = new AckMessage(Long.MAX_VALUE);

        // Correct content
        assertEquals(Arrays.equals(ackMessage.getData(),
                new byte[] { (byte) 0x00, (byte) 0x0B, (byte) 0x00, (byte) 0x02, (byte) 0x01, (byte) 0x7F, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF, (byte) 0xFF }),
                true);
    }

    @Test
    public void testCorrectContent2() {

        System.out.println(getClass().getSimpleName() + ".testCorrectContent2");

        long usn = (long) (Math.random() * 1000000000000000L);

        AckMessage ackMessage1 = new AckMessage(usn);
        byte[] ackData1 = ackMessage1.getData();

        AckMessage ackMessage2 = new AckMessage(ackData1);

        assertEquals(Arrays.equals(ackMessage2.getData(), ackData1), true);

        assertEquals(ackMessage1.getId(), ackMessage2.getId());
        assertEquals(ackMessage1.getVersion(), ackMessage2.getVersion());
        assertEquals(ackMessage1.getUsn(), ackMessage2.getUsn());
    }

}
