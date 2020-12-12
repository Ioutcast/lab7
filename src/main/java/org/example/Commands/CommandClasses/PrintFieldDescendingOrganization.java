package org.example.Commands.CommandClasses;

import org.example.Utils.DataBaseManager;
import org.example.Worker.Organization;
import org.example.Worker.Worker;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;

public class PrintFieldDescendingOrganization extends AbstractCommand {
    public PrintFieldDescendingOrganization() {
        command = "print_field_descending_organization";
        helpText = "вывести значение поля organization в порядке убывания";
        NeedAnStr = false;

    }

    @Override
    public String execute(DataBaseManager dataBaseManager, String arg) {
        String s = "";
        boolean k = true;
        ArrayList<Organization> organizations = new ArrayList();
        for (Map.Entry<Integer, Worker> entry : dataBaseManager.getMapColletion().entrySet()) {
            organizations.add(entry.getValue().getOrganization());
        }
        for (Organization organization : organizations) {
            if (organization == null) {
                k = false;
                s = "Сортировка не выполена как сравнивать organization = null?";
            }
        }
        if (k) {
            Comparator<Organization> comparator = Comparator.comparingLong(Organization::getEmployeesCount);
            ArrayList<Organization> sorted_organizations = (ArrayList<Organization>) organizations.stream().sorted(comparator).collect(Collectors.toList());
            for (Organization organization : sorted_organizations) {
                s = s + organization.toString();
            }
        }
        return s;
    }


}
