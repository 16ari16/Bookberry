package com.example.bookberry;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
    private String id;
    private String name;
    private String author;
    private String year;
    private String price;
    private String rating;
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

    protected Book(Parcel in) {
        id = in.readString();
        name = in.readString();
        author = in.readString();
        year = in.readString();
        price = in.readString();
        rating = in.readString();
        url = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(author);
        dest.writeString(year);
        dest.writeString(price);
        dest.writeString(rating);
        dest.writeString(url);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Book> CREATOR = new Creator<Book>() {
        @Override
        public Book createFromParcel(Parcel in) {
            return new Book(in);
        }

        @Override
        public Book[] newArray(int size) {
            return new Book[size];
        }
    };

    // Getters
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
