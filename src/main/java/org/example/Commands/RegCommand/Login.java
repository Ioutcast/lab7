package org.example.Commands.RegCommand;


public class Login extends Authorization {
    public Login(String userName, String password) {
        super(userName, password);
        command = "login";
        helpText = " : Вход зарегистрированного пользователя";

    }
}