package com.example.bookberry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import javax.security.auth.login.LoginException;

public class MainActivity16 extends AppCompatActivity
{
    ImageButton button1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_book);
        button1 = findViewById(R.id.back_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity16.this, MainActivity5.class);

                startActivity(intent);
            }
        });
    }
}
