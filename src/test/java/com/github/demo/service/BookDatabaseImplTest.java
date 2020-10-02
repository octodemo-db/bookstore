package com.github.demo.service;

import com.github.demo.model.Book;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

import java.util.Collection;

public class BookDatabaseImplTest {

    private BookDatabase booksDatabase;

    @Before
    public void setUp() throws Exception {
        booksDatabase = new BookDatabaseImpl();
    }

    @After
    public void tearDown() {
        booksDatabase.destroy();
    }

    @Test
    public void testGetBooks() {
        Collection<Book> books = booksDatabase.getAll();
        assertEquals("Books in database should match", BookUtils.getSampleBooks().size(), books.size());
    }
}
