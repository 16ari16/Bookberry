package com.example.bookberry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfileActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference usersRef;
    private TextView usernameTextView;
    private TextView emailTextView;
    private User currentUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();

        if (firebaseUser == null) {
            // User is not authenticated, redirect to LoginActivity
            startActivity(new Intent(UserProfileActivity.this, LoginActivity.class));
            finish();
            return;
        }

        usersRef = FirebaseDatabase.getInstance().getReference("users");

        usernameTextView = findViewById(R.id.username);
        emailTextView = findViewById(R.id.email);
        ImageView profileImageView = findViewById(R.id.profile_image);

        // Fetch user details from the database
        usersRef.child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                currentUserData = snapshot.getValue(User.class);
                if (currentUserData != null) {
                    usernameTextView.setText(currentUserData.getUserLogin());
                    emailTextView.setText(currentUserData.getUserEmail());
                    // Assuming you have a profile image URL in the User object
                    // Picasso.get().load(currentUserData.getProfileImageUrl()).into(profileImageView);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Failed to load user details.", Toast.LENGTH_SHORT).show();
            }
        });

        Button changeButton = findViewById(R.id.change_button);
        Button exitButton = findViewById(R.id.exit_button);

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Go to UserChangeInfoActivity
                Intent intent = new Intent(UserProfileActivity.this, UserChangeInfoActivity.class);
                intent.putExtra("user", currentUserData);
                startActivity(intent);
            }
        });

        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Sign out and go to MarketActivity
                auth.signOut();
                startActivity(new Intent(UserProfileActivity.this, MarketActivity.class));
                finish();
            }
        });

        ImageButton navHome = findViewById(R.id.nav_home);
        ImageButton navSearch = findViewById(R.id.nav_search);
        ImageButton navLibrary = findViewById(R.id.nav_library);
        ImageButton navProfile = findViewById(R.id.nav_profile);

        navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent homeIntent = new Intent(UserProfileActivity.this, MarketActivity.class);
                startActivity(homeIntent);
            }
        });

        navSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent searchIntent = new Intent(UserProfileActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        navLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent libraryIntent = new Intent(UserProfileActivity.this, FavBooksActivity.class);
                startActivity(libraryIntent);
            }
        });

        navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Already in UserProfileActivity, do nothing or refresh
            }
        });
    }
}
