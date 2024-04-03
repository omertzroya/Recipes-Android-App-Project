package com.example.recipesapp.activities;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.recipesapp.databinding.ActivityAddRecipeBinding;
import com.example.recipesapp.models.Category;
import com.example.recipesapp.models.Recipe;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class AddRecipeActivity extends AppCompatActivity {

  private boolean isImageSelected = false;
  private Bitmap bitmap;

  private ProgressDialog dialog;
  ActivityAddRecipeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding= ActivityAddRecipeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        loadCategories();

        binding.btnAddRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getData();
            }
        });

        binding.imgRecipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               pickImage();
            }
        });
    }

    private void loadCategories() {
        List<String> categories = new ArrayList<>();
        // Instead of writing code, we use CHat GPT to generate code.
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, categories);
        binding.etCategory.setAdapter(adapter);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Categories");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.hasChildren()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        categories.add(dataSnapshot.getValue(Category.class).getName());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void pickImage() {

        PickImageDialog.build(new PickSetup()).show(AddRecipeActivity.this).setOnPickResult(r-> {
            binding.imgRecipe.setImageBitmap(r.getBitmap());
            bitmap = r.getBitmap();
            binding.imgRecipe.setScaleType((ImageView.ScaleType.CENTER_CROP));
            isImageSelected = true;

        }).setOnPickCancel(()->{
            Toast.makeText(this, "Cancelled", Toast.LENGTH_SHORT).show();
        });

    }

    private void getData() {
        // Fetch All the data from the user in variables.
        String recipeName = Objects.requireNonNull(binding.etRecipeName.getText()).toString();
        String recipeDescription = Objects.requireNonNull(binding.etDescription.getText()).toString();
        String cookingTime = Objects.requireNonNull(binding.etCookingTime.getText()).toString();
        String recipeCategory = binding.etCategory.getText().toString();
        String calories = Objects.requireNonNull(binding.etCalories.getText()).toString();

        // 2. We will validate the data.
        if (recipeName.isEmpty()) {
            binding.etRecipeName.setError("Please enter Recipe Name");
        } else if (recipeDescription.isEmpty()) {
            binding.etDescription.setError("Please enter Recipe Description");
        } else if (cookingTime.isEmpty()) {
            binding.etCookingTime.setError("Please enter Cooking Time");
        } else if (recipeCategory.isEmpty()) {
            binding.etCategory.setError("Please enter Recipe Category");
        } else if (calories.isEmpty()) {
            binding.etCalories.setError("Please enter Calories");
        } else if (!isImageSelected) {
            Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show();
        } else {
            // 3. We will create a Recipe Object.
            // ID will be auto generated.
            dialog = new ProgressDialog(this);
            dialog.setMessage("Uploading Recipe...");
            dialog.setCancelable(false);
            dialog.show();
            Recipe recipe = new Recipe(recipeName, recipeDescription, cookingTime, recipeCategory, calories, "", FirebaseAuth.getInstance().getUid());
            // We also need to pick image and make sure it is not null.
            // 5. We will upload the image to the firebase storage.
            uploadImage(recipe);
        }


    }

    private String uploadImage(Recipe recipe) {
       final String[] url = {""};

       String id = System.currentTimeMillis() + "";
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("images/"+ id+ "_recipe.jpg");;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = storageRef.putBytes(data);

        uploadTask.continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return storageRef.getDownloadUrl();
        }).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Uri downloadUri = task.getResult();
                url[0] = downloadUri.toString();
                Toast.makeText(AddRecipeActivity.this, "Image Uploaded Successfully", Toast.LENGTH_SHORT).show();
                saveDataInDataBase(recipe, url[0]);

            } else {
                Log.e("ProfileFragment", "onComplete: " + Objects.requireNonNull(task.getException()).getMessage());
            }
        });
        return url[0];

    }

    private void  saveDataInDataBase (Recipe recipe, String url) {
        recipe.setImage(url);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Recipes");
        String id = reference.push().getKey();
        recipe.setId(id);
        if(id!=null){
            reference.child(id).setValue(recipe).addOnCompleteListener(task -> {
                if(task.isSuccessful()){
                    dialog.dismiss();
                    Toast.makeText(AddRecipeActivity.this, "Recipe Added Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                }else{
                    dialog.dismiss();
                    Toast.makeText(AddRecipeActivity.this, "Failed to add Recipe", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}