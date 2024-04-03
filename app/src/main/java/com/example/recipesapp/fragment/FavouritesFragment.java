package com.example.recipesapp.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.recipesapp.R;
import com.example.recipesapp.adapters.RecipeFavoriteAdapter;
import com.example.recipesapp.models.RecipeFavorite;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavouritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavouritesFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecipeFavoriteAdapter recipeFavoriteAdapter;
    private List<RecipeFavorite> recipeFavoriteList;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FavouritesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FavouritesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FavouritesFragment newInstance(String param1, String param2) {
        FavouritesFragment fragment = new FavouritesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);

        recyclerView = view.findViewById(R.id.recipesRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recipeFavoriteList = new ArrayList<>();
        recipeFavoriteList.add(new RecipeFavorite("חזה עוף על האש", "https://www.recipe1.com"));
        recipeFavoriteList.add(new RecipeFavorite("פסטה שמנת פטריות", "https://www.recipe2.com"));

        recipeFavoriteAdapter = new RecipeFavoriteAdapter(recipeFavoriteList);
        recyclerView.setAdapter(recipeFavoriteAdapter);

        view.findViewById(R.id.addRecipeButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddRecipeDialog();
            }
        });

        return view;
    }

    private void showAddRecipeDialog() {
        // Inflate the dialog layout
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_recipe_favorite, null);

        // Initialize dialog components
        EditText recipeNameEditText = dialogView.findViewById(R.id.recipeNameEditText);
        EditText recipeLinkEditText = dialogView.findViewById(R.id.recipeLinkEditText);
        Button addButton = dialogView.findViewById(R.id.addButton);

        // Create and show the dialog
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(requireContext());
        builder.setView(dialogView);
        final androidx.appcompat.app.AlertDialog dialog = builder.create();
        dialog.show();

        // Set OnClickListener for the "Add" button
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve name and link from EditText fields
                String name = recipeNameEditText.getText().toString();
                String link = recipeLinkEditText.getText().toString();

                // Add the new recipe to the list and update the RecyclerView
                addNewRecipe(name, link);

                // Dismiss the dialog
                dialog.dismiss();
            }
        });
    }
    private void addNewRecipe(String name, String link) {
        RecipeFavorite newRecipe = new RecipeFavorite(name, link);
        recipeFavoriteList.add(newRecipe);
        recipeFavoriteAdapter.notifyDataSetChanged();
    }
}
