package com.codespacepro.whatsify.Fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.codespacepro.whatsify.Adapters.ChatAdapter;
import com.codespacepro.whatsify.Models.Chat;
import com.codespacepro.whatsify.Models.Users;
import com.codespacepro.whatsify.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ChatFragment extends Fragment {


    public ChatFragment() {
        // Required empty public constructor
    }


    RecyclerView recyclerView;
    List<Chat> usersArrayList = new ArrayList<>();
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_chat, container, false);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference();
        recyclerView = view.findViewById(R.id.recyclerviewchat);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);

//        database.getReference("Users").addValueEventListener(new ValueEventListener() {
//            @SuppressLint("NotifyDataSetChanged")
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                usersArrayList.clear();
//                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                    if (dataSnapshot.getValue() instanceof Chat) {
//                        Chat chat = dataSnapshot.getValue(Chat.class);
//                        usersArrayList.add(chat);
//                    }
//                }
//                chatAdapter.notifyDataSetChanged();
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dataSnapshot : snapshot.child("Users").getChildren()) {
                    usersArrayList.clear();
                    if (dataSnapshot.hasChild("email") && dataSnapshot.hasChild("fullname")) {
                        String email = dataSnapshot.child("email").getValue(String.class);
                        String fullname = dataSnapshot.child("fullname").getValue(String.class);


                        Chat chat = new Chat(email, fullname);
                        usersArrayList.add(chat);
                    }

                }
                recyclerView.setAdapter(new ChatAdapter(getContext(), usersArrayList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}