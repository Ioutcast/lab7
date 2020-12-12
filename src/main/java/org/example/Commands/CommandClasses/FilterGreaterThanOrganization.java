package org.example.Commands.CommandClasses;

import org.example.Utils.DataBaseManager;
import org.example.Worker.Worker;

import java.util.Map;

public class FilterGreaterThanOrganization extends AbstractCommand {
    public FilterGreaterThanOrganization() {
        command = "filter_greater_than_organization";
        helpText = "вывести элементы значение поля organization которых больше заданного";
        NeedAnStr = false;
        NeedAnObject = true;
        NeedOrganization = true;
    }


    @Override
    public String execute(DataBaseManager dataBaseManager, String arg) {
        String s = "";
        if (dataBaseManager.getMapColletion().size() != 0) {
            if (organization == null) {
                s = "null organization не сравниваем, введите команду занов";
            } else {
                System.out.println("элементы, значение поля organization которых больше заданного:");
                for (Map.Entry<Integer, Worker> entry : dataBaseManager.getMapColletion().entrySet()) {
                    if (entry.getValue().getOrganization().compareTo(organization) > 0) {
                        s = s + "Ключ " + entry.getKey() + " Элемент коллекции " + entry.getValue().toString() + "\n";
                    }
                }
            }
        } else s = "Размер коллекции == 0";
        return s;
    }


}
