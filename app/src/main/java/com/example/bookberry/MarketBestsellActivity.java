package com.example.bookberry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MarketBestsellActivity extends AppCompatActivity implements BookAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private BookAdapter bookAdapter;
    private List<Book> bookList;
    private DatabaseReference databaseReference;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_bestsell);

        // Устанавливаем отступы под системные панели
        View mainView = findViewById(R.id.main);
        mainView.setOnApplyWindowInsetsListener((v, insets) -> {
            v.setPadding(insets.getSystemWindowInsetLeft(), insets.getSystemWindowInsetTop(),
                    insets.getSystemWindowInsetRight(), insets.getSystemWindowInsetBottom());
            return insets.consumeSystemWindowInsets();
        });

        // Скрываем ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Инициализируем RecyclerView и настраиваем LayoutManager
        recyclerView = findViewById(R.id.recycler_view_bestsellers);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        // Инициализируем список книг и адаптер
        bookList = new ArrayList<>();
        bookAdapter = new BookAdapter(this, bookList, this);
        recyclerView.setAdapter(bookAdapter);

        // Получаем ссылку на Firebase Realtime Database
        databaseReference = FirebaseDatabase.getInstance().getReference("books");

        // Читаем данные из Firebase
        fetchBooksFromDatabase("");

        // Находим SearchView и устанавливаем слушатель для изменения текста поиска
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Вызываем метод загрузки данных с учетом нового текста поиска
                fetchBooksFromDatabase(newText);
                return true;
            }
        });

        // Находим TextView для разделов и устанавливаем OnClickListener
        TextView sectionMainBooks = findViewById(R.id.section_mainBooks);
        TextView sectionNewBooks = findViewById(R.id.section_newBooks);

        sectionMainBooks.setOnClickListener(v -> {
            // Переходим на MarketActivity при нажатии на section_mainBooks
            Intent intent = new Intent(MarketBestsellActivity.this, MarketActivity.class);
            startActivity(intent);
        });

        sectionNewBooks.setOnClickListener(v -> {
            // Переходим на MarketNewBooksActivity при нажатии на section_newBooks
            Intent intent = new Intent(MarketBestsellActivity.this, MarketNewBooksActivity.class);
            startActivity(intent);
        });

        // Находим кнопки нижней навигации и устанавливаем OnClickListener
        ImageButton navHome = findViewById(R.id.nav_home);
        ImageButton navSearch = findViewById(R.id.nav_search);
        ImageButton navLibrary = findViewById(R.id.nav_library);
        ImageButton navProfile = findViewById(R.id.nav_profile);

        navHome.setOnClickListener(v -> {
            Intent intent = new Intent(MarketBestsellActivity.this, MarketActivity.class);
            startActivity(intent);
        });

        navSearch.setOnClickListener(v -> {
            Intent intent = new Intent(MarketBestsellActivity.this, SearchActivity.class);
            startActivity(intent);
        });

        navLibrary.setOnClickListener(v -> {
            Intent intent = new Intent(MarketBestsellActivity.this, FavBooksActivity.class);
            startActivity(intent);
        });

        navProfile.setOnClickListener(v -> {
            Intent intent = new Intent(MarketBestsellActivity.this, UserProfileActivity.class);
            startActivity(intent);
        });
    }

    private void fetchBooksFromDatabase(String searchText) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Book book = postSnapshot.getValue(Book.class);
                    if (book != null) {
                        try {
                            double rating = Double.parseDouble(book.getRating());
                            if (rating >= 4.0 && book.getName().toLowerCase().contains(searchText.toLowerCase())) {
                                bookList.add(book);
                            }
                        } catch (NumberFormatException e) {
                            // Обработка случая, если рейтинг некорректно представлен как строка
                            // Здесь можно добавить логирование или уведомление об ошибке
                        }
                    }
                }
                bookAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Обработка ошибок при чтении данных из Firebase
                Toast.makeText(MarketBestsellActivity.this, "Ошибка загрузки данных", Toast.LENGTH_SHORT).show();
            }
        });
    }



    @Override
    public void onItemClick(Book book) {
        // Обработка клика по элементу списка книг
        // Проверяем, куплена ли книга
        if (isBookPurchased(book.getId())) {
            // Если книга уже куплена, открываем экран деталей
            Intent intent = new Intent(this, FavBooksActivity.class);
            intent.putExtra("book", book);
            startActivity(intent);
        } else {
            // Если книга не куплена, открываем экран покупки
            Intent intent = new Intent(this, BookBuyActivity.class);
            intent.putExtra("book", book);
            startActivityForResult(intent, 1);
        }
    }

    // Метод для проверки, куплена ли книга
    private boolean isBookPurchased(String bookId) {
        // Здесь должна быть ваша логика проверки в базе данных или SharedPreferences
        // В данном случае всегда возвращаем false, так как логику покупки не реализована
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Обработка результата из BookBuyActivity
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String purchasedBookId = data.getStringExtra("purchasedBookId");
            if (purchasedBookId != null) {
                markBookAsPurchased(purchasedBookId);
            }
        }
    }

    // Метод для пометки книги как купленной
    private void markBookAsPurchased(String bookId) {
        // Здесь должна быть ваша логика сохранения информации о покупке
        // В данном случае просто выводим сообщение
        Toast.makeText(this, "Книга куплена успешно!", Toast.LENGTH_SHORT).show();
        // Обновляем список книг после покупки
        fetchBooksFromDatabase("");
    }
}
