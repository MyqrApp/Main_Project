package com.androidqr.qr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    EditText etusername, etemail, etpassword;
    String username, email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);
        etusername = (EditText)findViewById(R.id.username);
        etemail = (EditText)findViewById(R.id.input_email);
        etpassword = (EditText)findViewById(R.id.input_password);

        //for back arrow
        ImageView back = (ImageView) findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent backIntent = new Intent(SignUpActivity.this, dashboardSignedOut.class);

                // Start the new activity
                startActivity(backIntent);
                finish();
            }
        });

        TextView login = (TextView) findViewById(R.id.login);

        // Set a click listener on that View
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent loginIntent = new Intent(SignUpActivity.this, SignInActivity.class);

                // Start the new activity
                startActivity(loginIntent);
                finish();
            }
        });
    }

    public void RegUser(View view) {
        username  = etusername.getText().toString();
        email  = etemail.getText().toString();
        password = etpassword.getText().toString();
        String type = "register";
        BackgroundWork backgroundWork = new BackgroundWork(this);
        backgroundWork.execute(type, username, email, password);
    }
}