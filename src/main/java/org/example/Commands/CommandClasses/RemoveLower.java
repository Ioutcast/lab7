package org.example.Commands.CommandClasses;

import org.example.Utils.DataBaseManager;
import org.example.Worker.Worker;

import java.util.Map;


public class RemoveLower extends AbstractCommand {
    public RemoveLower() {
        command = "remove_lower";
        helpText = "удалить из коллекции все элементы, меньшие чем заданный";
        NeedAnStr = false;
        NeedAnObject = true;
        NeedWorker = true;
    }

    @Override
    public String execute(DataBaseManager dataBaseManager, String arg) {
        if (DataBaseManager.getMapColletion().isEmpty()) return "Коллекция пуста";
        //System.out.println("Введите данные Группы. Все группы, которые меньше вашей(исходя из логики сравнения), будут удалены.");
        for (Map.Entry<Integer, Worker> workerEntry : DataBaseManager.getMapColletion().entrySet()) {
            String UserName = workerEntry.getValue().getUserName().trim();
            if (workerEntry.getValue().compareTo(worker) > 0 && user.equals(UserName)) {
                dataBaseManager.removeFromDataBase(workerEntry.getValue().getId(), user);
            }

        }

        return null;
    }
}
//        String s = "Никто не постарал";
//        if (dataBaseManager.getCollection().size() != 0) {
//            mapCollection.entrySet().removeIf(entry -> entry.getValue().compareTo(getWorker()) < 0);
//            s = "удаление прошло успешно";
//        } else s = "Размер коллекции == 0";
//        return s;
//    }

