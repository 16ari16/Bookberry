package com.example.bookberry;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context mContext;
    private List<Book> bookList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public BookAdapter(Context context, List<Book> books, OnItemClickListener listener) {
        this.mContext = context;
        this.bookList = books;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.activity_bookitem, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.textViewTitle.setText(book.getName());
        holder.textViewAuthor.setText(book.getAuthor());
        holder.textViewRating.setText(String.format("Rating: %s", book.getRating()));
        holder.textViewPrice.setText(String.format("Price: %s", book.getPrice()+" руб"));

        // Загрузка изображения с помощью Picasso
        Picasso.get()
                .load(book.getUrl())
                .placeholder(R.drawable.grayavatar) // Замените на ваш placeholder
                .error(R.drawable.grayavatar) // Замените на ваш error image
                .resize(180, 220) // Установить размеры изображения
                .centerInside()
                .into(holder.imageViewBook);

        // Установить клик слушатель для элемента RecyclerView
        holder.itemView.setOnClickListener(v -> mListener.onItemClick(book));

        // Установить клик слушатель для кнопки "Приобрести"
        holder.buttonPurchase.setOnClickListener(v -> {
            // Check if user is authenticated
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {
                // User is signed in, proceed to purchase
                mListener.onItemClick(book);
            } else {
                // User is not signed in, redirect to login
                Intent intent = new Intent(mContext, LoginActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewAuthor, textViewRating, textViewPrice;
        ImageView imageViewBook;
        Button buttonPurchase;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            textViewPrice = itemView.findViewById(R.id.textViewPrice);
            imageViewBook = itemView.findViewById(R.id.imageViewBook);
            buttonPurchase = itemView.findViewById(R.id.buttonPurchase);
        }
    }
}
