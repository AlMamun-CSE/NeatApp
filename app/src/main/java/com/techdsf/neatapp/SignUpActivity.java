package com.techdsf.neatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.techdsf.neatapp.Models.Users;
import com.techdsf.neatapp.databinding.ActivitySignUpBinding;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "SignUpActivity";

    //Final Var..............


    //Weight Var..................
    private ProgressDialog mProgressDialog;


    //Firebase Var..................
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;

    //Others......................
    private ActivitySignUpBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // inflate signUp Activity layout...............................
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
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
        mProgressDialog.setTitle("Creating Account");
        mProgressDialog.setMessage("We're creating your account. ");

        //Create User Account.................
        CreateSignUpUser();

        //Click Already Have An Account And Go to SignIn Activity
        ClickAlreadyHaveAnAccount();



    }

    private void ClickAlreadyHaveAnAccount() {
        binding.alreadyAccountTvId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent logInIntent = new Intent(SignUpActivity.this,SignInActivity.class);
                startActivity(logInIntent);
                finish();
            }
        });
    }

    private void CreateSignUpUser() {
        binding.signUpBtnId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //show progressDialog....................
                mProgressDialog.show();

                mAuth.createUserWithEmailAndPassword(binding.signUpEmailEtId.getText().toString().trim(),binding.signUPUserPasswordEtId.getText().toString().trim())
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {

                                //progressDialog dismiss.................
                                mProgressDialog.dismiss();

                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information.................
                                    Log.d(TAG, "signInWithEmail:success");
                                    Toast.makeText(SignUpActivity.this, "User Created successfully", Toast.LENGTH_SHORT).show();
                                    //Get a Current user with Firebase Auth...........................
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //Get Users Property....call Users Model...............................
                                    Users users = new Users(binding.signUpUserNameEtId.getText().toString().trim(),
                                            binding.signUpEmailEtId.getText().toString().trim(),
                                            binding.signUPUserPasswordEtId.getText().toString().trim());

                                    //Store user data with firebase database............................
//                                    String id = user.getUid();
                                    String id = task.getResult().getUser().getUid();
                                    mDatabase.getReference().child("Users").child(id).setValue(users);

                                    //Go To Log In Test........................................................
                                    Intent logInIntent = new Intent(SignUpActivity.this,SignInActivity.class);
                                    startActivity(logInIntent);
                                    finish();

                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.........................
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                    // ...
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
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser currentUser) {

    }
}