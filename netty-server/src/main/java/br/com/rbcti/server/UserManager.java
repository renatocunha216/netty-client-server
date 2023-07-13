package br.com.rbcti.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import io.netty.channel.Channel;

/**
 *
 * @author Renato Cunha
 *
 */
public class UserManager {

    private Map<String, User> userSessions;

    protected UserManager() {
        userSessions = new ConcurrentHashMap<String, User>();
    }

    public void addUser(User user) {
        userSessions.put(user.getChannel().id().asLongText(), user);
    }

    public void removeUser(Channel channel) {
        userSessions.remove(channel.id().asLongText());
    }

    public User getUser(Channel channel) {
        return userSessions.get(channel.id().asLongText());
    }

}