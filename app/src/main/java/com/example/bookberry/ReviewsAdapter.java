package com.example.bookberry;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewViewHolder> {

    private ArrayList<Review> reviewsList;

    public ReviewsAdapter(ArrayList<Review> reviewsList) {
        this.reviewsList = reviewsList;
    }

    @NonNull
    @Override
    public ReviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item, parent, false);
        return new ReviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewViewHolder holder, int position) {
        Review review = reviewsList.get(position);
        holder.bookName.setText("Book: "+review.getBookName());
        holder.author.setText("Author: "+review.getAuthor());
        holder.rating.setText("Rating: " +review.getRating() + " ★");
        holder.description.setText(review.getDescription());
    }

    @Override
    public int getItemCount() {
        return reviewsList.size();
    }

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView bookName, author, rating, description;

        public ReviewViewHolder(@NonNull View itemView) {
            super(itemView);
            bookName = itemView.findViewById(R.id.review_book_name);
            author = itemView.findViewById(R.id.review_author);
            rating = itemView.findViewById(R.id.review_rating);
            description = itemView.findViewById(R.id.review_description);
        }
    }
}
