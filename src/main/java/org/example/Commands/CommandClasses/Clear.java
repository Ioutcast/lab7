package org.example.Commands.CommandClasses;

import org.example.Utils.DataBaseManager;
import org.example.Worker.Worker;

import java.util.Map;

public class Clear extends AbstractCommand {
    public Clear() {
        command = "clear";
        helpText = "очистить коллекцию";
        NeedAnStr = false;
    }

    public String execute(DataBaseManager dataBaseManager, String arg) {

        for (Map.Entry<Integer, Worker> dragonEntry : dataBaseManager.getMapColletion().entrySet()) {
            String UserName = dragonEntry.getValue().getUserName().trim();
            if (user.equals(UserName)) {
                dataBaseManager.removeFromDataBase(dragonEntry.getValue().getId(), user);
            }

        }

        return "Коллекция " + dataBaseManager.getMapColletion() + " была очищена.";

    }
}
