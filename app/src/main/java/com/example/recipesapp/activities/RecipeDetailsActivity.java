package com.example.recipesapp.activities;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.recipesapp.R;
import com.example.recipesapp.databinding.ActivityRecipeDetailsBinding;
import com.example.recipesapp.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class RecipeDetailsActivity extends AppCompatActivity {

    ActivityRecipeDetailsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecipeDetailsBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_recipe_details);
        init();

    }

    private void init() {
       Recipe recipe = (Recipe) getIntent().getSerializableExtra("recipe");


        TextView tvName = findViewById(R.id.tv_name1);
        TextView tvCategory = findViewById(R.id.tv_category);
        TextView tvCalories = findViewById(R.id.tv_calories);
        TextView tvDescription = findViewById(R.id.tv_description);

         tvName.setText(recipe.getName());
         tvCategory.setText(recipe.getCategory());
         tvCalories.setText(recipe.getCalories()+"קלוריות " );
         tvDescription.setText(recipe.getDescription());


        String imageUrl = recipe.getImage();
        ImageView imageView = findViewById(R.id.img_recipe1); // assuming you have defined an ImageView in your layout XML with the id "imageView"


        new DownloadImageTask(imageView).execute(imageUrl);


    }
    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        public DownloadImageTask(ImageView imageView) {
            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String imageUrl = urls[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                bitmap = BitmapFactory.decodeStream(inputStream);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                imageView.setImageBitmap(result);
            }
        }
    }
}