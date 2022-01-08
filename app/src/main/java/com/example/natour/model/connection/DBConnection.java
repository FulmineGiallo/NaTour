package com.example.natour.model.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

public class DBConnection
{
    private static DBConnection instance;
    private Connection connection = null;
    private final String USERNAME = "postgres";
    private final String PASSWORD = "Carmine123";
    private final String IP = "natour.ch9owjm5jwk4.eu-west-1.rds.amazonaws.com\n";
    private final String PORT = "5432";
    private final String database ="natourapp";
    private String url = "jdbc:postgresql://"+IP+":"+PORT+"/"+database;


    public DBConnection() throws SQLException
    {
        try
        {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, USERNAME, PASSWORD);
            System.out.println("Database Connection Creation");

        }
        catch (ClassNotFoundException ex)
        {
            System.out.println("Database Connection Creation Failed : " + ex.getMessage());
        }

    }

    public Connection getConnection() {
        return connection;
    }

    public static DBConnection getInstance() throws SQLException
    {
        if (instance == null)
        {
            instance = new DBConnection();
        }
        else if (instance.getConnection().isClosed())
        {
            instance = new DBConnection();
        }

        return instance;
    }
}