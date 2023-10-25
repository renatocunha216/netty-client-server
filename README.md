# netty-client-server
[![en](https://github.com/renatocunha216/common/blob/main/images/lang-en.svg?raw=true)](https://github.com/renatocunha216/netty-client-server/blob/main/README.en.md)
[![pt-br](https://github.com/renatocunha216/common/blob/main/images/lang-pt-br.svg?raw=true)](https://github.com/renatocunha216/netty-client-server/blob/main/README.md)

Exemplo de uso do framework [Netty](https://netty.io/) em uma aplicação cliente servidor com o protocolo TCP.

### Protocolo da aplicação

O protocolo da aplicação é um protocolo binário onde os primeiros bytes das mensagens do protocolo definem o tamanho total da mensagem.<br>
Este padrão é implementado pelo framework Netty através da classe **LengthFieldBasedFrameDecoder**.<br>

Além do campo de tamanho o protocolo deste exemplo utiliza o seguinte padrão para todas as mensagens.<br>

| Posição | Campo                    | Tamanho                 |
|---------|--------------------------|-------------------------|
|  0      |  Tamanho                 | 2 bytes                 |
|  1      |  Id da mensagem          | 2 bytes                 |
|  2      |  Versão da mensagem      | 1 byte                  |
|  3      |  Número sequêncial único | 8 bytes                 |
|  4      |  Dados                   | N bytes                 |


### Exemplo de uso

Execute o servidor NettyServerTest.<br>

O código abaixo executa um aplicativo cliente do servidor **NettyServerTest** e envia mensagens para o servidor.<br>


```java
    ExampleNettyClient.java
    
    private static final Logger LOGGER = LogManager.getLogger(ExampleNettyClient.class);
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

Veja outro exemplo do uso do Netty com o FlatBuffers no projeto [netty-article-publisher](https://github.com/renatocunha216/netty-article-publisher).