package Management;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;

public class Database {

    private String SERVER_ADDRESS;
    private String PORT;
    private String DATABASE_NAME;
    private String USER_NAME;
    private String PASSWORD;

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    private void loadParameters() {
        Properties properties = new Properties();
        InputStream inputStream;

        try {
            File file = new File("config.properties");
            inputStream = new FileInputStream(file);
            properties.load(inputStream);
        } catch (Exception e) {
            System.out.println("READING properties FAILED.");
        }

        SERVER_ADDRESS = properties.getProperty("SERVER_ADDRESS");
        PORT = properties.getProperty("PORT");
        DATABASE_NAME = properties.getProperty("DATABASE_NAME");
        USER_NAME = properties.getProperty("USER_NAME");
        PASSWORD = properties.getProperty("PASSWORD");
    }

    public Database() {
        loadParameters();
        try {
            connection = DriverManager
                    .getConnection("jdbc:postgresql://" + SERVER_ADDRESS + ':' + PORT + '/' + DATABASE_NAME, USER_NAME, PASSWORD);
            System.out.println("DATABSE OK");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static String myGetString(ResultSet resultSet, String column) throws SQLException {
        String result = resultSet.getString(column);
        if (result == null)
            return null;
        return result.replaceAll("'", "");
    }

}
