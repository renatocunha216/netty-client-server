package br.com.rbcti.common.commands;

import br.com.rbcti.common.Session;
import br.com.rbcti.common.messages.SimpleMessage;
import io.netty.channel.ChannelHandlerContext;


public interface Command {

    public void execute(ChannelHandlerContext  ctx, Session session, SimpleMessage message);

}
