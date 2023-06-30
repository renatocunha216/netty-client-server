package br.com.rbcti.server.commands;

import br.com.rbcti.common.Session;
import br.com.rbcti.common.commands.Command;
import br.com.rbcti.common.messages.SimpleMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author Renato Cunha
 *
 */
public class LogoutCommand implements Command {

    @Override
    public void execute(ChannelHandlerContext channel, Session session, SimpleMessage message) {
        //session.removeProperty("user");
        channel.close();
    }

}