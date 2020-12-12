package org.example;

import org.example.Commands.CommandClasses.AbstractCommand;
import org.example.Commands.CommandClasses.ExecuteScript;
import org.example.Commands.CommandClasses.Update;
import org.example.Commands.CommandManager;
import org.example.Commands.RegCommand.Authorization;
import org.example.Commands.RegCommand.Login;
import org.example.Commands.RegCommand.Register;
import org.example.UpdateWorker.SetOrganizationDefault;
import org.example.Utils.CommonUtils.Serialization;
import org.example.Worker.Organization;
import org.example.Worker.Worker;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.PortUnreachableException;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.UnresolvedAddressException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/**
 * Приложение Клиента
 */

public class Client2 implements Runnable {

    private static boolean UserLog = false;

    private static String username;
    private static String password;
    private static int port;
    private final DatagramChannel datagramChannel;
    private final Selector selector;
    int i = 1;
    private SocketAddress socketAddress;
    private CommandManager commandManager = new CommandManager();
    private Iterator<String> script_iterator = null;

    public Client2() throws IOException, TransformerException, ParserConfigurationException {
        selector = Selector.open();
        datagramChannel = DatagramChannel.open();
        datagramChannel.configureBlocking(false);
    }

    public static void main(String args[]) throws IOException, ArrayIndexOutOfBoundsException {


        Scanner scanner = new Scanner(System.in);
        String string;
        try {
            string = args[0];
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Вы ввели порт не верно." + "\nВведите порт в виде целого числа");
            string = scanner.nextLine();
        }


        while (!CheckINT(string)) {
            System.out.println("Введите порт в виде целого числа");
            string = scanner.nextLine();
        }

        port = Integer.parseInt(string);


        try {
            Client1 client = new Client1();
            client.connect("localhost", port);
            client.run();
        } catch (IOException | UnresolvedAddressException e) {
        }
    }

