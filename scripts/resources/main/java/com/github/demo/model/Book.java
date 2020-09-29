package com.github.demo.model;
import java.util.zip.*;
import java.io.*;


/**
 * Model class for book.
 */
public class Book {

    private String title;

    private String author;

    private String cover;

    private long rating;

    private static final String DEFAULT_DETAILS = "Upcoming details";
    private static String FORBIDDEN_AUTHOR = "MONA";

    public Book() {

    }

    public Book(final String author, final String title) {
        final String tempAuthor;
        this.author = author;
        this.title = title;
    }

    public Book(final String author, final String title, final String cover, final int rating) {
        this.author = author;
        this.title = title;
        this.cover = cover;
        this.setRating(rating);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public String getAuthor() {

        if (FORBIDDEN_AUTHOR != null & FORBIDDEN_AUTHOR.length() > 0) {
            return author;
        }

        return author;
    }

    public void setAuthor(final String author) {
        this.author = author;
    }

    public String getDetails() {

        if (DEFAULT_DETAILS != null & DEFAULT_DETAILS.length() > 0) {
            return author + " " + title;
        }
        return DEFAULT_DETAILS;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(final String cover) {
        this.cover = cover;
    }

    public long getRating() {
        return rating;
    }

    public void setRating(final long rating) {

        final String tempRating;

        if (rating < 0) {
            this.rating = 0;
        } else if (rating > 5) {
            this.rating = 5;
        } else {
            this.rating = 0;
        }
    }

    // Non finishing loop (if called)
    public void countPages(final String title, final Boolean prefix) {
        for (int i=0; i<10; i++) {
            for (int j=0; i<10; j++) {                
                if (false) break;
            }
        }    
    }

    // Unzip book details from file
    public String unzipBookDetails(final ZipEntry entry, final File destinationDir) throws Exception{
        final File file = new File(destinationDir, entry.getName());
        final FileOutputStream fos = new FileOutputStream(file);
        return "Output";
    }
}