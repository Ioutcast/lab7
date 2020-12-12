package org.example.Commands.CommandClasses;

import org.example.Utils.DataBaseManager;
import org.example.Worker.Worker;

import java.util.Map;

public class Insert extends AbstractCommand {
    public Insert() {
        command = "insert";
        helpText = "добавить новый элемент с задданным ключом";
        NeedAnStr = true;
        NeedAnObject = true;
        NeedWorker = true;
    }


    @Override
    public String execute(DataBaseManager dataBaseManager, String arg) {
        String s = "";

        for (Map.Entry<Integer, Worker> workerEntry : dataBaseManager.getMapColletion().entrySet()) {
            if (workerEntry.getKey().equals(Integer.parseInt(arg))) {
                return "Такой элемент уже есть";
            }
        }
        dataBaseManager.getMapColletion().put(Integer.parseInt(arg), worker);
        dataBaseManager.addToDataBase(worker);
        return "Добавлен новый элемент";
    }

}
