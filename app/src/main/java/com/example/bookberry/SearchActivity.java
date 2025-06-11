package com.example.bookberry;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private List<Book> filteredBookList;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().hide();

        searchView = findViewById(R.id.search_3);
        recyclerView = findViewById(R.id.search_RV);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2)); // Установка GridLayoutManager с двумя колонками

        bookList = new ArrayList<>();
        filteredBookList = new ArrayList<>();

        bookAdapter = new BookAdapter(this, filteredBookList, book -> {
            // Handle book item click
            Intent intent = new Intent(SearchActivity.this, BookBuyActivity.class);
            intent.putExtra("book", book);
            startActivity(intent);
        });

        recyclerView.setAdapter(bookAdapter);

        loadBooks();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filterBooks(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterBooks(newText);
                return true;
            }
        });

        // Set up navigation buttons
        setupNavigationButtons();
    }

    private void loadBooks() {
        FirebaseDatabase.getInstance().getReference("books").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bookList.clear();
                for (DataSnapshot bookSnapshot : snapshot.getChildren()) {
                    Book book = bookSnapshot.getValue(Book.class);
                    if (book != null) {
                        bookList.add(book);
                    }
                }
                filteredBookList.addAll(bookList);
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }

    private void filterBooks(String query) {
        filteredBookList.clear();
        if (query.isEmpty()) {
            filteredBookList.addAll(bookList);
        } else {
            for (Book book : bookList) {
                if (book.getName().toLowerCase().contains(query.toLowerCase()) ||
                        book.getAuthor().toLowerCase().contains(query.toLowerCase())) {
                    filteredBookList.add(book);
                }
            }
        }
        bookAdapter.notifyDataSetChanged();
    }

    private void setupNavigationButtons() {
        ImageButton navHome = findViewById(R.id.nav_home);
        ImageButton navSearch = findViewById(R.id.nav_search);
        ImageButton navLibrary = findViewById(R.id.nav_library);
        ImageButton navProfile = findViewById(R.id.nav_profile);

        navHome.setOnClickListener(view -> startActivity(new Intent(SearchActivity.this, MarketActivity.class)));
        navSearch.setOnClickListener(view -> startActivity(new Intent(SearchActivity.this, SearchActivity.class)));
        navLibrary.setOnClickListener(view -> startActivity(new Intent(SearchActivity.this, FavBooksActivity.class)));
        navProfile.setOnClickListener(view -> startActivity(new Intent(SearchActivity.this, UserProfileActivity.class)));
    }
}
