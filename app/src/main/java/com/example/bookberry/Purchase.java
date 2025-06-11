package com.example.bookberry;

public class Purchase {
    private String userId;
    private String bookId;
    private boolean isFavorite;

    public Purchase() {
        // Пустой конструктор необходим для Firebase
    }

    public Purchase(String userId, String bookId, boolean isFavorite) {
        this.userId = userId;
        this.bookId = bookId;
        this.isFavorite = isFavorite;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}
