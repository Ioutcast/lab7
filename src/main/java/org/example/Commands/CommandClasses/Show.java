package org.example.Commands.CommandClasses;

import org.example.Utils.DataBaseManager;
import org.example.Worker.Worker;

import java.util.*;

public class Show extends AbstractCommand {
    public Show() {
        command = "show";
        helpText = "вывести в стандартный поток вывода все элементы коллекции в строковом представлении";
        NeedAnStr = false;

    }

    protected static LinkedHashMap<Integer, Worker> SortCollection(LinkedHashMap<Integer, Worker> collection) {
        Comparator<Worker> workerComparator = new Comparator<Worker>() {
            @Override
            public int compare(Worker s, Worker s1) {
                return s.getName().compareTo(s1.getName());
            }
        };
        List<Worker> workers = new ArrayList<>();
        for (Map.Entry<Integer, Worker> workerEntry : collection.entrySet()) {
            workers.add(workerEntry.getValue());
        }
        LinkedHashMap<Integer, Worker> linkedHashMap = new LinkedHashMap<Integer, Worker>();
        Collections.sort(workers, workerComparator);
        for (Worker worker : workers) {
            linkedHashMap.put(worker.getId(), worker);
        }
        collection = linkedHashMap;

        return collection;
    }

    @Override
    public String execute(DataBaseManager dataBaseManager, String arg) {
        dataBaseManager.setCollection(SortCollection(dataBaseManager.getMapColletion()));
        String ShowCollection = "";
        String ShowCollection1 = "";
        if (!dataBaseManager.getMapColletion().isEmpty()) {
            for (Map.Entry<Integer, Worker> entry : dataBaseManager.getMapColletion().entrySet()) {

                String workerUser = entry.getValue().getUserName().trim();

                System.out.println("if " + workerUser.equals(user));

                if (workerUser.equals(user)) {
                    ShowCollection = ShowCollection + entry.getKey() + " " + (entry.getValue().toWorkerString()) + "\n";
                } else {
                    ShowCollection1 = ShowCollection1 + entry.getKey() + " " + entry.getValue().toString();
                }

            }
        } else {
            return ("Collection is empty");
        }
        return (ShowCollection + ShowCollection1);

    }

//    private String show(LinkedHashMap<Integer, Worker> collection) {
//        SortCollection(collection);
//        String s = "";
//        if (collection.size() != 0) {
//            s = s + "Вывод колекции:" + "\n";
//            for (Map.Entry<Integer, Worker> entry : collection.entrySet()) {
//                s = s + "Ключ " + entry.getKey() + " Элемент коллекции " + entry.getValue().toString() + "\n";
//            }
//        } else s = "Размер коллекции == 0";
//        return s;
//    }
}
