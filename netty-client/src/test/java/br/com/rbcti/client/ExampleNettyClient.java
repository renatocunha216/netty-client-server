package br.com.rbcti.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.rbcti.common.messages.LoginMessage;
import br.com.rbcti.common.messages.LoginResultMessage;

public class ExampleNettyClient {

    private static final Logger LOGGER = LogManager.getLogger(ExampleNettyClient.class);
    private static long usn = 1;

    public static void main(String[] args) {

        NettyClientTest client = new NettyClientTest("127.0.0.1", 10079);

        try {
            client.start();
            LOGGER.info("Connected.");

            LoginMessage login = new LoginMessage("user1", "password#123", usn);

            LoginResultMessage result = client.loginRequest(login);

            LOGGER.info("::{}", result);

            if (LoginResultMessage.LOGIN_OK == result.getReturnCode()) {
                LOGGER.info("Successful login!", result);

            } else {
                LOGGER.info("Login failed.", result);
            }


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
