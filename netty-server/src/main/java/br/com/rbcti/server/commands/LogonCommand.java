package br.com.rbcti.server.commands;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.rbcti.common.Session;
import br.com.rbcti.common.commands.Command;
import br.com.rbcti.common.messages.SimpleMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author Renato Cunha
 *
 */
public class LogonCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(LogonCommand.class);

    @Override
    public void execute(ChannelHandlerContext channel, Session session, SimpleMessage message) {
        // TODO: to implement
    }
}
