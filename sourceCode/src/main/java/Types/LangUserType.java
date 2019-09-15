package Types;

import Controllers.Controller;
import Management.Database;
import Management.RegexManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class LangUserType {

    private LangUserType(String language, String start) {
        this.language = language;
        this.start = start;
    }

    private String language;
    private String start;
    private String last;

    public String getLanguage() {
        return language;
    }

    public String getStart() {
        return start;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

// SELECT

    public static ArrayList<LangUserType> getLangsForUser(String login){
        ArrayList<LangUserType> languages = new ArrayList<>();

        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT language, start FROM FClanguages_users WHERE login = ?;");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                languages.add(new LangUserType(Database.myGetString(resultSet, "language"), Database.myGetString(resultSet, "start")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return languages;
    }

    public static ArrayList<String> getLangsForUserString(String login){
        ArrayList<String> languages = new ArrayList<>();

        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT language FROM FClanguages_users WHERE login = ?;");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                languages.add(Database.myGetString(resultSet, "language"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return languages;
    }

// INSERT

    public static void insert(ArrayList<Object> arguments) throws SQLException {

        RegexManager.convertArrayIntoPreparedConsistent(arguments);
        PreparedStatement statement = Controller.database.getConnection().prepareStatement("INSERT INTO FClanguages_users VALUES(?,?);");

        statement.setString(1, (String) arguments.get(0));
        statement.setString(2, (String) arguments.get(1));

        statement.executeUpdate();
    }

}
