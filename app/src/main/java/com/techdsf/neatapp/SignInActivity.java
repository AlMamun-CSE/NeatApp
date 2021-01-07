package com.techdsf.neatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.techdsf.neatapp.databinding.ActivitySignInBinding;

public class SignInActivity extends AppCompatActivity {
    private static final String TAG = "SignInActivity";
    //Final Var......................

    //Weight Var....................
    private ProgressDialog mProgressDialog;

    //Firebase Var..................
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;

    //Others Var.....................
    private ActivitySignInBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate signIn Activity layout.................................
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Log.d(TAG, "onCreate: started.");

        //remove action bar..............
        getSupportActionBar().hide();

        //Initialize firebase instance...............
        mAuth = FirebaseAuth.getInstance();
        //Access User Information...................
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();

        //Weight Var Implementation..............................
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setTitle("Log In User");
        mProgressDialog.setMessage("We're log in your account. ");

        //Create User Account.................
        LogInSignUpUser();

        //Click Sign Up And Go To Sign Up Activity........................
        ClickSignUp();




    }

    private void ClickSignUp() {
        binding.clickSignUpTvId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Go To Main Activity
                Intent mainIntent = new Intent(SignInActivity.this,SignUpActivity.class);
                startActivity(mainIntent);
                finish();
            }
        });
    }


    private void LogInSignUpUser() {
        binding.signInBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //show progressDialog....................
                mProgressDialog.show();

                mAuth.signInWithEmailAndPassword(binding.signInUserEmailEtId.getText().toString().trim(),binding.signInUserPassEtId.getText().toString().trim())
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

                                } else {
                                    // If sign in fails, display a message to the user......................
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignInActivity.this, "LogIn failed.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            }
                        });
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.............
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null){
            //Go To Main Activity
            Intent mainIntent = new Intent(SignInActivity.this,MainActivity.class);
            startActivity(mainIntent);
            finish();
        }
    }
}