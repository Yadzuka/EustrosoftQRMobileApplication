package ru.eustrosoft.androidqr.apipostgres;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBConnector {
    private Connection connectToDB() throws SQLException {
        String url = "jdbc:postgresql://fudo.eustrosoft.org/test";
        Properties props = new Properties();
        props.setProperty("user", "fred");
        props.setProperty("password", "secret");
        props.setProperty("ssl", "true");
        return DriverManager.getConnection(url, props);
    }

    public ResultSet getData(String query) throws SQLException {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            connection = connectToDB();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(query);
            return resultSet;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        } finally {
            if (statement != null)
                statement.close();
            if (resultSet != null)
                resultSet.close();
            if (connection != null)
                connection.close();
        }
    }
}
