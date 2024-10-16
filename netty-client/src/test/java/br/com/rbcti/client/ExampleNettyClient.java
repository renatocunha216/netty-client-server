package br.com.rbcti.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.rbcti.common.messages.LoginMessage;
import br.com.rbcti.common.messages.LoginResultMessage;
import br.com.rbcti.common.messages.LogoutMessage;

/**
 *
 * @author Renato Cunha
 *
 */
public class ExampleNettyClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleNettyClient.class);
    private static long usn = 1;

    public static void main(String[] args) {

        NettyClientTest client = new NettyClientTest("127.0.0.1", 10079);

        try {
            client.start();
            LOGGER.info("Connected.");

            LoginMessage login = new LoginMessage("user1", "password#123", usn++);

            LoginResultMessage result = client.loginRequest(login);

            LOGGER.info("::{}", result);

            if (LoginResultMessage.LOGIN_OK == result.getReturnCode()) {
                LOGGER.info("Successful login! ", result);

                LOGGER.info("Sending a file to the server.");
                //client.sendFile("c:\\temp\\data.rar", usn++);
                //client.sendFile("c:\\temp\\cnh.pdf", usn++);

            } else {
                LOGGER.info("Login failed.", result);
            }

            Thread.sleep(1000);

            LOGGER.info("Logout request.");
            client.logoutRequest(new LogoutMessage(usn++));

            Thread.sleep(2000);

        } catch(Exception e) {
            e.printStackTrace();

        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        LOGGER.info("Finished example application.");
    }

}
