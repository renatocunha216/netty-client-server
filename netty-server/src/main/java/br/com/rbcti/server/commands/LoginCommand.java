package br.com.rbcti.server.commands;

import java.util.UUID;

import br.com.rbcti.common.Session;
import br.com.rbcti.common.commands.Command;
import br.com.rbcti.common.messages.LoginMessage;
import br.com.rbcti.common.messages.LoginResultMessage;
import br.com.rbcti.common.messages.SimpleMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author Renato Cunha
 *
 */
public class LoginCommand implements Command {

    @Override
    public void execute(ChannelHandlerContext channel, Session session, SimpleMessage message) {

        LoginMessage loginMessage = (LoginMessage) message;

        //TODO: calls business rules
        LoginResultMessage response = new LoginResultMessage((short) 1, UUID.randomUUID().toString(), loginMessage.getUsn());

        channel.write(response);
        channel.flush();
    }
}
