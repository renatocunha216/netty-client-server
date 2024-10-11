# netty-client-server
[![en](https://github.com/renatocunha216/common/blob/main/images/lang-en.svg?raw=true)](https://github.com/renatocunha216/netty-client-server/blob/netty-client-server-spring-mybatis/README.en.md)
[![pt-br](https://github.com/renatocunha216/common/blob/main/images/lang-pt-br.svg?raw=true)](https://github.com/renatocunha216/netty-client-server/blob/netty-client-server-spring-mybatis/README.md)

Example of using the [Netty](https://netty.io/) framework in a client server application with TCP protocol.

### Application protocol

The application protocol is a binary protocol where the first bytes of protocol messages define the total size of the message.<br>
This pattern is implemented by the Netty framework through the **LengthFieldBasedFrameDecoder** class.<br>

In addition to the length field, the protocol in this example uses the following pattern for all messages.<br>

| Position | Field                    | Length                  |
|----------|--------------------------|-------------------------|
|  0       |  Length                  | 2 bytes                 |
|  1       |  Message Id              | 2 bytes                 |
|  2       |  Message Version         | 1 byte                  |
|  3       |  Unique sequence number  | 8 bytes                 |
|  4       |  Data (Payload)          | N bytes                 |


### Example of use

Run the NettyServerTest server.<br>

The code below runs a client application on the **NettyServerTest** server and sends messages to the server.

```java
    ExampleNettyClient.java
    
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
                client.sendFile("c:\\temp\\data.rar", usn++);
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
```
<br>

### Branches

- **[netty-client-server-spring-mybatis](https://github.com/renatocunha216/netty-client-server/tree/netty-client-server-spring-mybatis)**<br>
Integration with Spring and MyBatis frameworks.<br>



### Banco de Dados 

```
  CREATE TABLE "T_USER"
   (    "USER_ID" NUMBER(19,0) NOT NULL ENABLE,
        "NAME" VARCHAR2(80 BYTE) NOT NULL ENABLE,
        "PASSWORD" VARCHAR2(80 BYTE) NOT NULL ENABLE,
        CONSTRAINT "T_USER_PK" PRIMARY KEY ("USER_ID")
   );
```


See another example of using Netty with FlatBuffers in the [netty-article-publisher](https://github.com/renatocunha216/netty-article-publisher) project.