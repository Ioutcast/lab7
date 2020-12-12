package org.example.Commands.RegCommand;

public class Register extends Authorization {
    public Register(String userName, String password) {
        super(userName, password);
        command = "register";
        helpText = " : Регистрация пользователя";
    }

}