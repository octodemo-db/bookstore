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

public class BookDatabaseImpl implements BookDatabase {

    private Connection connection;

    public BookDatabaseImpl() {
        this(null, null, null);
    }

    public BookDatabaseImpl(String url, String username, String password) {
        try {
            Properties props = new Properties();

            if (username != null) {
                props.setProperty("user", username);
            }
            if (password != null) {
                props.setProperty("password", password);    
            }
            // This is a postgres specific setting, but SQLlite tolerates it
            props.setProperty("ssl", "false");

            // Default to a sqlite in memory database if no database url has been provided
            if (url == null) {
                url = "jdbc:sqlite::memory:";
            }
            connection = DriverManager.getConnection(url, props);

            if (url.indexOf(":memory:") > -1) {
                initializeAndPopulateDatabase();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
                    rs.getString("image"),
                    rs.getInt("rating")
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
        List<Book> books = new ArrayList<Book>();

        Statement stmt = null;

        try {
            stmt = connection.createStatement();
            String query = "SELECT * FROM books WHERE title LIKE '%" + name + "%'";

            ResultSet results = stmt.executeQuery(query);

            while(results.next()) {
                Book book = new Book(
                    results.getString("author"),
                    results.getString("title"),
                    results.getString("image"),
                    results.getInt("rating")
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

    @Override
    public void destroy() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            // Ignore
            connection = null;
        }
    }

    @Override
    public void populate(Collection<Book> books) throws SQLException {
        if (books != null && books.size() > 0) {
            PreparedStatement ps = null;

            try {
                ps = connection.prepareStatement("INSERT INTO books (title, author, image) VALUES(?, ?, ?)");

                for (Book book : books) {
                    System.out.println("Adding book to database: " + book.getTitle());
                    ps.setString(1, book.getTitle());
                    ps.setString(2, book.getAuthor());
                    ps.setString(3, book.getCover());
                    ps.execute();
                }
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
            System.out.println("Database populated");
        }
    }

    /**
     * Initializes the internal database structure and populates it with our default data. 
     */
    private void initializeAndPopulateDatabase() throws SQLException {
        Statement statement = null;
        try {
            // Initialize the database tables for in memory database
            statement = connection.createStatement();

            statement.execute("CREATE TABLE IF NOT EXISTS books (" 
                + "id INTEGER PRIMARY KEY, "
                + "title TEXT NOT NULL, "
                + "author TEXT, "
                + "image TEXT, "
                + "rating, INTEGER "
                + ")"
            );
            // Populate the database with some sample data
            populate(BookUtils.getSampleBooks());
        } catch (SQLException e) {
            if (statement != null) {
                statement.close();
            }
            throw e;
        }
        
    }
}
