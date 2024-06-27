package com.example.bookberry;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> bookList;

    public BookAdapter(Context context, List<Book> bookList) {
        this.context = context;
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_bookitem, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.textViewTitle.setText(book.getName());
        holder.textViewAuthor.setText(book.getAuthor());
        holder.textViewRating.setText(book.getRating() +" ★");
        Picasso.get().load(book.getUrl()).into(holder.imageViewBook);

        // Обработчик для кнопки "Приобрести"
        holder.buttonPurchase.setOnClickListener(v -> {
            // Логика покупки книги
        });
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {

        TextView textViewTitle, textViewAuthor, textViewRating;
        ImageView imageViewBook;
        Button buttonPurchase;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);

            textViewTitle = itemView.findViewById(R.id.textViewTitle);
            textViewAuthor = itemView.findViewById(R.id.textViewAuthor);
            textViewRating = itemView.findViewById(R.id.textViewRating);
            imageViewBook = itemView.findViewById(R.id.imageViewBook);
            buttonPurchase = itemView.findViewById(R.id.buttonPurchase);
        }
    }
}
