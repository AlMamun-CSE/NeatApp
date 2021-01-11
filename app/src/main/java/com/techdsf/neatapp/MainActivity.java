package com.techdsf.neatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.techdsf.neatapp.Adapters.FragmentsAdapter;
import com.techdsf.neatapp.databinding.ActivityMainBinding;
import com.techdsf.neatapp.databinding.ActivitySignInBinding;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    //Final Var.................................................

    //Weight Var................................................

    //Firebase Var..............................................
    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;

    //Others Var...................................................
    private ActivityMainBinding mBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        Log.d(TAG, "onCreate: started.");

        mBinding.viewPagerId.setAdapter(new FragmentsAdapter(getSupportFragmentManager()));
        mBinding.tabLayoutId.setupWithViewPager(mBinding.viewPagerId);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d(TAG, "onCreateOptionsMenu: create menu");
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d(TAG, "onOptionsItemSelected: click menu item");

        switch (item.getItemId()){
            case R.id.settingMenuId:
                break;
            case R.id.logoutMenuId:
                mAuth.signOut();
                Intent signInIntent = new Intent(MainActivity.this,SignInActivity.class);
                startActivity(signInIntent);
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
        
    }
}