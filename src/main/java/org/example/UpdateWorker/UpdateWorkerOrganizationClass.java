package org.example.UpdateWorker;

import org.example.Worker.Organization;
import org.example.Worker.Worker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;

public class UpdateWorkerOrganizationClass implements Serializable {
    public static void UpdateWorkerOrganization(Worker worker, List<String> arguments, boolean isAuto, BufferedReader reader) throws IOException {
        Organization organization = SetOrganizationDefault.setOrganizationDefault(arguments, isAuto, reader);
        worker.setOrganization(organization);
    }
}
