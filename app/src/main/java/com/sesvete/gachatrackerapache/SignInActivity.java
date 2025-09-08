package com.sesvete.gachatrackerapache;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.credentials.CredentialManager;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.sesvete.gachatrackerapache.fragment.LanguageSettingsFragment;
import com.sesvete.gachatrackerapache.helper.AuthenticationHelper;
import com.sesvete.gachatrackerapache.helper.LocaleHelper;


public class SignInActivity extends AppCompatActivity {

    private MaterialButton btnSignInWithGoogle;
    private MaterialButton btnSignInWithEmail;
    private FirebaseAuth mAuth;
    private CredentialManager credentialManager;

    //basically zagotovimo, da se locale posodobi preden se izgradi main activity

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        mAuth = FirebaseAuth.getInstance();

        credentialManager = CredentialManager.create(getBaseContext());

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.language_fragment_container, LanguageSettingsFragment.class, null).setReorderingAllowed(true).commit();

        btnSignInWithEmail = findViewById(R.id.btn_sign_in_password);
        // basically se bo to delalo na novem activity

        btnSignInWithGoogle = findViewById(R.id.btn_sign_in_google);


        btnSignInWithEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignInWithPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });

        btnSignInWithGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AuthenticationHelper.launchCredentialManager(getResources(), credentialManager, mAuth, SignInActivity.this);
            }
        });

    }
}
