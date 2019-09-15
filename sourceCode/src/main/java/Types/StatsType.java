package Types;

import Controllers.Controller;
import Management.Database;
import Management.TimeManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;

public class StatsType {

    public StatsType(String login, String day, String language, Integer good, Integer aall, Integer ttime) {
        this.login = login;
        this.day = day;
        this.language = language;
        this.good = good;
        this.aall = aall;
        this.ttime = ttime;
    }

    private String login;
    private String day;
    private String language;
    private Integer good;
    private Integer aall;
    private Integer ttime;

    public String getDay() {
        return day;
    }

    public Integer getGood() {
        return good;
    }

    public Integer getAall() {
        return aall;
    }

    public Integer getTtime() {
        return ttime;
    }

// SELECT

    public static Calendar getLast(String loginDB, String languageDB) {
        String date = null;
        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT day FROM FCstats WHERE login = ? AND language = ? AND day >= ALL (SELECT day FROM FCstats WHERE login = ? AND language = ?);");
            statement.setString(1, loginDB);
            statement.setString(2, languageDB);
            statement.setString(3, loginDB);
            statement.setString(4, languageDB);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                date = Database.myGetString(resultSet, "day");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return TimeManager.convertToDate(date);
    }

    public static LinkedList<Calendar> getDates(String loginDB, String languageDB) {
        LinkedList<Calendar> result = new LinkedList<>();
        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT day FROM FCstats WHERE login = ? AND language = ?;");
            statement.setString(1, loginDB);
            statement.setString(2, languageDB);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add(TimeManager.convertToDate(Database.myGetString(resultSet, "day")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    private static LinkedList<StatsType> getStats(String loginDB, String languageDB) {
        LinkedList<StatsType> result = new LinkedList<>();
        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT * FROM FCstats WHERE login = ? AND language = ?;");
            statement.setString(1, loginDB);
            statement.setString(2, languageDB);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                result.add( new StatsType(
                        Database.myGetString(resultSet, "login"),
                        Database.myGetString(resultSet, "day"),
                        Database.myGetString(resultSet, "language"),
                        resultSet.getInt(4),
                        resultSet.getInt(5),
                        resultSet.getInt(6)));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static HashMap<String, StatsType> toHashMap(String loginDB, String languageDB){
        HashMap<String, StatsType> map = new HashMap<>();
        for(StatsType st : getStats(loginDB, languageDB))
            map.put(st.getDay(), st);
        return map;
    }

// INSERT

    public static void insert(StatsType statsType) throws SQLException {

        PreparedStatement statement = Controller.database.getConnection().prepareStatement("INSERT INTO FCstats(login,language,good,aall,ttime) VALUES(?,?,?,?,?);");

        statement.setString(1, statsType.login);
        statement.setString(2, statsType.language);
        statement.setInt(3, statsType.good);
        statement.setInt(4, statsType.aall);
        statement.setInt(5, statsType.ttime);

        statement.execute();
    }


}
