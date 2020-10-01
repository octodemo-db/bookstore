package com.github.demo.service;

import com.github.demo.model.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.Properties;
import java.util.Collection;
import java.util.List;
import java.util.ArrayList;

public class PostgresDatabase implements IBookDatabase {

    private Connection connection;

    public PostgresDatabase(String url, String username, String password) {
        try {
            Properties props = new Properties();
            props.setProperty("user", username);
            props.setProperty("password", password);
            props.setProperty("ssl", "false");

            connection = DriverManager.getConnection(url, props);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void populate(Collection<Book> books) {
        //TODO we expect this to be provided to us as part of the application deployment
    }

    @Override
    public List<Book> getAll() {
        List<Book> books = new ArrayList<Book>();
        
        try {
            Statement stmt = connection.createStatement();

            ResultSet rs = stmt.executeQuery("SELECT * FROM books");
            while(rs.next()) {
                Book book = new Book(
                    rs.getString("author"),
                    rs.getString("title"),
                    rs.getString("image")
                );
                books.add(book);
            }
        }
        catch(SQLException error) {
            //TODO
            error.printStackTrace();
        }
        return books;
    }

    @Override
    public List<Book> getBooksByTitle(String name) {
        return null;
    }
}
