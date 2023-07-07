# netty-client-server

Exemplo de uso do framawork Netty em uma aplicação cliente servidor com o protocolo TCP.


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
