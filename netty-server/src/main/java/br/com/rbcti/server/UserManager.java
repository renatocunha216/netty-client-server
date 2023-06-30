package br.com.rbcti.server;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserManager {

    private Map<String,User>userSessions;

    protected UserManager() {
        userSessions = new ConcurrentHashMap<String, User>();
    }

    public void addUser(User user) {
        userSessions.put(user.getUuid(), user);
    }

//    public void removeUserByChannelId(Integer channelId) {
//        User user = usuariosLogados.get(channelId);
//        if(user != null) {
//            usuariosLogados.remove(user.getChannelId());
//            sessoesUsuarios.remove(user.getUuid());
//        }
//    }

//    public User getUserByChannelId(Integer channelId) {
//        return usuariosLogados.get(channelId);
//    }

    public User getUserByUUID(String uuid) {
        return userSessions.get(uuid);
    }

}