package br.com.rbcti.server.commands;

import br.com.rbcti.common.Session;
import br.com.rbcti.common.commands.Command;
import br.com.rbcti.common.messages.SimpleMessage;
import br.com.rbcti.server.ServerManager;
import br.com.rbcti.server.UserManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;

/**
 *
 * @author Renato Cunha
 *
 */
public class LogoutCommand implements Command {

    @Override
    public void execute(ChannelHandlerContext ctx, Session session, SimpleMessage message) {

        // LogoutCommand logoutMessage = (LogoutCommand) message;

        if (session != null) {
            // UserSession userSession = (UserSession) session;
            UserManager userManager = ServerManager.getInstance().getUserManager();
            userManager.removeUser(ctx.channel());

            // Remove userSession
            ctx.channel().attr(AttributeKey.valueOf("userSession")).set(null);
        }

        ctx.close();
    }

}