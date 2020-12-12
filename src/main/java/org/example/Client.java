package org.example;

//import java.io.InputStreamReader;
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//import java.nio.ByteBuffer;
//import java.nio.channels.DatagramChannel;
//import java.nio.channels.SelectionKey;
//import java.nio.channels.Selector;
//import java.nio.channels.UnresolvedAddressException;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.Scanner;
//
//@Log4j2
//public class Client implements Runnable {
//
//    private final DatagramChannel datagramChannel;
//    private SocketAddress socketAddress;
//    private final Selector selector;
//    private CommandManager commandManager = new CommandManager();
//    private Iterator<String> script_iterator = null;
//
//    public Client() throws IOException, TransformerException, ParserConfigurationException {
//        selector = Selector.open();
//        datagramChannel = DatagramChannel.open();
//        datagramChannel.configureBlocking(false);
//    }
//
//    public static void main(String args[]) throws IOException, ArrayIndexOutOfBoundsException {
//
//        try {
//
//            Client client = new Client();
//            client.connect("localhost", 50000);
//            client.run();
//        } catch (IOException | UnresolvedAddressException | TransformerException | ParserConfigurationException e) {
//        }
//    }
//
//    @Override
//    public void run() {
//
//        try {
//            try {
//                Scanner scanner = new Scanner(System.in);
//
//                datagramChannel.register(selector, SelectionKey.OP_WRITE);
//                while (selector.select() > 0) {
//                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
//                    while (iterator.hasNext()) {
//                        SelectionKey selectionKey = iterator.next();
//                        iterator.remove();
//
//                        //Тут просиходит получение ответа от Сервера.
//                        if (selectionKey.isReadable()) {
//
//                            String answer = receiveAnswer();
//                            if (!answer.contains("Something goes wrong")) {
//                                datagramChannel.register(selector, SelectionKey.OP_WRITE);
//                                System.out.println(answer);
//                            }
//                        }
//                        if (selectionKey.isWritable()) {
//                            datagramChannel.register(selector, SelectionKey.OP_READ);
//                            try {
//                                String string;
//
//                                if (script_iterator != null && script_iterator.hasNext()) {
//                                    string = script_iterator.next();
//                                } else {
//                                    string = scanner.nextLine();
////                                   if  CommandInfo.setXml(new File(string));
//                                }
//                                String[] strings = string.trim().split("\\s+");
//                                AbstractCommand command = null;
//
//
//                                //Выполнение скрипта
//                                if (strings[0].equals("execute_script") && strings.length == 2) {
//                                    ExecuteScript execute_script = new ExecuteScript();
//                                    try {
//                                        execute_script.GetExecute(strings[1]);
//                                        script_iterator = execute_script.ScriptCommands().iterator();
//                                    } catch (ParserConfigurationException | TransformerException e) {
//                                        e.printStackTrace();
//                                    }
//
//                                }
//
//                                //Отсеивание неправильных команд
//                                if (strings[0].equals("execute_script") && strings.length != 2) {
//                                    throw new NumberFormatException();
//                                }
//
//                                //Отсеивание неправильных команд
//                                if (strings.length > 2 || !commandManager.Check(strings[0])) {
//                                    if (!string.contains("execute_script")) sendCommand(string);
//                                    //throw new NoCommandException("Такой команды не существует");
//                                } else {
//                                    command = commandManager.getCommand(strings[0]);
//                                    //Отсеивание неправильных команд
//                                    if (strings.length == 0) {
//                                        throw new NumberFormatException();
//                                    }
//                                    //Отсеивание неправильных команд
//                                    if (strings.length == 2) {
//                                        if (CheckINT(strings[1])) {
//                                            if (strings[0].equals("execute_script")) {
//                                                throw new NumberFormatException();
//                                            } else if (strings[0].equals("remove_any_by_end_date")) {
//                                                throw new NumberFormatException();
//                                            }
//                                        }
//                                    }
//
//                                    //Отсеивание неправильных команд
//                                    if (strings.length == 2 && !command.isNeedAnStr()) {
//                                        throw new NumberFormatException();
//                                    }
//
//                                    //Отсеивание неправильных команд
//                                    if (strings.length == 1 && command.isNeedAnStr()) {
//                                        throw new NumberFormatException();
//                                    }
//
//                                    // не нужен объект и аргумент к нему.(Пример Help, Show)
//                                    if (!command.getObjectExecute() && !command.isNeedAnStr()) {
//                                        sendCommand(command);
//                                        continue;
//                                    }
//
//                                    //нужен только аргумент. (Пример: RemoveLowerKey {key})
//                                    if (!command.getObjectExecute() && command.isNeedAnStr()) {
//                                        sendCommand(command, strings[1]);
//                                        continue;
//                                    }
//
//                                    //нужен объект и аргумент. (Пример: removeLower {element}})
//                                    if (command.getObjectExecute() && !command.isNeedAnStr()) {
//                                        if (command.isNeedWorker())
//                                            sendWorkerCommand(command);
//                                        if (command.isNeedOrganization())
//                                            sendOrganizationCommand(command);
//                                        continue;
//                                    }
//
//                                    //нужен объект и аргумент. (Пример: insert {key} + {element}})
//                                    if (command.getObjectExecute() && command.isNeedAnStr()) {
//                                        sendWorkerCommand(command, strings[1]);
//                                    }
//                                }
//                            } catch (Exception e) {
//                                System.out.println("Вы убили клиент,  поздравляю.");
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    //Подключение к серверу
//    private void connect(String hostname, int port) throws IOException {
//        socketAddress = new InetSocketAddress(hostname, port);
//        datagramChannel.connect(socketAddress);
//        System.out.println("Устанавливаем соединение с " + hostname + " по порту " + port);
//    }
//
//    //Получение ответа от сервера
//    private String receiveAnswer() throws IOException {
//        byte[] bytes = new byte[65536];
//        ByteBuffer buffer = ByteBuffer.wrap(bytes);
//        socketAddress = datagramChannel.receive(buffer);
//        String msg = new String(buffer.array());
//        return msg;
//    }
//
//    //Отправляет команду вместе с объектом
//    private void sendWorkerCommand(AbstractCommand command) throws IOException {
//
//        Worker worker = new Update().update(String.valueOf(0), new ArrayList<>(), false);
//        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(command));
//        datagramChannel.send(buffer, socketAddress);
//        sendWorker(worker);
//    }
//
//    //Отправляет команду вместе с объектом
//    private void sendWorkerCommand(AbstractCommand command, String string) throws IOException {
//        if (command != null) {
//            command.setString(string);
//        }
//        Worker worker = new Update().update(string, new ArrayList<>(), false);
//        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(command));
//        datagramChannel.send(buffer, socketAddress);
//        sendWorker(worker);
//    }
//
//    //Отправляет команду вместе с объектом
//    private void sendOrganizationCommand(AbstractCommand command) throws IOException {
//
//        BufferedInputStream bf = new BufferedInputStream(System.in);
//        BufferedReader reader = new BufferedReader(new InputStreamReader(bf, StandardCharsets.UTF_8));
//        Organization organization = SetOrganizationDefault.setOrganizationDefault(new ArrayList<>(), false, reader);
//        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(command));
//        datagramChannel.send(buffer, socketAddress);
//        sendOrganization(organization);
//    }
//
//    // Отправляет объект на сервер
//    private void sendOrganization(Organization organization) throws IOException {
//        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(organization));
//        datagramChannel.send(buffer, socketAddress);
//        System.out.println("org" + " отправлен");
//    }
//
//    //Отправка команды c параметром на сервер
//    private void sendCommand(AbstractCommand command, String parameter) throws IOException {
//        if (command != null) {
//            command.setString(parameter);
//        }
//        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(command));
//        datagramChannel.send(buffer, socketAddress);
//        sendCommand(parameter);
//    }
//
//    //Отправка строки на сервер
//    private void sendCommand(String str) throws IOException {
//        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(str));
//        datagramChannel.send(buffer, socketAddress);
//    }
//
//    //Отправка команды на сервер
//    private void sendCommand(AbstractCommand str) throws IOException {
//        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(str));
//        datagramChannel.send(buffer, socketAddress);
//        if (str.getCommand().equals("exit")) {
//            System.out.println("Завершение программы");
//            System.exit(0);
//        }
//
//    }
//
//    // Отправляет объект на сервер
//    private void sendWorker(Worker worker) throws IOException {
//        ByteBuffer buffer = ByteBuffer.wrap(new Serialization().SerializeObject(worker));
//        datagramChannel.send(buffer, socketAddress);
//        System.out.println(worker.getName() + " отправлен");
//    }
//
//
//    //проверка на число
//    private boolean CheckINT(String str) {
//        try {
//            Integer abs = Integer.valueOf(str);
//            return true;
//        } catch (Exception e) {
//            return false;
//        }
//    }
//}