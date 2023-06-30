package br.com.rbcti.common;

public interface Session {

    public void addProperty(String key, Object value);

    public Object getProperty(String key);

    public void removeProperty(String key);

}
