package com.senpai.firebasepushnotification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UsersRecyclerViewAdapter extends RecyclerView.Adapter<UsersRecyclerViewAdapter.ViewHolder> {

    private List<Users> usersList;
    private Context context;

    public UsersRecyclerViewAdapter(Context context, List<Users> usersList) {
        this.usersList = usersList;
        this.context=context;
    }

    @NonNull
    @Override
    public UsersRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.users_list_item,parent,false);


        return new UsersRecyclerViewAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersRecyclerViewAdapter.ViewHolder holder, int position) {

        holder.user_list_name.setText(usersList.get(position).getName());
        Glide.with(context).load(usersList.get(position).getImage()).into(holder.user_list_image);
        String userId = usersList.get(position).userId;
        //String name = usersList.get(position).getName();

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent sendIntent = new Intent(context,SendActivity.class);
                sendIntent.putExtra("userID",userId);
                //sendIntent.putExtra("name",name);
                context.startActivity(sendIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private CircleImageView user_list_image;
        private TextView user_list_name;

        public ViewHolder(View itemView){
            super(itemView);
            mView = itemView;

            user_list_image = (CircleImageView) mView.findViewById(R.id.users_list_image);
            user_list_name = (TextView)mView.findViewById(R.id.user_list_name);




        }
    }
}
