package br.com.rbcti.server.commands;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import br.com.rbcti.common.Session;
import br.com.rbcti.common.commands.Command;
import br.com.rbcti.common.messages.SimpleMessage;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 * @author Renato Cunha
 *
 */
public class ReceiveFileCommand implements Command {

    private static final Logger LOGGER = LogManager.getLogger(ReceiveFileCommand.class);

    private static final String PATH = "C:\\temp\\testeNetty\\server\\";

    @Override
    public void execute(ChannelHandlerContext channel, Session session, SimpleMessage message) {
        // TODO: to implement
    }

    private void writeFile(String nomeArquivo, byte[] dados) throws Exception {
        File file  = new File(PATH + nomeArquivo + ".tmp");
        OutputStream output = new BufferedOutputStream(new FileOutputStream(file, true));
        output.write(dados);
        output.close();
    }

}
