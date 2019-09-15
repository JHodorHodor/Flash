package Types;

import Controllers.Controller;
import Management.Database;
import Management.RegexManager;

import javax.xml.crypto.Data;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

public class UserType {

    public UserType(String login, Integer password, String start, Integer wordsDaily) {
        this.login = login;
        this.password = password;
        this.start = start;
        this.wordsDaily = wordsDaily;
    }

    private String login;
    private Integer password;
    private String start;
    private Integer wordsDaily;

    public String getLogin() {
        return login;
    }

    public Integer getPassword() {
        return password;
    }

    public String getStart() {
        return start;
    }

    public Integer getWordsDaily() {
        return wordsDaily;
    }

    public void setWordsDaily(Integer wordsDaily) {
        this.wordsDaily = wordsDaily;
    }

// SELECT

    public static HashMap<String, Integer> getUsers() {

        HashMap<String, Integer> users = new HashMap<>();
        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT login, password FROM FCusers;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                users.put(Database.myGetString(resultSet, "login"), resultSet.getInt(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public static UserType getUser(String login) {
        UserType user = null;
        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT * FROM FCusers WHERE login = ?;");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                user = new UserType(Database.myGetString(resultSet, "login"), resultSet.getInt(2), Database.myGetString(resultSet, "start"), resultSet.getInt(4));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

// INSERT

    public static void insert(ArrayList<Object> arguments) throws SQLException {

        RegexManager.convertArrayIntoPreparedConsistent(arguments);
        PreparedStatement statement = Controller.database.getConnection().prepareStatement("INSERT INTO FCusers VALUES(?,?);");

        statement.setString(1, (String) arguments.get(0));
        statement.setInt(2, (Integer) arguments.get(1));

        statement.executeUpdate();
    }

// UPDATE

    public static void update(String login, Integer wordsDaily) throws SQLException {
        PreparedStatement statement = Controller.database.getConnection().prepareStatement("UPDATE FCusers SET words_daily = ? WHERE login = ?");

        statement.setString(2, login);
        statement.setInt(1, wordsDaily);

        statement.executeUpdate();
    }

}
