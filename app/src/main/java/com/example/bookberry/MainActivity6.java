package com.example.bookberry;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import javax.security.auth.login.LoginException;

public class MainActivity6 extends AppCompatActivity
{
   Button button1;
   Button button2;
   ImageButton button3;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        button1 = findViewById(R.id.register_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity6.this, MainActivity3.class);

                startActivity(intent);
            }
        });
        button2 = findViewById(R.id.login_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity6.this, MainActivity2.class);

                startActivity(intent);
            }
        });
        button3 = findViewById(R.id.back_button);
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity6.this, MainActivity1.class);

                startActivity(intent);
            }
        });
    }
}
