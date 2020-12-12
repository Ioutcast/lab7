package org.example.Utils;

import org.example.Commands.CommandClasses.AbstractCommand;
import org.example.Commands.CommandManager;
import org.example.OwnExeptions.InvalidCountOfArgumentException;

import java.io.IOException;

public class CommandResult extends CommandManager {
    CommandManager commandManager;


    public CommandResult(CommandManager commandManager) {
        this.commandManager = commandManager;
    }

    /**
     * Метод на входе получает Абстракт-команду, и на выходе даёт её результат.
     *
     * @param command
     * @return String: результат команды
     */
    //todo 2020 VAR!!!!!!!!!!!!!!!!!!!!!!!!!
    //todo среда четверг 
    //todo cached thread pool
    public synchronized String sendResult(AbstractCommand command, DataBaseManager dataBaseManager) throws IOException, InvalidCountOfArgumentException {
        dataBaseManager.updateCollectionFromDataBase();

        System.out.println("CR_user = " + dataBaseManager.getUSER());

        if (command.getCommand().equals("exit")) {
            commandManager.getCommand("save").execute("save");
        }

        if (!command.getObjectExecute() && !command.isNeedAnStr()) {
            return command.execute(dataBaseManager, command.getString());
        }
        //Команды, где нужен только аргумент. (Пример: remove_key_lower {key})
        if (!command.getObjectExecute() && command.isNeedAnStr()) {

            if (command.getString() != null) {
                return command.execute(dataBaseManager, command.getString());
            } else {
                if (command.getString() == null) return "Нет строки";
            }

        }

        //Команды, где нужен объект и аргумент. (Пример: remove_lower {element}})
        if (command.getObjectExecute() && !command.isNeedAnStr()) {

            if (command.getWorker() != null) {
                return command.execute(dataBaseManager, command.getString());
            } else {
                if (command.getWorker() == null) return "Нет рабочего";
            }
        }

        //Команды, где нужен объект и аргумент. (Пример: insert {key} + {element}})
        if (command.getObjectExecute() && command.isNeedAnStr()) {

            if (command.getWorker() != null && command.getString() != null) {
                return command.execute(dataBaseManager, command.getString());
            } else {
                if (command.getWorker() == null && command.getString() == null) return "Нет рабочего и строки";
                if (command.getWorker() == null) return "Нет рабочего";
                if (command.getString() == null) return "Нет строки";
            }
        }
        return "Что-то пошло не так.";
    }

}