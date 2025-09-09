/*

TODO: Trenutno je to naštimano za prijavo v firebase
TODO: naredi nov helper za prijavo na naš strežnik

package com.sesvete.gachatrackerapache.helper;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.credentials.ClearCredentialStateRequest;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.credentials.exceptions.ClearCredentialException;
import androidx.credentials.exceptions.GetCredentialException;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.sesvete.gachatrackerapache.MainActivity;
import com.sesvete.gachatrackerapache.R;
import com.sesvete.gachatrackerapache.SignInActivity;

import java.util.concurrent.Executors;

public class AuthenticationHelper {

    public static void launchCredentialManager(Resources resources, CredentialManager credentialManager, FirebaseAuth mAuth, Activity activity){
        // Instantiate a Google sign-in request
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                // for some reason setFilterByAuthorizedAccounts(true) does not work
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(resources.getString(R.string.web_client_id))
                .build();

        // Create the Credential Manager request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        // END create_credential_manager_request

        credentialManager.getCredentialAsync(
                activity,
                request,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<>() {
                    @Override
                    public void onResult(GetCredentialResponse result) {
                        handleSignIn(result.getCredential(), mAuth, activity);
                    }

                    @Override
                    public void onError(@NonNull GetCredentialException e) {
                        Log.e("Authentication", "Couldn't retrieve user's credentials: " + e.getLocalizedMessage());
                    }
                }

        );


    }
    private static void handleSignIn(Credential credential, FirebaseAuth mAuth, Activity activity){
        if (credential instanceof CustomCredential customCredential && credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)){
            Bundle credentialData = customCredential.getData();
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);
            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken(), mAuth, activity);
        }
        else {
            Log.d("Sign in", "Credential is not of type Google ID!");
        }
    }

    private static void firebaseAuthWithGoogle(String idToken, FirebaseAuth mAuth, Activity activity){
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(activity, MainActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                        // Sign in success, update UI with the signed-in user's information
                        Log.d("SignInGoogle", "signInWithCredential:success");
                    } else {
                        // If sign in fails, display a message to the user
                        Log.w("SignInGoogle", "signInWithCredential:failure", task.getException());
                    }
                });

    }

    public static void logOut(FirebaseAuth mAuth, CredentialManager credentialManager, Activity activity){
        mAuth.signOut();

        ClearCredentialStateRequest clearRequest = new ClearCredentialStateRequest();
        credentialManager.clearCredentialStateAsync(
                clearRequest,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback<Void, ClearCredentialException>() {
                    @Override
                    public void onResult(Void unused) {
                        Log.e("logOut", "Cleared user credentials");
                        Intent intent = new Intent(activity, SignInActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                    }

                    @Override
                    public void onError(@NonNull ClearCredentialException e) {
                        Log.e("logOut", "Couldn't clear user credentials: " + e.getLocalizedMessage());
                    }
                }
        );
    }

    public static void createUserWithEmailAndPassword(FirebaseAuth mAuth, Activity activity, Resources resources, String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                            activity.finish();
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignInPassword", "createUserWithEmail:success");

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignInPassword", "createUserWithEmail:failure", task.getException());
                            if (task.getException() != null && task.getException().getMessage().contains("The email address is already in use by another account")) {
                                Toast.makeText(activity, resources.getString(R.string.email_used), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, resources.getString(R.string.registration_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

    public static void signInWithEmailAndPassword(FirebaseAuth mAuth, Activity activity, Resources resources, String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("SignInPassword", "signInWithEmail:success");
                            Intent intent = new Intent(activity, MainActivity.class);
                            activity.startActivity(intent);
                            activity.finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("SignInPassword", "signInWithEmail:failure", task.getException());
                            if (task.getException() != null && task.getException().getMessage().contains("The supplied auth credential is incorrect")) {
                                Toast.makeText(activity, resources.getString(R.string.user_not_exist), Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(activity, resources.getString(R.string.sign_in_failed), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
    }

}

 */
