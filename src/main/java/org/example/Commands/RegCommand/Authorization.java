package org.example.Commands.RegCommand;

import java.io.Serializable;

public abstract class Authorization implements Serializable {

    String command;
    String helpText;

    String password;
    String userName;

    public Authorization(String userName, String password) {
        this.password = password;
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }


    @Override
    public String toString() {
        return "request for " + command;
    }
}