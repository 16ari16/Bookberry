package com.example.bookberry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso; // Assuming you use Picasso for image loading

public class BookBuyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_buy);

        // Получение объекта Book из Intent
        Intent intent = getIntent();
        Book book = (Book) intent.getSerializableExtra("book");

        // Отображение информации о книге
        ImageView bookCover = findViewById(R.id.book_cover);
        TextView bookTitle = findViewById(R.id.book_title);
        TextView bookAuthor = findViewById(R.id.book_author);
        TextView rating = findViewById(R.id.rating);

        bookTitle.setText(book.getName());
        bookAuthor.setText(book.getAuthor());
        rating.setText(book.getRating() + "★");

        // Загрузка обложки книги (используйте Picasso или Glide для загрузки изображений)
        Picasso.get().load(book.getUrl()).into(bookCover);

        // Настройка кнопки "Купить"
        Button buyButton = findViewById(R.id.buy_button);
        buyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Логика покупки книги
                buyBook(book);
            }
        });

        // Настройка кнопки "Написать отзыв"
        Button opinionButton = findViewById(R.id.opinion_button);
        opinionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Логика написания отзыва
                writeReview(book);
            }
        });
    }

    // Метод для симуляции покупки книги
    private void buyBook(Book book) {
        // Здесь может быть ваша логика покупки книги
        Toast.makeText(this, "Куплена книга: " + book.getName(), Toast.LENGTH_SHORT).show();
    }

    // Метод для симуляции написания отзыва
    private void writeReview(Book book) {
        // Здесь может быть ваша логика написания отзыва
        Toast.makeText(this, "Написан отзыв для книги: " + book.getName(), Toast.LENGTH_SHORT).show();
    }
}
