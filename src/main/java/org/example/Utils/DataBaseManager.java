package org.example.Utils;

import org.example.Utils.CommonUtils.PortForwarder;
import org.example.Worker.*;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.*;
import java.time.LocalDate;
import java.util.LinkedHashMap;


public class DataBaseManager implements Serializable {

    public static LinkedHashMap<Integer, Worker> mapColletion = new LinkedHashMap<>();

    private String URL;//= PortForwarder.forwardAnyPort();

    private String USER = "s284699";
    private String PASSWORD = "";

    //Нужны для авторизации пользователя

    private String userLogin = "example";
    private String userPassword;

    public DataBaseManager() {
        try {
            DriverManager.registerDriver(new org.postgresql.Driver());
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    public static LinkedHashMap<Integer, Worker> getMapColletion() {
        return DataBaseManager.mapColletion;
    }

//Добавление юзера в БД
    //todo переписать в файл USER
    public boolean CheckDB() {
        try (Connection connection = DriverManager.getConnection(PortForwarder.forwardAnyPort(), USER, PASSWORD)) {
            return true;
        } catch (SQLException e) {
            System.out.println(e);
            return false;
        }
    }


    public boolean CheckUser(String login) {
        try (Connection connection = DriverManager.getConnection(PortForwarder.forwardAnyPort(), USER, PASSWORD)) {

            PreparedStatement ps = connection.prepareStatement("select * from user where login = ?");
            ps.setString(1, login);
            return true;

        } catch (SQLException e) {
            return false;
        }
    }


    public boolean AddUser(String login, String password) {
        try (Connection connection = DriverManager.getConnection(PortForwarder.forwardAnyPort(), USER, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement
                    ("INSERT INTO s284699.user(login, pass) VALUES (?, ?)");
            preparedStatement.setString(1, login);
            preparedStatement.setString(2, encryptThisString(password));
            preparedStatement.execute();
            userLogin = login;
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
//метод для авторизации пользователя

    public boolean login(String login, String password) throws SQLException {
        try (Connection connection = DriverManager.getConnection(PortForwarder.forwardAnyPort(), USER, PASSWORD)) {

            PreparedStatement ps = connection.prepareStatement("select * from s284699.user where login = ?");
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) return false;

            if (rs.getString("pass").equals(encryptThisString(password))) {
                userLogin = login;
                return true;
            }

            return false;
        }
    }

    public void updateCollectionFromDataBase() {
        LinkedHashMap<Integer, Worker> linkedHashMap = new LinkedHashMap<>();
        try (Connection connection = DriverManager.getConnection(PortForwarder.forwardAnyPort(), USER, PASSWORD)) {
            //todo updatecol
            System.out.println("updatecoll");
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM worker");
            while (resultSet.next()) {
                Worker worker = new Worker();
                worker.setId(resultSet.getInt("id"));
                worker.setName(resultSet.getString("name"));
                worker.setCoordinates(new Coordinates(resultSet.getInt("x"), resultSet.getInt("y")));
                worker.setSalary(resultSet.getDouble("salary"));
                worker.setStartDate(LocalDate.parse(resultSet.getString("startdate")));
                worker.setEndDate(resultSet.getString("enddate"));
                worker.setStatus(Status.valueOf(resultSet.getString("status")));
                worker.setOrganization(new Organization(
                        resultSet.getLong("employeesCount"),
                        OrganizationType.valueOf(resultSet.getString("type")),
                        new Address(resultSet.getString("street"),
                                resultSet.getString("zipcode"),
                                new Location(resultSet.getDouble("townx"),
                                        resultSet.getInt("towny"),
                                        resultSet.getString("townname")))
                ));
                worker.setUserName(resultSet.getString("user"));
                System.out.println(worker.toWorkerString());
                linkedHashMap.put(worker.getId(), worker);
                System.out.println(linkedHashMap.get(60));
            }
            mapColletion.clear();
            mapColletion = linkedHashMap;

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean addToDataBase(Worker worker) {
        try (Connection connection = DriverManager.getConnection(PortForwarder.forwardAnyPort(), USER, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO worker (\"id\",\"name\", \"x\", \"y\",/* \"creation_time\", */ \"salary\", \"startdate\", \"enddate\", \"status\", \"employeescount\" , \"type\" , \"street\" , \"zipcode\" ,  \"townx\", \"towny\", \"townname\",\"user\") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?);");
            preparedStatement.setLong(1, worker.getId());
            preparedStatement.setString(2, worker.getName());
            preparedStatement.setLong(3, worker.getCoordinates().getX());
            preparedStatement.setInt(4, worker.getCoordinates().getY());
            preparedStatement.setDouble(5, worker.getSalary());
            preparedStatement.setString(6, String.valueOf(worker.getStartDate()));
            preparedStatement.setString(7, String.valueOf(worker.getEndDate()));
            preparedStatement.setString(8, String.valueOf(worker.getStatus()));
            preparedStatement.setLong(9, worker.getOrganization().getEmployeesCount());
            preparedStatement.setString(10, String.valueOf(worker.getOrganization().getType()));
            preparedStatement.setString(11, worker.getOrganization().getOfficialAddress().getStreet());
            preparedStatement.setString(12, worker.getOrganization().getOfficialAddress().getZipCode());
            preparedStatement.setDouble(13, worker.getOrganization().getOfficialAddress().getTown().getX());
            preparedStatement.setInt(14, worker.getOrganization().getOfficialAddress().getTown().getY());
            preparedStatement.setString(15, worker.getOrganization().getOfficialAddress().getTown().getName());
            preparedStatement.setString(16, userLogin);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean addToDataBase1(Worker worker) {
        try (Connection connection = DriverManager.getConnection(PortForwarder.forwardAnyPort(), USER, PASSWORD)) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO worker (\"id\",\"name\", \"x\", \"y\",/* \"creation_time\", */ \"salary\", \"startDate\", \"endDate\", \"status\", \"employeescount\" , \"type\" , \"street\" , \"zipCode\" ,  \"x\", \"y\", \"townname\",\"user\") " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?,?,?);");
            preparedStatement.setLong(1, worker.getId());
            preparedStatement.setString(2, worker.getName());
            preparedStatement.setLong(3, worker.getCoordinates().getX());
            preparedStatement.setInt(4, worker.getCoordinates().getY());
            preparedStatement.setDouble(5, worker.getSalary());
            preparedStatement.setString(6, String.valueOf(worker.getStartDate()));
            preparedStatement.setString(7, String.valueOf(worker.getEndDate()));
            preparedStatement.setString(8, String.valueOf(worker.getStatus()));
            preparedStatement.setString(9, userLogin);
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updateElementInDataBase1(Worker worker, String user) {
        try (Connection connection = DriverManager.getConnection(PortForwarder.forwardAnyPort(), USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM worker WHERE id = " + worker.getId());

            String userString = "";

            while (resultSet.next()) {
                userString = resultSet.getString("user");
            }

            if (user.equals(userString)) {
                removeFromDataBase(worker.getId(), user);
                addToDataBase(worker);
                return true;
            } else return false;

        } catch (SQLException e) {
            return false;
        }
    }


    public boolean removeFromDataBase(int id, String userLogin) {
        try (Connection connection = DriverManager.getConnection(PortForwarder.forwardAnyPort(), USER, PASSWORD)) {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM worker WHERE id = " + id);
            while (resultSet.next()) {
                if (!resultSet.getString("user").equals(userLogin)) {
                    return false;
                }
            }
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM worker WHERE id = " + id + " AND \"user\" = '" + userLogin + "'");
            preparedStatement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUSER() {
        return userLogin;
    }

    public void setUSER(String userLogin) {
        this.userLogin = userLogin;
    }

    //Проверяет авторизован ли пользователь
    public boolean UserAuthorized() {
        try {
            //todo костыль
            return !userLogin.equals("example");
        } catch (NullPointerException e) {
            return false;
        }
    }


    public void setCollection(LinkedHashMap<Integer, Worker> map) {
        DataBaseManager.mapColletion = map;
    }


    public String encryptThisString(String input) {
        try {
            // метод getInstance () вызывается с алгоритмом MD2
            MessageDigest md = MessageDigest.getInstance("MD2");

            // вызывается метод digest ()
            // вычислить дайджест сообщения из входной строки
            // возвращается как массив байтов
            byte[] messageDigest = md.digest(input.getBytes());
            // Преобразование байтового массива в представление знака
            BigInteger no = new BigInteger(1, messageDigest);
            // Преобразуем дайджест сообщения в шестнадцатеричное значение
            String hashtext = no.toString(16);
            // Добавить предыдущие 0, чтобы сделать его 32-битным
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            // возвращаем HashText
            return hashtext;
        }

        // Для указания неправильных алгоритмов дайджеста сообщений
        catch (NoSuchAlgorithmException e) {

            throw new RuntimeException(e);

        }

    }


}
