package com.github.demo.service;

import com.github.demo.model.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.ArrayList;

public class InMemoryDatabaseTest {

    private InMemoryDatabase booksDatabase;

    @Before
    public void setUp() throws Exception {
        booksDatabase = new InMemoryDatabase();

        Collection<Book> books = new ArrayList<Book>(5);
        books.add(new Book("Peter Murray", "A Book"));
        booksDatabase.populate(books);
    }

    @After
    public void tearDown() {
        booksDatabase.destroy();
    }

    @Test
    public void testGetBooks() {
        Collection<Book> books = booksDatabase.getAll();
        assertEquals("Books in database should match", 1, books.size());
    }
}
