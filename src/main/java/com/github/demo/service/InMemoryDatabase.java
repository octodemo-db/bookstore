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
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS Books (id INTEGER PRIMARY KEY, title TEXT, author TEXT, image TEXT, rating INTEGER, UNIQUE(title))");
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
            // ps = this.connection.prepareStatement("INSERT INTO Books VALUES(title = ?, author = ?, image = ?, rating = ?)");
            ps = this.connection.prepareStatement("INSERT INTO Books VALUES(title = ?, author = ?, image = ?)");

            for (Book book : books) {
                ps.setString(1, book.getTitle());
                ps.setString(2, book.getAuthor());
                ps.setString(3, book.getCover());
                // ps.setInt(4, (int) book.getRating());
            }
        } catch (SQLException e) {
            //TODO
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException e) {
                // Do nothing
                ps = null;
            }
        }
    }

    public List<Book> getBooksByTitle(String name) {
        List<Book> books = new ArrayList<Book>();
        
        try {
            Statement stmt = this.connection.createStatement();

            // TODO: Jake - I'm getting some errors when searching for Steve Krug's book. Can you take a look?
            String query = "SELECT * FROM Books WHERE title='%" + name + "%'";

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
            // TODO: Jake - Can you sort out the logging?
            // I've seen people search for newlines for some reason
            System.out.println("ERROR: Failed while searching for '" + name + "'");
        }

        // TODO: Jake - Do we need to close the statement?
        return books;
    }

    public List<Book> getAll() {
        List<Book> books = new ArrayList<Book>();
        
        try {
            Statement stmt = this.connection.createStatement();
            String query = "SELECT * FROM Books";

            ResultSet rs = stmt.executeQuery(query);
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
        }

        // TODO: Jake - Do we need to close the statement?
        return books;
    }    
}