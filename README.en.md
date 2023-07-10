# netty-client-server
[![en](https://github.com/renatocunha216/common/blob/main/images/lang-en.svg?raw=true)](https://github.com/renatocunha216/netty-client-server/blob/main/README.en.md)
[![pt-br](https://github.com/renatocunha216/common/blob/main/images/lang-pt-br.svg?raw=true)](https://github.com/renatocunha216/netty-client-server/blob/main/README.md)

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
    
    public static void main(String[] args) {
        
        long usn = 1;

        NettyClientTest client = new NettyClientTest("127.0.0.1", 10079);

        try {
            client.start();
            System.out.println("Connected.");

            LoginMessage login = new LoginMessage("user1", "password#123", usn);
            LoginResultMessage result = client.loginRequest(login);

            System.out.println("result = " + result);

        } catch(Exception e) {
            e.printStackTrace();

        } finally {
            try {
                client.stop();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Finished example application.");
    }
```
