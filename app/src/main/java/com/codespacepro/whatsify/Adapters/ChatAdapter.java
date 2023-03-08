package com.codespacepro.whatsify.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.codespacepro.whatsify.Models.Chat;
import com.codespacepro.whatsify.R;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {

    Context context;
    private List<Chat> userList;

    public ChatAdapter(Context context, List<Chat> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.chat_sample, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Chat users = userList.get(position);
        holder.bind(users);


    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView profile;
        TextView fullname;
        TextView lastmessage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profile = itemView.findViewById(R.id.nav_chat_profile);
            fullname = itemView.findViewById(R.id.nav_fullname);
            lastmessage = itemView.findViewById(R.id.nav_last_message);
        }

        public void bind(Chat chat) {
            fullname.setText(chat.getFullname());
            Glide.with(itemView.getContext()).load(chat.getProfile()).into(profile);
        }
    }
}
