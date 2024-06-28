package com.example.bookberry;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class BookBuyActivity extends AppCompatActivity {

    private FirebaseUser currentUser;
    private DatabaseReference purchasesRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_buy);
        getSupportActionBar().hide();

        Intent intent = getIntent();
        Book book = intent.getParcelableExtra("book");

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Пользователь не был передан. Переход на страницу входа.", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(BookBuyActivity.this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
            return;
        }

        purchasesRef = FirebaseDatabase.getInstance().getReference("purchases");

        TextView bookName = findViewById(R.id.book_title);
        TextView bookAuthor = findViewById(R.id.book_author);
        TextView bookPrice = findViewById(R.id.book_price);
        ImageView bookImage = findViewById(R.id.book_cover);
        Button purchaseButton = findViewById(R.id.buy_button);

        bookName.setText(book.getName());
        bookAuthor.setText(book.getAuthor());
        bookPrice.setText(String.valueOf(book.getPrice()) + " руб");
        Picasso.get().load(book.getUrl()).into(bookImage);
        Button opinionButton = findViewById(R.id.opinion_button);
        opinionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent reviewIntent = new Intent(BookBuyActivity.this, ReviewActivity.class);
                reviewIntent.putExtra("book", book); // Pass the current book object
                startActivity(reviewIntent);
            }
        });
        Button opinionsButton = findViewById(R.id.opinions_button);
        opinionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent usersReviewsIntent = new Intent(BookBuyActivity.this, UsersReviewsActivity.class);
                // Pass book name to UsersReviewsActivity
                usersReviewsIntent.putExtra("bookName", book.getName());
                startActivity(usersReviewsIntent);
            }
        });


        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                purchaseBook(book);
            }
        });

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void purchaseBook(Book book) {
        // Check if the user has already purchased this book
        String userId = currentUser.getUid();
        String bookId = book.getId();
        String userIdBookId = userId + "_" + bookId;

        purchasesRef.child(userIdBookId).runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                if (mutableData.getValue() != null) {
                    // Book has already been purchased by the user
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run () {
                            Toast.makeText(BookBuyActivity.this, "Вы уже купили эту книгу", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    // Proceed with the purchase
                    Purchase purchase = new Purchase(userId, bookId, false);
                    mutableData.setValue(purchase);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BookBuyActivity.this, "Покупка успешна", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(@NonNull DatabaseError databaseError, boolean committed, @NonNull DataSnapshot dataSnapshot) {
                if (databaseError != null) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(BookBuyActivity.this, "Транзакция не удалась: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


}
