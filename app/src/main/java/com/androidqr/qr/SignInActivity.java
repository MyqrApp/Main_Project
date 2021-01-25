package com.androidqr.qr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SignInActivity extends AppCompatActivity {

    EditText usernameet, passwordet;
    String user_name, pass, type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        usernameet = (EditText)findViewById(R.id.input_email);
        passwordet = (EditText)findViewById(R.id.input_password);

        //for back arrow
        ImageView back = (ImageView) findViewById(R.id.back_arrow);
        back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent backIntent = new Intent(SignInActivity.this, dashboardSignedOut.class);

                // Start the new activity
                startActivity(backIntent);
                finish();
            }
        });

        Button login = (Button) findViewById(R.id.login);

        // Set a click listener on that View
        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent loginIntent = new Intent(SignInActivity.this, dashboardSignedIn.class);

                // Start the new activity
                startActivity(loginIntent);
                finish();
            }
        });
        TextView signup = (TextView) findViewById(R.id.signup);

        // Set a click listener on that View
        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent signupIntent = new Intent(SignInActivity.this, SignUpActivity.class);

                // Start the new activity
                startActivity(signupIntent);
                finish();
            }
        });
    }

    public void loginFun(View view) {
        user_name  = usernameet.getText().toString();
        pass = passwordet.getText().toString();
        type = "login";
        BackgroundWork backgroundWork = new BackgroundWork(this);
        backgroundWork.execute(type, user_name, pass);
    }
}