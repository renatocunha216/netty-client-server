package br.com.rbcti.server.commands;

import static br.com.rbcti.common.messages.LoginResultMessage.LOGIN_NOK;
import static br.com.rbcti.common.messages.LoginResultMessage.LOGIN_OK;

import java.util.UUID;

import br.com.rbcti.common.Session;
import br.com.rbcti.common.commands.Command;
import br.com.rbcti.common.messages.LoginMessage;
import br.com.rbcti.common.messages.LoginResultMessage;
import br.com.rbcti.common.messages.SimpleMessage;
import io.netty.channel.ChannelHandlerContext;;

/**
 *
 * @author Renato Cunha
 *
 */
public class LoginCommand implements Command {

    @Override
    public void execute(ChannelHandlerContext channel, Session session, SimpleMessage message) {

        SimpleMessage response = null;

        LoginMessage loginMessage = (LoginMessage) message;

        //TODO: calls business rules
        if ("user1".equals(loginMessage.getUser()) && "password#123".equals(loginMessage.getPassword())) {
            response = new LoginResultMessage(LOGIN_OK, UUID.randomUUID().toString(), loginMessage.getUsn());

        } else {
            response = new LoginResultMessage(LOGIN_NOK, null, loginMessage.getUsn());
            try {
                // avoids brute force attack
                Thread.sleep((long)(Math.random() * 1000L) + 1000L);
            } catch (InterruptedException e) {
            }
        }

        channel.write(response);
        channel.flush();
    }
}
