package com.example.bookberry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserChangeInfoActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private DatabaseReference usersRef;
    private FirebaseUser currentUser;

    private EditText newNicknameEditText;
    private EditText oldPasswordEditText;
    private EditText newPasswordEditText;
    private Button updateDataButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change_info);
        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        usersRef = FirebaseDatabase.getInstance().getReference("users");

        newNicknameEditText = findViewById(R.id.new_nickname);
        oldPasswordEditText = findViewById(R.id.old_password);
        newPasswordEditText = findViewById(R.id.new_password);
        updateDataButton = findViewById(R.id.update_data_button);

        updateDataButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateUserInfo();
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

    private void updateUserInfo() {
        String newNickname = newNicknameEditText.getText().toString().trim();
        String oldPassword = oldPasswordEditText.getText().toString().trim();
        String newPassword = newPasswordEditText.getText().toString().trim();

        if (newNickname.isEmpty() || oldPassword.isEmpty() || newPassword.isEmpty()) {
            Toast.makeText(UserChangeInfoActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        AuthCredential credential = EmailAuthProvider.getCredential(currentUser.getEmail(), oldPassword);
        currentUser.reauthenticate(credential).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                currentUser.updatePassword(newPassword).addOnCompleteListener(passwordUpdateTask -> {
                    if (passwordUpdateTask.isSuccessful()) {
                        usersRef.child(currentUser.getUid()).child("userLogin").setValue(newNickname)
                                .addOnCompleteListener(nicknameUpdateTask -> {
                                    if (nicknameUpdateTask.isSuccessful()) {
                                        Toast.makeText(UserChangeInfoActivity.this, "User information updated", Toast.LENGTH_SHORT).show();
                                        navigateToSuccessChngsActivity();
                                    } else {
                                        Toast.makeText(UserChangeInfoActivity.this, "Failed to update nickname", Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        Toast.makeText(UserChangeInfoActivity.this, "Failed to update password", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(UserChangeInfoActivity.this, "Authentication failed. Check your old password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateToSuccessChngsActivity() {
        Intent intent = new Intent(UserChangeInfoActivity.this, SuccessChngsActivity.class);
        startActivity(intent);
        finish();
    }
}
