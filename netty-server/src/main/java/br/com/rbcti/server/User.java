package br.com.rbcti.server;

import io.netty.channel.Channel;

import java.io.Serializable;


public class User implements Serializable {

    private static final long serialVersionUID = -1516732029276485216L;

    private String name;
    private String uuid;
    private Channel channel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "User [name=" + name + ", uuid=" + uuid + "]";
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

}
