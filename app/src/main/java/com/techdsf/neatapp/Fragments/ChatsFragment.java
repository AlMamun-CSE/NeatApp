package com.techdsf.neatapp.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.techdsf.neatapp.Adapters.UserModelAdapter;
import com.techdsf.neatapp.Models.Users;
import com.techdsf.neatapp.R;
import com.techdsf.neatapp.databinding.ActivityMainBinding;
import com.techdsf.neatapp.databinding.FragmentChatsBinding;

import java.util.ArrayList;


public class ChatsFragment extends Fragment {
    private static final String TAG = "ChatsFragment";

    //Final Var.................................................

    //Weight Var................................................

    //Firebase Var..............................................
    private FirebaseDatabase mDatabase;

    //Others Var...................................................
    private FragmentChatsBinding mBinding;
    private ArrayList<Users>list = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started.");
        // Inflate the layout for this fragment
        mBinding = FragmentChatsBinding.inflate(inflater,container, false);

        //Create Adapter Object And Set adapter.................................
        UserModelAdapter adapter = new UserModelAdapter(list,getContext());
        mBinding.userRecyclerViewId.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance();

        //Create layoutManager And Set Layout....................................................
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mBinding.userRecyclerViewId.setLayoutManager(linearLayoutManager);

        //get firebase database user data and set and change .................................
        mDatabase.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //.............OK..................
                list.clear();

                for (DataSnapshot dataSnapshot: snapshot.getChildren()){
                    Users users = dataSnapshot.getValue(Users.class);
                    users.getUserId(dataSnapshot.getKey());
                    list.add(users);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        return mBinding.getRoot();
    }
}