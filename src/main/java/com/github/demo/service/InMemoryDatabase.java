package com.github.demo.service;

import com.github.demo.model.Book;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;
import java.util.ArrayList;
import java.util.Collection;

public class InMemoryDatabase implements IBookDatabase {

    private Connection connection;

    public InMemoryDatabase() {
        Statement stmt = null;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection("jdbc:sqlite::memory:");
            
            stmt = connection.createStatement();
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS books (id INTEGER PRIMARY KEY, title TEXT NOT NULL UNIQUE, author TEXT, image TEXT, rating INTEGER)");
        }
        catch(SQLException error) {
            error.printStackTrace();
        } catch (ClassNotFoundException error) {
            error.printStackTrace();
        }
        finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException error) {
                // Do nothing
                error.printStackTrace();
            }
        }
    }

    public void populate(Collection<Book> books) {
        PreparedStatement ps = null;

        try {
             ps = connection.prepareStatement("INSERT INTO books (title, author, image) VALUES(?, ?, ?)");

            for (Book book : books) {
                ps.setString(1, book.getTitle());
                ps.setString(2, book.getAuthor());
                ps.setString(3, book.getCover());
                // ps.setInt(4, (int) book.getRating());
                ps.execute();
            }
        } catch (SQLException e) {
            //TODO
            e.printStackTrace();
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
                // Do nothing
                ps = null;
            }
        }
        System.out.println("Database populated");
    }

    public List<Book> getBooksByTitle(String name) {
        List<Book> books = new ArrayList<Book>();

        Statement stmt = null;

        try {
            stmt = connection.createStatement();
            String query = "SELECT * FROM books WHERE title='%" + name + "%'";

            ResultSet results = stmt.executeQuery(query);

            while(results.next()) {
                Book book = new Book(
                    results.getString("author"),
                    results.getString("title"),
                    results.getString("image")
                    // results.getInt("rating")
                );

                books.add(book);
            }
        }
        catch(SQLException error) {
            // I've seen people search for newlines for some reason
            System.out.println("ERROR: Failed while searching for '" + name + "'");

            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (SQLException e) {
                // Do nothing
            } finally {
                stmt = null;
            }
        }
        return books;
    }

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
                    // results.getInt("rating")
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

    public void destroy() {
        try {
            this.connection.close();
        } catch (SQLException throwables) {
            // Ignore
            throwables.printStackTrace();
        } finally {
            this.connection = null;
        }
    }
}