package com.sesvete.gachatrackerapache;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.sesvete.gachatrackerapache.helper.DialogHelper;
import com.sesvete.gachatrackerapache.helper.LocaleHelper;

public class SignInWithPasswordActivity extends AppCompatActivity {


    private EditText editTextSignInPasswordEmail;
    private EditText editTextSignInPasswordPassword;
    private MaterialButton btnSignInPasswordSignIn;
    private MaterialButton btnSignInPasswordNewUser;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_with_password);

        DialogHelper.keyboardTouchListener(findViewById(R.id.main_sign_in_with_password), this);


        editTextSignInPasswordEmail = findViewById(R.id.edit_text_sign_in_password_email);
        editTextSignInPasswordPassword = findViewById(R.id.edit_text_sign_in_password_password);
        btnSignInPasswordSignIn = findViewById(R.id.btn_sign_in_password_sign_in);
        btnSignInPasswordNewUser = findViewById(R.id.btn_sign_in_password_new_user);

        btnSignInPasswordSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextSignInPasswordEmail.getText().toString();
                String password = editTextSignInPasswordPassword.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignInWithPasswordActivity.this, getString(R.string.fill_all_forms), Toast.LENGTH_SHORT).show();
                } else {
                    // TODO: check if user exists and sign in user
                    // za zdaj ta funkcija samo preusmeri na main activity

                    Intent intent = new Intent(SignInWithPasswordActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
        });

        btnSignInPasswordNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInWithPasswordActivity.this, CreateAccountActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }
}