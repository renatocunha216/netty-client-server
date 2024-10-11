package br.com.rbcti.server.commands;

import static br.com.rbcti.common.messages.LoginResultMessage.LOGIN_NOK;
import static br.com.rbcti.common.messages.LoginResultMessage.LOGIN_OK;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import br.com.rbcti.common.Session;
import br.com.rbcti.common.commands.Command;
import br.com.rbcti.common.messages.LoginMessage;
import br.com.rbcti.common.messages.LoginResultMessage;
import br.com.rbcti.common.messages.SimpleMessage;
import br.com.rbcti.server.NettyServerTest;
import br.com.rbcti.server.ServerManager;
import br.com.rbcti.server.User;
import br.com.rbcti.server.UserManager;
import br.com.rbcti.server.handlers.UserSession;
import br.com.rbcti.server.services.UserService;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;;

/**
 *
 * @author Renato Cunha
 *
 */
public class LoginCommand implements Command {

    private static final Logger LOGGER = LoggerFactory.getLogger(NettyServerTest.class);

    private UserService userService;

    @Override
    public void execute(ChannelHandlerContext ctx, Session session, SimpleMessage message) {

        SimpleMessage response = null;

        LoginMessage loginMessage = (LoginMessage) message;

        br.com.rbcti.common.model.User _user = null;
        try {
            _user = userService.selectUser(loginMessage.getUser());
            LOGGER.debug("::" + _user);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //TODO: calls business rules
        if (_user != null &&  _user.getPassword().equals(loginMessage.getPassword())) {

            String uuid = UUID.randomUUID().toString();

            User user = new User();
            user.setName(_user.getName());
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

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
