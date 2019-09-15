package Types;

import Controllers.Controller;
import Management.Database;
import Management.RegexManager;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class WordType {

    private WordType(Integer id, String language, String original, String polish) {
        this.id = id;
        this.language = language;
        this.original = original;
        this.polish = polish;
    }

    private Integer id;
    private String language;
    private String original;
    private String polish;

    public Integer getId() {
        return id;
    }

    public String getLanguage() {
        return language;
    }

    public String getOriginal() {
        return original;
    }

    public String getPolish() {
        return polish;
    }


// SELECT

    public static LinkedList<WordType> getWords() {

        LinkedList<WordType> words = new LinkedList<>();
        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT * FROM FCwords;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                WordType word = new WordType(
                        resultSet.getInt(1),
                        Database.myGetString(resultSet, "language"),
                        Database.myGetString(resultSet, "original"),
                        Database.myGetString(resultSet, "polish"));
                words.add(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static HashMap<Integer, WordType> toHashMap(){
        HashMap<Integer, WordType> result = new HashMap<>();
        for(WordType wordType : getWords()){
            result.put(wordType.id, wordType);
        }
        return result;
    }

    public static LinkedList<WordType> getWords(String languageDB) {

        LinkedList<WordType> words = new LinkedList<>();
        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT * FROM FCwords WHERE language = ?;");
            statement.setString(1, languageDB);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                WordType word = new WordType(
                        resultSet.getInt(1),
                        Database.myGetString(resultSet, "language"),
                        Database.myGetString(resultSet, "original"),
                        Database.myGetString(resultSet, "polish"));
                words.add(word);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return words;
    }

    public static HashMap<String, WordType> toHashMap(String languageDB){
        HashMap<String, WordType> result = new HashMap<>();
        for(WordType wordType : getWords(languageDB)){
            result.put(RegexManager.convertToDisplay(wordType), wordType);
        }
        return result;
    }

    public static int getIdNew(String languageDB, String original, String polish){
        int id = -1;
        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT id FROM FCwords WHERE language = ? AND original = ? AND polish = ?;");
            statement.setString(1, languageDB);
            statement.setString(2, original);
            statement.setString(3, polish);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }

// INSERT

    public static void insert(String languageDB, String original, String polish) throws SQLException {

        PreparedStatement statement = Controller.database.getConnection().prepareStatement("INSERT INTO FCwords(language, original,polish) VALUES(?,?,?);");

        statement.setString(1, languageDB);
        statement.setString(2, original);
        statement.setString(3, polish);

        statement.execute();
    }

}
