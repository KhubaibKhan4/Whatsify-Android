package com.codespacepro.whatsify.Adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codespacepro.whatsify.Models.Chat;
import com.codespacepro.whatsify.Models.Users;
import com.codespacepro.whatsify.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Chat> mDataList;

    public ChatAdapter(Context mContext, ArrayList<Chat> mDataList) {
        this.mContext = mContext;
        this.mDataList = mDataList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chat_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Chat chat = mDataList.get(position);
        holder.Fullname.setText(chat.getFullname());
        Glide.with(mContext).load(chat.getProfile()).placeholder(R.drawable.avatar_profile).into(holder.Profile);

    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView Fullname;
        CircleImageView Profile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            Fullname = itemView.findViewById(R.id.nav_fullname);
            Profile = itemView.findViewById(R.id.nav_profile);
        }
    }
}

