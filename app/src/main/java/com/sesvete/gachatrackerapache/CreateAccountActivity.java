package com.sesvete.gachatrackerapache;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.sesvete.gachatrackerapache.helper.AuthenticationHelper;
import com.sesvete.gachatrackerapache.helper.DialogHelper;
import com.sesvete.gachatrackerapache.helper.LocaleHelper;

public class CreateAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private EditText editTextRegisterEmail;
    private EditText editTextRegisterPassword;
    private EditText editTextRegisterPasswordReenter;
    private MaterialButton btnRegisterCreate;
    private MaterialButton btnRegisterBack;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        DialogHelper.keyboardTouchListener(findViewById(R.id.main_register), this);

        mAuth = FirebaseAuth.getInstance();

        editTextRegisterEmail = findViewById(R.id.edit_text_register_email);
        editTextRegisterPassword = findViewById(R.id.edit_text_register_password);
        editTextRegisterPasswordReenter = findViewById(R.id.edit_text_register_password_reenter);
        btnRegisterBack = findViewById(R.id.btn_register_back);
        btnRegisterCreate = findViewById(R.id.btn_register_create);

        btnRegisterCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextRegisterEmail.getText().toString();
                String password = editTextRegisterPassword.getText().toString();
                String passwordReenter = editTextRegisterPasswordReenter.getText().toString();

                if (email.isEmpty() || password.isEmpty() || passwordReenter.isEmpty()){
                    Toast.makeText(CreateAccountActivity.this, getString(R.string.fill_all_forms), Toast.LENGTH_SHORT).show();
                } else if (!password.equals(passwordReenter)) {
                    Toast.makeText(CreateAccountActivity.this, getString(R.string.mismatch_password), Toast.LENGTH_SHORT).show();
                } else if (password.length() <= 6) {
                    Toast.makeText(CreateAccountActivity.this, getString(R.string.short_password), Toast.LENGTH_SHORT).show();
                } else {
                    AuthenticationHelper.createUserWithEmailAndPassword(mAuth, CreateAccountActivity.this, getResources(), email, password);
                }

            }
        });

        btnRegisterBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateAccountActivity.this, SignInWithPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}