package br.com.rbcti.server.handlers;

import java.util.HashMap;
import java.util.Map;

import br.com.rbcti.common.Session;

public class UserSession implements Session {

    private Map<String, Object>properties;

    public UserSession() {
        this.properties = new HashMap<String, Object>();
    }

    public void addProperty(String key, Object value) {
        this.properties.put(key, value);
    }

    public Object getProperty(String key) {
        return this.properties.get(key);
    }

    public void removeProperty(String key) {
        this.properties.remove(key);
    }

}
