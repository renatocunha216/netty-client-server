package br.com.rbcti.server.commands;

import static br.com.rbcti.common.messages.LoginResultMessage.LOGIN_NOK;
import static br.com.rbcti.common.messages.LoginResultMessage.LOGIN_OK;

import java.util.UUID;

import br.com.rbcti.common.Session;
import br.com.rbcti.common.commands.Command;
import br.com.rbcti.common.messages.LoginMessage;
import br.com.rbcti.common.messages.LoginResultMessage;
import br.com.rbcti.common.messages.SimpleMessage;
import br.com.rbcti.server.ServerManager;
import br.com.rbcti.server.User;
import br.com.rbcti.server.UserManager;
import br.com.rbcti.server.handlers.UserSession;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;;

/**
 *
 * @author Renato Cunha
 *
 */
public class LoginCommand implements Command {

    @Override
    public void execute(ChannelHandlerContext ctx, Session session, SimpleMessage message) {

        SimpleMessage response = null;

        LoginMessage loginMessage = (LoginMessage) message;

        //TODO: calls business rules
        if ("user1".equals(loginMessage.getUser()) && "password#123".equals(loginMessage.getPassword())) {

            String uuid = UUID.randomUUID().toString();

            User user = new User();
            user.setName(loginMessage.getUser());
            user.setUuid(uuid);
            user.setChannel(ctx.channel());

            UserManager userManager = ServerManager.getInstance().getUserManager();
            userManager.addUser(user);

            UserSession userSession = new UserSession();
            userSession.addProperty("userData", user);
            userSession.addProperty("lastAccess", Long.valueOf(System.currentTimeMillis()));

            ctx.channel().attr(AttributeKey.valueOf("userSession")).set(userSession);

            response = new LoginResultMessage(LOGIN_OK, uuid, loginMessage.getUsn());

        } else {
            response = new LoginResultMessage(LOGIN_NOK, null, loginMessage.getUsn());
            try {
                // avoids brute force attack
                Thread.sleep((long)(Math.random() * 1000L) + 1000L);
            } catch (InterruptedException e) {
            }
        }

        ctx.writeAndFlush(response);
    }
}
