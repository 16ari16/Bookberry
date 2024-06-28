package com.example.bookberry;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UsersReviewsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewsAdapter reviewsAdapter;
    private ArrayList<Review> reviewsList;
    private FirebaseDatabase database;
    private DatabaseReference reviewsRef;
    private String bookName;
    private TextView noReviewsTextView; // TextView to display "Отзывов нет."

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_reviews);
        getSupportActionBar().hide();

        // Retrieve book name from intent
        bookName = getIntent().getStringExtra("bookName");

        // Initialize views
        recyclerView = findViewById(R.id.reviews_recycler_view);
        noReviewsTextView = findViewById(R.id.no_reviews_text); // Initialize TextView
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reviewsList = new ArrayList<>();
        reviewsAdapter = new ReviewsAdapter(reviewsList);
        recyclerView.setAdapter(reviewsAdapter);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        reviewsRef = database.getReference("reviews");

        // Load reviews for the specified book
        loadReviews();

        // Back button setup
        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        getSupportActionBar().hide();
    }

    private void loadReviews() {
        reviewsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                reviewsList.clear();
                for (DataSnapshot reviewSnapshot : dataSnapshot.getChildren()) {
                    Review review = reviewSnapshot.getValue(Review.class);
                    if (review != null && review.getBookName().equals(bookName)) {
                        reviewsList.add(review);
                    }
                }
                reviewsAdapter.notifyDataSetChanged();

                // Show "Отзывов нет." message if reviewsList is empty
                if (reviewsList.isEmpty()) {
                    noReviewsTextView.setVisibility(View.VISIBLE);
                } else {
                    noReviewsTextView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(UsersReviewsActivity.this, "Failed to load reviews: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
