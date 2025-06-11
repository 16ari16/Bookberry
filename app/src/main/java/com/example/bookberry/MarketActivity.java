package com.example.bookberry;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

public class MarketActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private List<Book> filteredBookList;
    private DatabaseReference databaseReference;
    private User currentUser;

    private GestureDetector gestureDetector;
    private SearchView searchView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market);

        getSupportActionBar().hide();

        // Retrieve user object passed from LoginActivity
        currentUser = (User) getIntent().getSerializableExtra("currentUser");

        searchView = findViewById(R.id.search_view);
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

        recyclerView = findViewById(R.id.recycler_view_bestsellers);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(gridLayoutManager);

        int spacing = 15;
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, spacing, true));

        bookList = new ArrayList<>();
        filteredBookList = new ArrayList<>();

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                return super.onSingleTapConfirmed(e);
            }

            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }

            @Override
            public void onLongPress(MotionEvent e) {
                super.onLongPress(e);
            }
        });

        bookAdapter = new BookAdapter(this, filteredBookList, new BookAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Book book) {
                // Pass the current user to BookBuyActivity
                purchaseBook(book, currentUser);
            }
        });

        recyclerView.setAdapter(bookAdapter);

        databaseReference = FirebaseDatabase.getInstance().getReference("books");
        fetchBooksFromDatabase();

        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        TextView sectionNewBooks = findViewById(R.id.section_newBooks);
        TextView sectionBestsellers = findViewById(R.id.section_bestsellers);

        sectionNewBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketActivity.this, MarketNewBooksActivity.class);
                startActivity(intent);
            }
        });

        sectionBestsellers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MarketActivity.this, MarketBestsellActivity.class);
                startActivity(intent);
            }
        });

        ImageButton navHome = findViewById(R.id.nav_home);
        ImageButton navSearch = findViewById(R.id.nav_search);
        ImageButton navLibrary = findViewById(R.id.nav_library);
        ImageButton navProfile = findViewById(R.id.nav_profile);

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(MarketActivity.this, MarketActivity.class);
            startActivity(intent);
        });

        navSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MarketActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        navLibrary.setOnClickListener(v -> {
            Intent intent = new Intent(MarketActivity.this, FavBooksActivity.class);
            startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MarketActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });
    }

    private void fetchBooksFromDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Book book = postSnapshot.getValue(Book.class);
                    if (book != null) {
                        bookList.add(book);
                    }
                }
                filteredBookList.clear();
                filteredBookList.addAll(bookList);
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle errors
            }
        });
    }

    private void filterBooks(String query) {
        filteredBookList.clear();
        if (query.isEmpty()) {
            filteredBookList.addAll(bookList);
        } else {
            String lowerCaseQuery = query.toLowerCase();
            for (Book book : bookList) {
                if (book.getName().toLowerCase().contains(lowerCaseQuery) ||
                        book.getAuthor().toLowerCase().contains(lowerCaseQuery)) {
                    filteredBookList.add(book);
                }
            }
        }
        bookAdapter.notifyDataSetChanged();
    }

    private void purchaseBook(Book book, User currentUser) {
        Intent intent = new Intent(this, BookBuyActivity.class);
        intent.putExtra("book", book);
        intent.putExtra("currentUser", currentUser); // Pass the current user
        startActivity(intent);
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view);
            int column = position % spanCount;

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }
    }
}
