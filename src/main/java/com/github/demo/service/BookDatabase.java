package com.github.demo.service;

import com.github.demo.model.Book;

import java.util.List;
import java.sql.SQLException;
import java.util.Collection;

public interface BookDatabase {

    List<Book> getAll();

    List<Book> getBooksByTitle(String name);

    void populate(Collection<Book> books) throws SQLException;

    void destroy() throws SQLException;
}