package org.example.Utils;

import lombok.extern.log4j.Log4j2;
import org.example.Commands.CommandClasses.AbstractCommand;
import org.example.Commands.CommandManager;
import org.example.Commands.RegCommand.Login;
import org.example.Commands.RegCommand.Register;
import org.example.OwnExeptions.InvalidCountOfArgumentException;
import org.example.Utils.CommonUtils.Serialization;
import org.example.Worker.Organization;
import org.example.Worker.Worker;

import java.io.IOException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.sql.SQLException;

@Log4j2
public class ServerHelper extends Thread {

    Object o = null;
    private DatagramChannel datagramChannel;
    private SocketAddress socketAddress;
    private DataBaseManager dataBaseManager;
    private ByteBuffer byteBuffer;
    private boolean UserLog = false;
    private CommandManager commandManager = new CommandManager(dataBaseManager);


    public ServerHelper(DatagramChannel datagramChannel, SocketAddress socketAddress,
                        DataBaseManager dataBaseManager, ByteBuffer byteBuffer) {
        this.datagramChannel = datagramChannel;
        this.socketAddress = socketAddress;
        this.dataBaseManager = dataBaseManager;
        this.byteBuffer = byteBuffer;

    }

    @Override
    public void run() {
        boolean a = false;

        if (!dataBaseManager.CheckDB()) {
            try {
                datagramChannel.send(ByteBuffer.wrap(("База данных временно не доступна. Просим прощения за неудобства.").getBytes()), socketAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        o = new Serialization().DeserializeObject(byteBuffer.array());
        String s = o.toString();

        commandManager.setServerDatagramChannel(datagramChannel);
        commandManager.setSocketAddress(socketAddress);


        System.out.println("Полученно [" + s + "] от " + socketAddress);

        log.debug(dataBaseManager.getUSER());
        if (o.getClass().getName().contains(".Login")) {
            Login register = (Login) o;
            String login = register.getUserName();
            String pass = register.getPassword();

            try {
                if (dataBaseManager.login(login, pass)) {
                    datagramChannel.send(ByteBuffer.wrap("Access".getBytes()), socketAddress);

                } else {
                    datagramChannel.send(ByteBuffer.wrap("Не верный логин или пароль.".getBytes()), socketAddress);
                }
            } catch (IOException | SQLException e) {
                e.printStackTrace();
            }
            return;
        }


        //Если пользователь хочет зарегистророваться
        if (o.getClass().getName().contains(".Register")) {
            Register register = (Register) o;
            String login = register.getUserName();
            String pass = register.getPassword();

            if (dataBaseManager.AddUser(login, pass)) {
                try {
                    datagramChannel.send(ByteBuffer.wrap(("Пользователь был добавлен в дазу данных. Войдите в систему. \n" +
                            "Ваш логин: " + login + ", и пароль: " + pass + ".").getBytes()), socketAddress);


                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    datagramChannel.send(ByteBuffer.wrap(("Не удалось добавить пользователя." +
                            " Логин занят, содержит недопустимые символы или их последовательность.").getBytes()), socketAddress);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return;
        }


        if (o.getClass().getName().contains(".AbstractCommand")) {
            AbstractCommand ac = (AbstractCommand) o;
            if (dataBaseManager.CheckUser(ac.getUserName())) {
                UserLog = true;
            }
        }

        //Если пользователь не авторизован
        if (UserLog) {
            try {
                datagramChannel.send(ByteBuffer.wrap(("Вы не авторизованы. Пожалуйста зайдите под существующим аккаунтом " +
                        "или создайте новый").getBytes()), socketAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        //Если команда не сущестует
        if (!commandManager.Check(s)) {
            try {
                datagramChannel.send(ByteBuffer.wrap(("Команда [" + s + "] не найдена или имеент неверное количество аргументов." +
                        " Для просмотра доступных команд введите help").getBytes()), socketAddress);
                datagramChannel.send(ByteBuffer.wrap("Something goes wrong".getBytes()), socketAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        //Если команды нет
        if (o == null) {
            try {
                datagramChannel.send(ByteBuffer.wrap(("Команда [" + s + "] не найдена или имеент неверное количество аргументов. " +
                        "Для просмотра доступных команд введите help").getBytes()), socketAddress);
                datagramChannel.send(ByteBuffer.wrap("Something goes wrong".getBytes()), socketAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }


        if (commandManager.Check(s) && dataBaseManager.UserAuthorized()) {
            try {
                byteBuffer.clear();
                socketAddress = datagramChannel.receive(byteBuffer);
                byteBuffer.flip();
            } catch (IOException e) {
                e.printStackTrace();
            }
            AbstractCommand command;
            //todo проходит здесь
            if (socketAddress != null) {
                command = (AbstractCommand) o;
                log.debug("установка setUser");
                dataBaseManager.setUSER(command.getUserName());
                a = true;
            }

            command = (AbstractCommand) o;
            //todo должно ли setUser
            //dataBaseManager.setUSER(command.getUserName());
            if ((command.getObjectExecute() || o.getClass().getName().contains(".Worker")) && a) {
                if (command.isNeedWorker()) {
                    Worker worker = new Serialization().DeserializeObject(byteBuffer.array());
                    command.setWorker(worker);
                }
                if (command.isNeedOrganization()) {
                    Organization organization = new Serialization().DeserializeObject(byteBuffer.array());
                    command.setOrganization(organization);
                }
            } else {
                if (a && !command.getObjectExecute())
                    if (command.isNeedAnStr()) {
                        o = new Serialization().DeserializeObject(byteBuffer.array());
                        String str = (String) o;
                        command.setString(str);
                    }
            }

            //выполнение команды и отправка результата
            if (!command.getCommand().equals("exit")) {
                try {
                    commandManager.printToClient(new CommandResult(commandManager).sendResult(command, dataBaseManager));
                } catch (IOException | InvalidCountOfArgumentException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}