package com.example.bookberry;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Addbook extends AppCompatActivity {

    private EditText editTextName, editTextAuthor, editTextYear, editTextPrice, editTextRating, editTextPhoto;
    private Button buttonAddBook;
    private DatabaseReference databaseBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addbook);

        editTextName = findViewById(R.id.editTextName);
        editTextAuthor = findViewById(R.id.editTextAuthor);
        editTextYear = findViewById(R.id.editTextYear);
        editTextPrice = findViewById(R.id.editTextPrice);
        editTextRating = findViewById(R.id.editTextRating);
        editTextPhoto = findViewById(R.id.editTextPhoto);
        buttonAddBook = findViewById(R.id.buttonAddBook);


        databaseBooks = FirebaseDatabase.getInstance().getReference("books");

        buttonAddBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBook();
            }
        });
    }

    private void addBook() {
        String name = editTextName.getText().toString().trim();
        String author = editTextAuthor.getText().toString().trim();
        String year = editTextYear.getText().toString().trim();
        String price = editTextPrice.getText().toString().trim();
        String rating = editTextPrice.getText().toString().trim();
        String urlphoto = editTextPhoto.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(author) && !TextUtils.isEmpty(year) && !TextUtils.isEmpty(price)) {
            String id = databaseBooks.push().getKey();
            Book book = new Book(id, name, author, year, price, rating,urlphoto);
            if (id != null) {
                databaseBooks.child(id).setValue(book);
                Toast.makeText(this, "Книга добавлена", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Ошибка добавления книги", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Пожалуйста, заполните все поля", Toast.LENGTH_SHORT).show();
        }
    }
}