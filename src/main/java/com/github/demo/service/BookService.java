package com.github.demo.service;

import com.github.demo.model.Book;

import java.util.ArrayList;
import java.util.List;

public class BookService {

    private static final String API_TOKEN = "AIzaSyAQfxPJiounkhOjODEO5ZieffeBv6yft2Q";

    private BookDatabase booksDatabase;

    public BookService() {
        String databaseUrl = System.getenv("DATABASE_URL");
        String databaseUser = System.getenv("DATABASE_USER");
        String databasePassword = System.getenv("DATABASE_PASSWORD");

        booksDatabase = new BookDatabaseImpl(databaseUrl, databaseUser, databasePassword);
    }

    public List<Book> getBooks() {
        return this.booksDatabase.getAll();
    }

    public List<Book> searchBooks(String name) {
        return this.booksDatabase.getBooksByTitle(name);
    }
}