    //проверка на число
    private static boolean CheckINT(String str) {
        try {
            Integer abs = Integer.valueOf(str);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void run() {


        try {
            Scanner scanner = new Scanner(System.in);

            datagramChannel.register(selector, SelectionKey.OP_WRITE);
            while (selector.select() > 0) {
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey selectionKey = iterator.next();
                    iterator.remove();

                    //Тут просиходит получение ответа от Сервера.
                    if (selectionKey.isReadable()) {

                        String answer = receiveAnswer();
                        if (!answer.contains("Something goes wrong")) {
                            if (!answer.contains("Access")) {
                                System.out.println(answer);
                            } else {
                                UserLog = true;
                                System.out.println("Вы зашли под пользователем " + username + ".");
                            }

                            datagramChannel.register(selector, SelectionKey.OP_WRITE);
                        }

                        if (answer.equals("Вы не авторизованы. Пожалуйста зайдите под существующим аккаунтом " +
                                "или создайте новый") || answer.equals("Не верный логин или пароль.")) UserLog = false;


                    }
                    if (selectionKey.isWritable()) {
                        datagramChannel.register(selector, SelectionKey.OP_READ);

                        if (UserLog) {

                            try {
                                String string;

                                if (script_iterator != null && script_iterator.hasNext()) {
                                    string = script_iterator.next();
                                } else {
                                    string = scanner.nextLine();
//                                   if  CommandInfo.setXml(new File(string));
                                }
                                String[] strings = string.trim().split("\\s+");
                                AbstractCommand command = null;


                                //Выполнение скрипта
                                if (strings[0].equals("execute_script") && strings.length == 2) {
                                    ExecuteScript execute_script = new ExecuteScript();
                                    try {
                                        execute_script.GetExecute(strings[1]);
                                        script_iterator = execute_script.ScriptCommands().iterator();
                                    } catch (ParserConfigurationException | TransformerException e) {
                                        e.printStackTrace();
                                    }

                                }

                                //Отсеивание неправильных команд
                                if (strings[0].equals("execute_script") && strings.length != 2) {
                                    throw new NumberFormatException();
                                }

                                //Отсеивание неправильных команд
                                if (strings.length > 2 || !commandManager.Check(strings[0])) {
                                    if (!string.contains("execute_script")) sendCommand(string);
                                    //throw new NoCommandException("Такой команды не существует");
                                } else {
                                    command = commandManager.getCommand(strings[0]);
                                    command.setUserName(username);
                                    //Отсеивание неправильных команд
                                    if (strings.length == 0) {
                                        throw new NumberFormatException();
                                    }
                                    //Отсеивание неправильных команд
                                    if (strings.length == 2) {
                                        if (CheckINT(strings[1])) {
                                            if (strings[0].equals("execute_script")) {
                                                throw new NumberFormatException();
                                            } else if (strings[0].equals("remove_any_by_end_date")) {
                                                throw new NumberFormatException();
                                            }
                                        }
                                    }

                                    //Отсеивание неправильных команд
                                    if (strings.length == 2 && !command.isNeedAnStr()) {
                                        throw new NumberFormatException();
                                    }

                                    //Отсеивание неправильных команд
                                    if (strings.length == 1 && command.isNeedAnStr()) {
                                        throw new NumberFormatException();
                                    }

                                    // не нужен объект и аргумент к нему.(Пример Help, Show)
                                    if (!command.getObjectExecute() && !command.isNeedAnStr()) {
                                        sendCommand(command);
                                        continue;
                                    }

                                    //нужен только аргумент. (Пример: RemoveLowerKey {key})
                                    if (!command.getObjectExecute() && command.isNeedAnStr()) {
                                        sendCommand(command, strings[1]);
                                        continue;
                                    }

                                    //нужен объект и аргумент. (Пример: removeLower {element}})
                                    if (command.getObjectExecute() && !command.isNeedAnStr()) {
                                        if (command.isNeedWorker())
                                            sendWorkerCommand(command);
                                        if (command.isNeedOrganization())
                                            sendOrganizationCommand(command);
                                        continue;
                                    }

                                    //нужен объект и аргумент. (Пример: insert {key} + {element}})
                                    if (command.getObjectExecute() && command.isNeedAnStr()) {
                                        sendWorkerCommand(command, strings[1]);
                                    }

                                }
                            } catch (NumberFormatException e) {

                                System.out.println("Неправильный ввод команды");
                                run();
                            }

                        } else {


                            if (i == 1) {
                                System.out.println("Вы не авторизованы.\nВведите Login, чтобы авторизоваться, или Register для создания " +
                                        "нового пользователя.");
                                i++;
                            } else {
                                System.out.println("Вы не авторизованы");
                            }

                            while (true) {
                                String str1 = scanner.nextLine();
                                if (str1.equals("Register") || str1.equals("register")) {
                                    register(scanner);
                                    break;
                                }


                                if (str1.equals("login") || str1.equals("Login")) {
                                    login(scanner);
                                    break;
                                }


                                System.out.println("Вы ввели команду не правильно");
                            }


                        }

                    }
                }
            }
        } catch (PortUnreachableException e) {
            System.err.println("Не удалось получить данные по указанному порту/сервер не доступен. \n" +
                    "Введите cont - чтобы продолжить. Change - чтобы поменять порт");
            Scanner scanner = new Scanner(System.in);
            String str = scanner.nextLine();
            if (str.equals("Change")) {
                System.out.print("Вы можете вводить порт: ");
                str = scanner.nextLine();
                while (!CheckINT(str)) {
                    System.out.println("Введите порт в виде целого числа");
                    str = scanner.nextLine();
                }
                port = Integer.parseInt(str);
            }

            startProgram(port);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Подключение к серверу
    private void connect(String hostname, int port) throws IOException {
        socketAddress = new InetSocketAddress(hostname, port);
        datagramChannel.connect(socketAddress);
        System.out.println("Устанавливаем соединение с " + hostname + " по порту " + port);
    }

    //Получение ответа от сервера
    private String receiveAnswer() throws IOException {
        byte[] bytes = new byte[65536];
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        socketAddress = datagramChannel.receive(buffer);
        String msg = new String(buffer.array());
        return msg;
    }

    private void startProgram(int port) {
        try {
            Client1 client = new Client1();
            client.connect("localhost", port);
            client.run();
        } catch (IOException | UnresolvedAddressException e) {
        }

    }

    //Отправляет команду вместе с объектом
    private void sendWorkerCommand(AbstractCommand command) throws IOException {

        Worker worker = new Update().update(String.valueOf(0), new ArrayList<>(), false);
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(command));
        datagramChannel.send(buffer, socketAddress);
        sendWorker(worker);
    }

    //Отправляет команду вместе с объектом
    private void sendWorkerCommand(AbstractCommand command, String string) throws IOException {
        if (command != null) {
            command.setString(string);
        }
        Worker worker = new Update().update(string, new ArrayList<>(), false);
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(command));
        datagramChannel.send(buffer, socketAddress);
        sendWorker(worker);
    }

    //Отправляет команду вместе с объектом
    private void sendOrganizationCommand(AbstractCommand command) throws IOException {

        BufferedInputStream bf = new BufferedInputStream(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(bf, StandardCharsets.UTF_8));
        Organization organization = SetOrganizationDefault.setOrganizationDefault(new ArrayList<>(), false, reader);
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(command));
        datagramChannel.send(buffer, socketAddress);
        sendOrganization(organization);
    }

