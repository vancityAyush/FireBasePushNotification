package com.senpai.firebasepushnotification;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfileFragment extends Fragment {

    private Button mLogoutButton;
    private FirebaseAuth mAuth;
    private CircleImageView profileImage;
    private TextView txtProfile;
    private FirebaseFirestore mFirestore;
    String userId;

    public ProfileFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        mFirestore = FirebaseFirestore.getInstance();

        userId=mAuth.getCurrentUser().getUid();
        profileImage = view.findViewById(R.id.profileImage);
        txtProfile = view.findViewById(R.id.txtProfile);
        mLogoutButton = view.findViewById(R.id.btnLogout);

        mFirestore.collection("User").document(userId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {

                String username = documentSnapshot.getString("name");
                String userImage = documentSnapshot.getString("image");
                txtProfile.setText(username);
                Glide.with(view.getContext()).load(userImage).into(profileImage);


            }
        });



        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(container.getContext(),LoginActivity.class);
                startActivity(intent);
            }
        });


        return view;
    }
}