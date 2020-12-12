package org.example.Commands.CommandClasses;

import org.example.OwnExeptions.InvalidCountOfArgumentException;
import org.example.Utils.DataBaseManager;

import java.io.IOException;


public class Exit extends AbstractCommand {

    public Exit() {
        command = "exit";
        helpText = ": Завершение работы программы";

        NeedAnStr = false;
    }

    public String execute(DataBaseManager dataBaseManager, String arg) throws IOException, InvalidCountOfArgumentException {

        System.exit(-1);
        return null;
    }

}