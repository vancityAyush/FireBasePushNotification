package com.senpai.firebasepushnotification;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersFragment extends Fragment {

        private RecyclerView mUsersRecyclerView;
        private List<Users> usersList;
        private UsersRecyclerViewAdapter usersRecyclerViewAdapter;
        private FirebaseFirestore mFirestore;

    public UsersFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_users, container, false);


        mFirestore = FirebaseFirestore.getInstance();
        mUsersRecyclerView = view.findViewById(R.id.users_list);
        usersList = new ArrayList<>();
        usersRecyclerViewAdapter = new UsersRecyclerViewAdapter(container.getContext(),usersList);

        mUsersRecyclerView.setHasFixedSize(true);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));

        mUsersRecyclerView.setAdapter(usersRecyclerViewAdapter);


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        usersList.clear();

        mFirestore.collection("Users").addSnapshotListener(getActivity(),new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                for(DocumentChange doc : value.getDocumentChanges()){
                    if(doc.getType() == DocumentChange.Type.ADDED){

                        String userID =  doc.getDocument().getId();
                        Users users = doc.getDocument().toObject(Users.class).withID(userID);
                        usersList.add(users);

                        usersRecyclerViewAdapter.notifyDataSetChanged();
                    }
                }
            }
        });



    }
}