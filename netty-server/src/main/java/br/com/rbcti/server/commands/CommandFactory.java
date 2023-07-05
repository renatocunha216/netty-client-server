package br.com.rbcti.server.commands;

import java.util.HashMap;
import java.util.Map;

import br.com.rbcti.common.commands.Command;
import br.com.rbcti.common.messages.Messages;

/**
 *
 * @author Renato Cunha
 *
 */
public class CommandFactory {

    private static Map<Integer, Command>registeredCommands;

    static {
        registeredCommands = new HashMap<Integer, Command>();
        //registeredCommands.put(Integer.valueOf(Messages.KEPP_ALIVE), new KeepAliveCommand());

        ReceiveFileCommand receiveFileCommand = new ReceiveFileCommand();
        registeredCommands.put(Integer.valueOf(Messages.START_SEND_FILE), receiveFileCommand);
        registeredCommands.put(Integer.valueOf(Messages.SEND_DATA_FILE), receiveFileCommand);
        registeredCommands.put(Integer.valueOf(Messages.END_SEND_FILE), receiveFileCommand);

        registeredCommands.put(Integer.valueOf(Messages.LOGIN), new LoginCommand());
        registeredCommands.put(Integer.valueOf(Messages.LOGOUT), new LogoutCommand());
    }

    public static Command getCommand(int messageId) {
        return registeredCommands.get(Integer.valueOf(messageId));
    }

}