package com.example.bookberry;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class ReviewActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference reviewsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        database = FirebaseDatabase.getInstance();
        reviewsRef = database.getReference("reviews");

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().hide();

        Book book = getIntent().getParcelableExtra("book");

        if (book != null) {
            ImageView bookCover = findViewById(R.id.book_cover);
            TextView bookTitle = findViewById(R.id.book_title);
            TextView bookAuthor = findViewById(R.id.book_author);
            TextView bookRating = findViewById(R.id.book_rating);

            bookTitle.setText(book.getName());
            bookAuthor.setText(book.getAuthor());
            bookRating.setText(book.getRating() + " ★");

            // Используйте Picasso для загрузки изображения из URL
            Picasso.get().load(book.getUrl()).into(bookCover);
        }

        Button submitReviewButton = findViewById(R.id.submit_review_button);
        submitReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitReview();
            }
        });
    }

    private void submitReview() {
        EditText reviewText = findViewById(R.id.review_text);
        String reviewDescription = reviewText.getText().toString().trim();

        Book book = getIntent().getParcelableExtra("book");

        if (book != null && !reviewDescription.isEmpty()) {
            String userId = "some_user_id";  // Replace with actual user ID logic
            Review review = new Review(book.getName(), book.getAuthor(), book.getRating(), reviewDescription, userId);
            reviewsRef.push().setValue(review).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(ReviewActivity.this, "Отзыв добавлен", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ReviewActivity.this, "Ошибка добавления отзыва", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }
}
