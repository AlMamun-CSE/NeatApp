package com.techdsf.neatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;
import com.techdsf.neatapp.Models.Users;
import com.techdsf.neatapp.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{
    private static final String TAG = "SignInActivity";

    //Final Var..............................................
    private static final int RC_SIGN_IN = 9001;

    //Weight Var....................
    private ProgressDialog mProgressDialog;
    public ProgressBar mProgressBar;

    //Firebase Var............................................
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;

    //Firebase Google Sign In....................................
    private GoogleSignInClient mGoogleSignInClient;

    //Others Var.....................
    private ActivitySignInBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate signIn Activity layout.................................
        mBinding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Log.d(TAG, "onCreate: started.");

        //remove action bar..............
        getSupportActionBar().hide();


        //Initialize firebase instance...............
        mAuth = FirebaseAuth.getInstance();
        //Access User Information...................
        mDatabase = FirebaseDatabase.getInstance();


        //Weight Var Implementation..............................
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Log In User");
        mProgressDialog.setMessage("We're log in your account. ");


        // Configure Google Sign In...........GoogleSignInOption object work requestIdToken..............
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();


        // Click listeners
        mBinding.signInBtnId.setOnClickListener(this);
        mBinding.signInGoogleBtnId.setOnClickListener(this);
        mBinding.signInFacebookBtnId.setOnClickListener(this);
        mBinding.phoneSignInTvId.setOnClickListener(this);
        mBinding.clickSignUpTvId.setOnClickListener(this);


        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);



    }

    public void showProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (mProgressBar != null) {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        hideProgressBar();
    }


    // [START on_start_check_user]........................
    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart: started.");
        // Check if user is signed in (non-null) and update UI accordingly..............

        if (mAuth.getCurrentUser() != null){
            Intent mainIntent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
    // [END on_start_check_user]............................



    // [START onactivityresult].............................
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);..............
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase.............
                GoogleSignInAccount account = task.getResult(ApiException.class);
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately...................
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE].............
            }
        }
    }
    // [END onactivityresult]............................

    // [START auth_with_google]...............................
    private void firebaseAuthWithGoogle(String idToken) {
        // [START_EXCLUDE silent].................
        showProgressBar();
        // [END_EXCLUDE]...............
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information..............
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //set user data for google auth.............................
                            Users users = new Users();
                            users.setUserId(user.getUid());
                            users.setUserName(user.getDisplayName());
                            users.setUserImage(user.getPhotoUrl().toString());
                            //store data database..................................
                            mDatabase.getReference().child("Users").child(user.getUid()).setValue(users);

                            //Go To Main Activity................................
                            Intent mainIntent = new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                            finish();


                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user..................
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(mBinding.getRoot(), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // [START_EXCLUDE]....................
                        hideProgressBar();
                        // [END_EXCLUDE]............
                    }
                });
    }
    // [END auth_with_google]..............


    private void updateUI(FirebaseUser user) {
        hideProgressBar();
        if (user != null) {

        } else {

        }
    }



    private void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    private void logInSignUpUser() {

        //show progressDialog....................
        mProgressDialog.show();

        mAuth.signInWithEmailAndPassword(mBinding.signInUserEmailEtId.getText().toString().trim(),mBinding.signInUserPassEtId.getText().toString().trim())
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //progressDialog dismiss.................
                        mProgressDialog.dismiss();

                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "Log in success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            //Go To Main Activity................................
                            Intent mainIntent = new Intent(SignInActivity.this,MainActivity.class);
                            startActivity(mainIntent);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user......................
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(SignInActivity.this, "Please Before Sign Up Your Account.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void goToSignUpActivity() {
        Intent signInActivity = new Intent(SignInActivity.this,SignUpActivity.class);
        startActivity(signInActivity);
        finish();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.signInBtnId) {
            logInSignUpUser();
        } else if (i == R.id.signInGoogleBtnId) {
            signIn();
        } else if (i == R.id.signInFacebookBtnId) {

        } else if (i == R.id.phoneSignInTvId) {

        }else if (i == R.id.clickSignUpTvId) {
            goToSignUpActivity();
        }
    }

}