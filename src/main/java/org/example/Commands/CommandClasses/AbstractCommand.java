package org.example.Commands.CommandClasses;

import lombok.Getter;
import lombok.Setter;
import org.example.OwnExeptions.InvalidCountOfArgumentException;
import org.example.Utils.DataBaseManager;
import org.example.Worker.Organization;
import org.example.Worker.Worker;

import java.io.IOException;
import java.io.Serializable;


public abstract class AbstractCommand implements Serializable {


    @Getter
    protected String command;
    @Getter
    protected String helpText;
    protected boolean NeedAnStr = false;
    protected boolean NeedAnObject = false;
    protected boolean NeedWorker = false;
    protected boolean NeedOrganization = false;
    protected Worker worker = new Worker();
    @Setter
    @Getter
    protected Organization organization = null;
    @Getter
    @Setter
    protected String string = "";
    protected String user;

    public String getUserName() {
        return user;
    }

    public void setUserName(String userName) {
        user = userName;
    }

    public Worker getWorker() {
        return worker;
    }

    public void setWorker(Worker worker) {
        this.worker = worker;
    }

    public boolean isNeedWorker() {
        return NeedWorker;
    }

    public boolean isNeedOrganization() {
        return NeedOrganization;
    }

    public boolean isNeedAnStr() {
        return NeedAnStr;
    }

    public boolean CheckAndSetWorker(Object o) {
        try {
            this.worker = (Worker) o;
            return true;
        } catch (ClassFormatError e) {
            return false;
        }
    }

    public boolean getObjectExecute() {
        return NeedAnObject;
    }

    public String execute(DataBaseManager dataBaseManager, String arg) throws IOException, InvalidCountOfArgumentException {
        return execute();

    }

    public String execute() {
        return "Такая команда не существует";
    }

    public String execute(String s) {
        return "команды не существует";
    }

    public String toPrint() {
        return command + " " + helpText;
    }

    @Override
    public String toString() {
        return command;
    }

}
