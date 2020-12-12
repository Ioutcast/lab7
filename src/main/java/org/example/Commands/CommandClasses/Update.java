package org.example.Commands.CommandClasses;

import org.example.UpdateWorker.*;
import org.example.Utils.DataBaseManager;
import org.example.Worker.Worker;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Update extends AbstractCommand {
    public Update() {
        command = "update";
        helpText = "обновить значение элемента коллекции, id которого равен заданному";
        NeedAnStr = true;
        NeedAnObject = true;
        NeedWorker = true;
    }

    public String execute(DataBaseManager dataBaseManager, String arg) {

        if (worker.getName().equals(null)) return "Не задан объект";
        if (dataBaseManager.updateElementInDataBase1(worker, user)) {
            return "Рабочий был заменён.";
        } else {
            return ("У вас нет рабочего с таким номером.");
//        boolean bool = true;
//
//        for (Map.Entry<Integer, Worker> workerEntry : dataBaseManager.getCollection().entrySet()) {
//            if (workerEntry.getKey().equals(Integer.parseInt(arg))) {
//                bool = false;
//            }
//        }
//        if (!bool) {
//            String msg = dataBaseManager.getCollection().get(Integer.parseInt(arg)).getName();
//            dataBaseManager.getCollection().put(Integer.parseInt(arg), worker);
//            return msg + " был заменён на " + worker.getName();
//        } else {
//            return ("Элемента с таким номером не существует.");
//        }

        }
    }

    public Worker update(String words, List<String> arguments, boolean isAuto) throws IOException {
        BufferedInputStream bf = new BufferedInputStream(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(bf, StandardCharsets.UTF_8));
        Worker worker = new Worker();
        UpdateWorkerNameClass.UpdateWorkerName(worker, arguments, isAuto, reader);

        UpdateWorkerCoordinatesClass.UpdateWorkerCoordinates(worker, arguments, isAuto, reader);

        UpdateWorkerSalaryClass.UpdateWorkerSalary(worker, arguments, isAuto, reader);

        UpdateWorkerStartDateClass.UpdateWorkerStartDate(worker, arguments, isAuto, reader);

        UpdateWorkerEndDateClass.UpdateWorkerEndDate(worker, arguments, isAuto, reader);

        UpdateWorkerStatusClass.UpdateWorkerStatus(worker, arguments, isAuto, reader);

        UpdateWorkerOrganizationClass.UpdateWorkerOrganization(worker, arguments, isAuto, reader);
        return worker;
    }

    public Worker update(Worker worker1, String words, List<String> arguments, boolean isAuto) throws IOException {
        BufferedInputStream bf = new BufferedInputStream(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(bf, StandardCharsets.UTF_8));
        Worker worker2 = worker1;
        UpdateWorkerNameClass.UpdateWorkerName(worker2, arguments, isAuto, reader);

        UpdateWorkerCoordinatesClass.UpdateWorkerCoordinates(worker2, arguments, isAuto, reader);

        UpdateWorkerSalaryClass.UpdateWorkerSalary(worker2, arguments, isAuto, reader);

        UpdateWorkerStartDateClass.UpdateWorkerStartDate(worker2, arguments, isAuto, reader);

        UpdateWorkerEndDateClass.UpdateWorkerEndDate(worker2, arguments, isAuto, reader);

        UpdateWorkerStatusClass.UpdateWorkerStatus(worker2, arguments, isAuto, reader);

        UpdateWorkerOrganizationClass.UpdateWorkerOrganization(worker2, arguments, isAuto, reader);
        return worker2;
    }
}
