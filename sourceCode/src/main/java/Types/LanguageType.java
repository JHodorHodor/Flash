package Types;

import Controllers.Controller;
import Management.Database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedList;

public class LanguageType {

    private String language;
    private String image;
    private String specialSymbols;
    private Integer quantity;

    public LanguageType(String language, String image, String specialSymbols, Integer quantity) {
        this.language = language;
        this.image = image;
        this.specialSymbols = specialSymbols;
        this.quantity = quantity;
    }

    public String getLanguage() {
        return language;
    }

    public String getImage() {
        return image;
    }

    public String getSpecialSymbols() {
        return specialSymbols;
    }

    public Integer getQuantity() {
        return quantity;
    }

// SELECT

    public static LinkedList<LanguageType> getLanguages(){

        LinkedList<LanguageType> languages = new LinkedList<>();
        try {
            PreparedStatement statement = Controller.database.getConnection().prepareStatement("SELECT * FROM FClanguages;");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                LanguageType languageType = new LanguageType(Database.myGetString(resultSet, "language"), Database.myGetString(resultSet, "image"), Database.myGetString(resultSet, "special_symbols"), resultSet.getInt(4));
                languages.add(languageType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return languages;
    }

    public static HashMap<String, LanguageType> toHashMap(){
        HashMap<String, LanguageType> languages = new HashMap<>();
        for(LanguageType languageType : getLanguages()){
            languages.put(languageType.getLanguage(), languageType);
        }
        return languages;
    }

// DELETE

    public static void delete(String loginDB, String languageDB) throws SQLException {
        PreparedStatement statement = Controller.database.getConnection().prepareStatement("DELETE FROM FClanguages_users WHERE login = ? AND language = ?;");
        statement.setString(1, loginDB);
        statement.setString(2, languageDB);
        statement.execute();
    }

}
