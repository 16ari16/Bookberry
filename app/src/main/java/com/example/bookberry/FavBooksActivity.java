package com.example.bookberry;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private DatabaseReference purchasesRef, booksRef;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;

    private ImageButton navHomeButton;
    private ImageButton navSearchButton;
    private ImageButton navLibraryButton;
    private ImageButton navProfileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fav_books);
        getSupportActionBar().hide();

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Redirect to LoginActivity if not authenticated
            Intent loginIntent = new Intent(FavBooksActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                // Handle item click
                Intent readBookIntent = new Intent(FavBooksActivity.this, ReadBookActivity.class);
                readBookIntent.putExtra("book", book);
                startActivity(readBookIntent);
            }
        });
        recyclerView.setAdapter(bookAdapter);

        // Firebase references
        purchasesRef = FirebaseDatabase.getInstance().getReference("purchases");
        booksRef = FirebaseDatabase.getInstance().getReference("books");

        fetchPurchasedBooks();

        // Initialize navigation buttons
        navHomeButton = findViewById(R.id.nav_home);
        navSearchButton = findViewById(R.id.nav_search);
        navLibraryButton = findViewById(R.id.nav_library);
        navProfileButton = findViewById(R.id.nav_profile);

        // Set click listeners for navigation buttons
        navHomeButton.setOnClickListener(v -> {
            // Start Home Activity
            Intent homeIntent = new Intent(FavBooksActivity.this, MarketActivity.class);
            startActivity(homeIntent);
        });

        navSearchButton.setOnClickListener(v -> {
            // Start Search Activity
            Intent searchIntent = new Intent(FavBooksActivity.this, SearchActivity.class);
            startActivity(searchIntent);
        });

        navLibraryButton.setOnClickListener(v -> {
            // Start Library Activity
            Intent libraryIntent = new Intent(FavBooksActivity.this, FavBooksActivity.class);
            startActivity(libraryIntent);
        });

        navProfileButton.setOnClickListener(v -> {
            // Start Profile Activity
            Intent profileIntent = new Intent(FavBooksActivity.this, UserProfileActivity.class);
            startActivity(profileIntent);
        });
    }

    private void fetchPurchasedBooks() {
        purchasesRef.orderByChild("userId").equalTo(currentUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Purchase purchase = snapshot.getValue(Purchase.class);
                    if (purchase != null) {
                        String bookId = purchase.getBookId();
                        fetchBookDetails(bookId);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FavBooksActivity.this, "Failed to load purchases: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FavBooksActivity", "Failed to load purchases: " + databaseError.getMessage());
            }
        });
    }

    private void fetchBookDetails(String bookId) {
        booksRef.child(bookId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Book book = dataSnapshot.getValue(Book.class);
                if (book != null) {
                    Log.d("FavBooksActivity", "Book found: " + book.getName());
                    bookList.add(book);
                    bookAdapter.notifyDataSetChanged(); // Update adapter after adding book
                } else {
                    Log.d("FavBooksActivity", "Book is null for bookId: " + bookId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(FavBooksActivity.this, "Failed to load book details: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("FavBooksActivity", "Failed to load book details for bookId " + bookId + ": " + databaseError.getMessage());
            }
        });
    }
}
