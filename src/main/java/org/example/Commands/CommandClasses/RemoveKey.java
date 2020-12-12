package org.example.Commands.CommandClasses;


import org.example.Utils.DataBaseManager;
import org.example.Worker.Worker;

import java.util.Map;

public class RemoveKey extends AbstractCommand {
    public RemoveKey() {
        command = "remove_key";
        helpText = "удалить элемент из коллекции по его ключу";
        NeedAnStr = true;
    }

    @Override
    public String execute(DataBaseManager dataBaseManager, String arg) {
        if (DataBaseManager.getMapColletion().isEmpty()) return "Коллекция пуста";
        boolean bool = false;
        Integer key = Integer.parseInt(string);

        for (Map.Entry<Integer, Worker> WorkerEntry : DataBaseManager.getMapColletion().entrySet()) {
            if (WorkerEntry.getKey().equals(key)) {
                bool = true;
            }
        }
        if (bool) {
            String msg = DataBaseManager.getMapColletion().get(key).toString();
            DataBaseManager.getMapColletion().remove(key);
            if (dataBaseManager.removeFromDataBase(key, user))
                return ("Рабочий " + msg + " под номер " + key + " был удален.");
            return ("Рабочий " + msg + " под номером " + key + " был удален.");

        } else {
            return ("Такого рабочего не существует.");
        }
    }


}
