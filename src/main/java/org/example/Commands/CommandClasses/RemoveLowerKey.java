package org.example.Commands.CommandClasses;

import org.example.Utils.DataBaseManager;
import org.example.Worker.Worker;

import java.util.Map;

public class RemoveLowerKey extends AbstractCommand {
    public RemoveLowerKey() {
        command = "remove_lower_key";
        helpText = "удалить из коллекции все элементы , ключ которых мешьне, чем заданный ";
        NeedAnStr = true;
    }

    @Override
    public String execute(DataBaseManager dataBaseManager, String arg) {
        String msg = " ";


        if (!dataBaseManager.getMapColletion().isEmpty()) {

            int rlk = Integer.parseInt(arg);
            msg = ("Были удалены: \n");
            for (Map.Entry<Integer, Worker> workerEntry : dataBaseManager.getMapColletion().entrySet()) {
                String userName = workerEntry.getValue().getUserName().trim();

                if ((workerEntry.getKey() < rlk) && userName.equals(user)) {
                    msg = msg + (workerEntry.getValue().getId() + " -> " + workerEntry.getValue().getName() + " \n");
                    dataBaseManager.getMapColletion().remove(workerEntry.getValue().getId());
                    dataBaseManager.removeFromDataBase(workerEntry.getValue().getId(), user);
                }
            }
        } else {
            msg = ("Коллекция пуста.");
        }
        return msg;
    }
}
//        String s = "";
//        if (dataBaseManager.getCollection().size() != 0) {
//            Set<Map.Entry<Integer, Worker>> setOfEntries = dataBaseManager.getCollection().entrySet();
//            Iterator<Map.Entry<Integer, Worker>> iterator = setOfEntries.iterator();
//            while (iterator.hasNext()) {
//                Map.Entry<Integer, Worker> entry = iterator.next();
//                Integer value = entry.getKey();
//                if (value.compareTo(Integer.valueOf(arg)) < 0) {
//                    s = "removeing : " + entry;
//                    iterator.remove();
//                }
//            }
//        } else s = "Размер коллекции == 0";
//        return s;
//    }



