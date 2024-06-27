package com.example.bookberry;

import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MarketNewBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private DatabaseReference databaseReference;
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_new_books);

        // Установка отступов под системные панели
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Скрытие Action Bar
        getSupportActionBar().hide();

        // Инициализация RecyclerView и настройка LayoutManager
        recyclerView = findViewById(R.id.recycler_view_new_books);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Инициализация списка книг
        bookList = new ArrayList<>();

        // Инициализация адаптера
        bookAdapter = new BookAdapter(this, bookList);
        recyclerView.setAdapter(bookAdapter);

        // Получение ссылки на Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        // Чтение данных из Firebase
        fetchBooksFromDatabase();

        // Инициализация GestureDetector для обработки свайпов
        gestureDetector = new GestureDetector(this, new SwipeGestureDetector());

        // Установка OnTouchListener для всего экрана
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gestureDetector.onTouchEvent(event);
            }
        });

        // Находим TextView для разделов и устанавливаем OnClickListener
        TextView sectionMainBooks = findViewById(R.id.section_mainBooks);
        TextView sectionBestsellers = findViewById(R.id.section_bestsellers);

        sectionMainBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на MarketActivity при нажатии на section_mainBooks
                Intent intent = new Intent(MarketNewBooksActivity.this, MarketActivity.class);
                startActivity(intent);
            }
        });

        sectionBestsellers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Переход на MarketBestsellActivity при нажатии на section_bestsellers
                Intent intent = new Intent(MarketNewBooksActivity.this, MarketBestsellActivity.class);
                startActivity(intent);
            }
        });
    }

    private void fetchBooksFromDatabase() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Book book = postSnapshot.getValue(Book.class);
                    if (book != null && Integer.parseInt(book.getYear()) >= 1990) {
                        bookList.add(book);
                    }
                }
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок
            }
        });
    }

    // GestureDetector для обработки свайпов
    private class SwipeGestureDetector extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float diffX = e2.getX() - e1.getX();
            float diffY = e2.getY() - e1.getY();

            if (Math.abs(diffX) > Math.abs(diffY)) {
                if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffX > 0) {
                        // Свайп вправо
                        onSwipeRight();
                    } else {
                        // Свайп влево
                        onSwipeLeft();
                    }
                    return true;
                }
            }
            return false;
        }
    }

    private void onSwipeLeft() {
        // Переход на MarketActivity при свайпе влево
        Intent intent = new Intent(this,MarketBestsellActivity.class);
        startActivity(intent);
    }

    private void onSwipeRight() {
        // Переход на MarketBestsellActivity при свайпе вправо
        Intent intent = new Intent(this, MarketActivity.class);
        startActivity(intent);
    }
}
