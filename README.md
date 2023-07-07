# netty-client-server

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
