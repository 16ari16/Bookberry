package com.example.bookberry;

        import android.content.Intent;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import androidx.appcompat.app.AppCompatActivity;

        import javax.security.auth.login.LoginException;

public class MainActivity2 extends AppCompatActivity
{
    Button button1;
    Button button2;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        button1 = findViewById(R.id.login_button);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity2.this, MainActivity3.class);

                startActivity(intent);
            }
        });
        button2 = findViewById(R.id.reg_button);
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(MainActivity2.this, MainActivity6.class);

                startActivity(intent);
            }
        });
    }
}


