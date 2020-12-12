package org.example.Commands;


import lombok.Getter;
import lombok.Setter;
import org.example.Commands.CommandClasses.*;
import org.example.Utils.DataBaseManager;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.HashMap;
import java.util.Map;

public class CommandManager {
    private static final Map<String, AbstractCommand> commands = new HashMap<>();
    public static CommandManager instance;

    @Getter
    private static HashMap<String, AbstractCommand> availableCommands;

    private DatagramChannel serverDatagramChannel;
    @Getter
    @Setter
    private SocketAddress socketAddress;

    DataBaseManager dataBaseManager;

    public CommandManager() {
        availableCommands = new HashMap<>();
        availableCommands.put(new Help().getCommand(), new Help());
        availableCommands.put(new Info().getCommand(), new Info());
        availableCommands.put(new Show().getCommand(), new Show());
        availableCommands.put(new Clear().getCommand(), new Clear());
        availableCommands.put(new Insert().getCommand(), new Insert());
        // availableCommands.put(new Save().getCommand(), new Save());
        availableCommands.put(new Update().getCommand(), new Update());
        availableCommands.put(new RemoveKey().getCommand(), new RemoveKey());
        availableCommands.put(new RemoveGreaterKey().getCommand(), new RemoveGreaterKey());
        availableCommands.put(new RemoveLower().getCommand(), new RemoveLower());
        availableCommands.put(new RemoveLowerKey().getCommand(), new RemoveLowerKey());
        availableCommands.put(new PrintFieldDescendingOrganization().getCommand(), new PrintFieldDescendingOrganization());
        availableCommands.put(new FilterGreaterThanOrganization().getCommand(), new FilterGreaterThanOrganization());
        availableCommands.put(new ExecuteScript().getCommand(), new ExecuteScript());
        availableCommands.put(new Exit().getCommand(), new Exit());
    }

    public CommandManager(DataBaseManager dataBaseManager) {
        this.dataBaseManager = dataBaseManager;
        availableCommands = new HashMap<>();
        availableCommands.put(new Help().getCommand(), new Help());
        availableCommands.put(new Info().getCommand(), new Info());
        availableCommands.put(new Show().getCommand(), new Show());
        availableCommands.put(new Clear().getCommand(), new Clear());
        availableCommands.put(new Insert().getCommand(), new Insert());
        // availableCommands.put(new Save().getCommand(), new Save());
        availableCommands.put(new Update().getCommand(), new Update());
        availableCommands.put(new RemoveKey().getCommand(), new RemoveKey());
        availableCommands.put(new RemoveGreaterKey().getCommand(), new RemoveGreaterKey());
        availableCommands.put(new RemoveLower().getCommand(), new RemoveLower());
        availableCommands.put(new RemoveLowerKey().getCommand(), new RemoveLowerKey());
        availableCommands.put(new PrintFieldDescendingOrganization().getCommand(), new PrintFieldDescendingOrganization());
        availableCommands.put(new FilterGreaterThanOrganization().getCommand(), new FilterGreaterThanOrganization());
        availableCommands.put(new ExecuteScript().getCommand(), new ExecuteScript());
        availableCommands.put(new Exit().getCommand(), new Exit());
        //availableCommands.put(new Register().getCommand(), new Register());
        //availableCommands.put(new Login().getCommand(), new Login());
    }

    public DatagramChannel getServerDatagramChannel() {
        return serverDatagramChannel;
    }

    public void setServerDatagramChannel(DatagramChannel datagramChannel) {
        serverDatagramChannel = datagramChannel;
    }

    //Позволяет проверить наличие команды в списке доступных команд
    public boolean Check(String str) {

        if (str.equals(null)) {
            return false;
        }

        boolean b = false;
        String[] strings = str.split(" ");
        if (strings.length > 2) return false;
        if (strings.length == 2) {
            if (!CheckINT(strings[1])) return false;
        }

        for (Map.Entry<String, AbstractCommand> abs : availableCommands.entrySet()) {
            if (abs.getKey().equals(str)) b = true;
        }
        return b;
    }

    public AbstractCommand getCommand(String str) {
        try {
            return availableCommands.get(str);
        } catch (NullPointerException e) {
            String mes = "команды не существует";
            System.out.println(mes);
            return new AbstractCommand() {
                @Override
                public String execute(String s) {
                    return super.execute(s);
                }
            };
        }
    }


    //отправка клиенту текстовое сообщение
    public void printToClient(String line) {
        try {
            ByteBuffer buffer = ByteBuffer.wrap((line.getBytes()));
            getServerDatagramChannel().send(buffer, getSocketAddress());
        } catch (IOException | NullPointerException e) {

        }
    }

    private boolean CheckINT(String str) {
        try {
            Integer abs = Integer.valueOf(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
