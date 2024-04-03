package com.example.recipesapp.adapters;

// RecipeFavoriteAdapter.java
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.recipesapp.R;
import com.example.recipesapp.models.RecipeFavorite;

import java.util.List;

public class RecipeFavoriteAdapter extends RecyclerView.Adapter<RecipeFavoriteAdapter.RecipeFavoriteViewHolder> {

    private List<RecipeFavorite> recipeFavoriteList;

    public RecipeFavoriteAdapter(List<RecipeFavorite> recipeFavoriteList) {
        this.recipeFavoriteList = recipeFavoriteList;
    }

    @NonNull
    @Override
    public RecipeFavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recipe_favorite, parent, false);
        return new RecipeFavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeFavoriteViewHolder holder, int position) {
        RecipeFavorite recipeFavorite = recipeFavoriteList.get(position);
        holder.bind(recipeFavorite);
    }

    @Override
    public int getItemCount() {
        return recipeFavoriteList.size();
    }

    public class RecipeFavoriteViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView linkTextView;

        public RecipeFavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            linkTextView = itemView.findViewById(R.id.linkTextView);
        }

        public void bind(RecipeFavorite recipeFavorite) {
            nameTextView.setText(recipeFavorite.getName());
            linkTextView.setText(recipeFavorite.getLink());
        }
    }
}
