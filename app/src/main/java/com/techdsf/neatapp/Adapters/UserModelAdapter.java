package com.techdsf.neatapp.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.techdsf.neatapp.Models.Users;
import com.techdsf.neatapp.R;

import java.util.ArrayList;

public class UserModelAdapter extends RecyclerView.Adapter<UserModelAdapter.ViewHolder>{

    //Var...........
    private ArrayList<Users> list;
    Context context;

    public UserModelAdapter(ArrayList<Users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate my Sample Layout........................
        View view = LayoutInflater.from(context).inflate(R.layout.sample_user_layout,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //set My Sample Layout item Value..............
        //finally get...........
        Users users =  list.get(position);

        //finally set........................
        Picasso.get().load(users.getUserImage()).placeholder(R.drawable.ic_user).into(holder.profileImage);
        holder.userName.setText(users.getUserName());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profileImage;
        TextView userName,lastMessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.usernameTvId);
            lastMessage = itemView.findViewById(R.id.lastMeassgeTvId);
        }
    }

}
