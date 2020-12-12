package org.example.Commands.CommandClasses;

import org.example.Commands.CommandManager;
import org.example.Utils.DataBaseManager;

import java.util.Map;

public class Help extends AbstractCommand {

    public Help() {
        command = "help";
        helpText = "вывести справку по доступным командам ";
        NeedAnStr = false;

    }

    @Override
    public String execute(DataBaseManager dataBaseManager, String s) {
        String str = "";
        for (Map.Entry<String, AbstractCommand> abs : CommandManager.getAvailableCommands().entrySet()) {
            str = str + abs.getValue().toPrint() + "\n";
        }

        return str;
    }
}
