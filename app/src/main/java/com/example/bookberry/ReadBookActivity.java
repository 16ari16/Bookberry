package com.example.bookberry;

import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ReadBookActivity extends AppCompatActivity {

    private TextView bookTitleTextView;
    private TextView bookAuthorTextView;
    private TextView chapterTitleTextView;
    private TextView chapterContentTextView;
    private ImageView bookCoverImageView;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);
        getSupportActionBar().hide();
        // Initialize views
        bookTitleTextView = findViewById(R.id.book_title);
        bookAuthorTextView = findViewById(R.id.author);
        chapterTitleTextView = findViewById(R.id.chapter_title);
        chapterContentTextView = findViewById(R.id.chapter_content);
        bookCoverImageView = findViewById(R.id.book_cover);
        backButton = findViewById(R.id.back_button);

        // Retrieve the book object passed from FavBooksActivity
        Book book = getIntent().getParcelableExtra("book");
        if (book != null) {
            // Set book details
            bookTitleTextView.setText(book.getName());
            bookAuthorTextView.setText(book.getAuthor());
            // Assuming the chapter title and content are part of the Book object
            chapterTitleTextView.setText("Глава 1\nСова ночью");


            // Load the book cover image using Picasso or any image loading library
            Picasso.get().load(book.getUrl()).into(bookCoverImageView);
        }

        // Set click listener for back button
        backButton.setOnClickListener(v -> onBackPressed());
    }
}
