package br.com.rbcti.server;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Renato Cunha
 *
 */
public class ServerManager {

    private static final ServerManager instance = new ServerManager();
    private final UserManager userManager = new UserManager();
    private static final Map<String, Object> properties = new HashMap<String, Object>();

    private ServerManager() {
    }

    public static ServerManager getInstance() {
        return instance;
    }

    public Object getProperty(String propertyName) {
        return properties.get(propertyName);
    }

    public void setProperty(String propertyName, Object value) {
        properties.put(propertyName, value);
    }

    public void removeProperty(String propertyName) {
        properties.remove(propertyName);
    }

    public UserManager getUserManager() {
        return userManager;
    }

}