    // Отправляет объект на сервер
    private void sendOrganization(Organization organization) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(organization));
        datagramChannel.send(buffer, socketAddress);
        System.out.println("org" + " отправлен");
    }

    //Отправка команды c параметром на сервер
    private void sendCommand(AbstractCommand command, String parameter) throws IOException {
        if (command != null) {
            command.setString(parameter);
        }
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(command));
        datagramChannel.send(buffer, socketAddress);
        sendCommand(parameter);
    }

    //Отправка строки на сервер
    private void sendCommand(String str) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(str));
        datagramChannel.send(buffer, socketAddress);
    }

    //Отправка команды на сервер
    private void sendCommand(AbstractCommand str) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(str));
        datagramChannel.send(buffer, socketAddress);
        if (str.getCommand().equals("exit")) {
            System.out.println("Завершение программы");
            System.exit(0);
        }

    }

    // Отправляет объект на сервер
    private void sendWorker(Worker worker) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(worker));
        datagramChannel.send(buffer, socketAddress);
        System.out.println(worker.getName() + " отправлен");
    }

    private void sendCommand(Authorization authorization) throws IOException {
        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(authorization));
        datagramChannel.send(buffer, socketAddress);
    }


    //Регистрация пользователя
    private void register(Scanner scanner) throws IOException {
        username = "";
        boolean lessThen4 = true;
        boolean withSpaces = true;
        boolean invalidChars = true;
        do {
            System.out.println("Придумайте логин, содержащий не менее 4 символов (допускается использование только английских прописных букв и цифр)");
            username = scanner.nextLine();
            lessThen4 = username.trim().split("\\s+")[0].length() < 4;
            withSpaces = username.trim().split("\\s+").length != 1;
            invalidChars = !username.trim().split("\\s+")[0].matches("[a-z0-9]+");
        } while (lessThen4 || withSpaces || invalidChars);
        password = "";
        lessThen4 = true;
        withSpaces = true;
        invalidChars = true;
        do {
            System.out.println("Придумайте пароль, содержащий не менее 4 (допускается использование только английских прописных букв и цифр)");
            password = scanner.nextLine();
            lessThen4 = password.trim().split("\\s+")[0].length() < 4;
            withSpaces = password.trim().split("\\s+").length != 1;
            invalidChars = !password.trim().split("\\s+")[0].matches("[a-z0-9]+");
        } while (lessThen4 || withSpaces || invalidChars);
        System.out.println("Ваш логин: " + password.trim().split("\\s+")[0] + "\nВаш пароль: " + password.trim().split("\\s+")[0]);

        sendCommand(new Register(username, password));

        System.out.println("Получение ответа...");
    }


    //Авторизация пользователя
    private void login(Scanner scanner) throws IOException {
        System.out.println("Введите логин: ");
        username = scanner.nextLine();
        System.out.println("Введите пароль: ");
        password = scanner.nextLine();
        sendCommand(new Login(username, password));


        System.out.println("Получение ответа...");
    }
}