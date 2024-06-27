package com.example.bookberry;

public class Book {
    private String id;
    private String name;
    private String author;
    private String year;
    private String price;
    private String  rating;
    private String url;

    public Book() {
        // Default constructor required for calls to DataSnapshot.getValue(Book.class)
    }

    public Book(String id, String name, String author, String year, String price, String rating, String url ) {
        this.id = id;
        this.name = name;
        this.author = author;
        this.year = year;
        this.price = price;
        this.rating = rating;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public String getYear() {
        return year;
    }

    public String getPrice() {
        return price;
    }

    public String getRating() {
        return rating;
    }
    public String getUrl() {
        return url;
    }
}
