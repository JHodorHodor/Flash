package Types;

import Controllers.Controller;
import Management.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class WordUserType {

    private WordUserType(String login, Integer id, Integer state, String history, boolean toDelete) {
        this.login = login;
        this.id = id;
        this.state = state;
        this.history = history;
        this.toDelete = toDelete;
    }

    private String login;
    private Integer id;
    private Integer state;
    private String history;
    private boolean toDelete;

    public Integer getState() {
        return state;
    }

    public String getHistory() {
        return history;
    }

    public boolean isToDelete() {
        return toDelete;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public void setHistory(String history) {
        this.history = history;
    }

    public void setToDelete(boolean toDelete) {
        this.toDelete = toDelete;
    }

// SELECT

    public static LinkedList<WordUserType> getWords(String loginDB, String languageDB) {

        LinkedList<WordUserType> words = new LinkedList<>();
        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT * FROM FCwords_users JOIN FCwords USING(id) WHERE login = ? AND language = ?;");
            statement.setString(1, loginDB);
            statement.setString(2, languageDB);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                WordUserType word = new WordUserType(
                        Database.myGetString(resultSet, "login"),
                        resultSet.getInt("id"),
                        resultSet.getInt("state"),
                        Database.myGetString(resultSet, "history"),
                        resultSet.getBoolean("to_delete")
                );
                words.add(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static HashMap<Integer, WordUserType> toHashMap(String loginDB, String languageDB) {
        HashMap<Integer, WordUserType> words = new HashMap<>();
        for(WordUserType wordUserType : getWords(loginDB, languageDB))
            words.put(wordUserType.id, wordUserType);
        return words;
    }

    private static LinkedList<WordUserType> getToDeleteWords(String loginDB, String languageDB) {

        LinkedList<WordUserType> words = new LinkedList<>();
        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT * FROM FCwords_users JOIN FCwords USING(id) WHERE login = ? AND language = ? AND to_delete = TRUE;");
            statement.setString(1, loginDB);
            statement.setString(2, languageDB);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                WordUserType word = new WordUserType(
                        Database.myGetString(resultSet, "login"),
                        resultSet.getInt("id"),
                        resultSet.getInt("state"),
                        Database.myGetString(resultSet, "history"),
                        resultSet.getBoolean("to_delete")
                );
                words.add(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static HashMap<Integer, WordUserType> toHashMaptoDelete(String loginDB, String languageDB) {
        HashMap<Integer, WordUserType> words = new HashMap<>();
        for(WordUserType wordUserType : getToDeleteWords(loginDB, languageDB))
            words.put(wordUserType.id, wordUserType);
        return words;
    }

// UPDATE

    public static void update(String login, Integer id, WordUserType wordUserType) throws SQLException {
        PreparedStatement statement = Controller.database.getConnection().prepareStatement("UPDATE FCwords_users SET state = ?, history = ?, to_delete = ? WHERE login = ? AND id = ?");

        statement.setInt(1, wordUserType.getState());
        statement.setString(2, wordUserType.getHistory());
        statement.setBoolean(3, wordUserType.isToDelete());
        statement.setString(4, login);
        statement.setInt(5, id);

        statement.executeUpdate();
    }

// INSERT

    public static void insert(Integer id, String loginDB) throws SQLException {

        PreparedStatement statement = Controller.database.getConnection().prepareStatement("INSERT INTO FCwords_users(login,id) VALUES(?,?);");

        statement.setString(1, loginDB);
        statement.setInt(2, id);

        statement.execute();
    }

// DELETE

    public static void delete(String loginDB, Integer id) throws SQLException {
        PreparedStatement statement = Controller.database.getConnection().prepareStatement("DELETE FROM FCwords_users WHERE login = ? AND id = ?;");
        statement.setString(1, loginDB);
        statement.setInt(2, id);
        statement.execute();
    }

}
