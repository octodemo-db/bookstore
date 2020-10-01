package com.github.demo.service;

import com.github.demo.model.Book;

import java.util.Collection;
import java.util.List;

public class PostgresDatabase implements IBookDatabase {

    public PostgresDatabase() {

    }

    @Override
    public void populate(Collection<Book> books) {

    }

    @Override
    public List<Book> getAll() {
        return null;
    }

    @Override
    public List<Book> getBooksByTitle(String name) {
        return null;
    }
}
