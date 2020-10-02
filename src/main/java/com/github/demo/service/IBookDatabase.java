package com.github.demo.service;

import com.github.demo.model.Book;

import java.util.List;
import java.util.Collection;

public interface IBookDatabase {

    void populate(Collection<Book> books);

    List<Book> getAll();

    List<Book> getBooksByTitle(String name);
}