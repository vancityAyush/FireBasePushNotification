package com.senpai.firebasepushnotification;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class RegisterActivity extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private CircleImageView mImageView;
    private EditText edtUsername, edtPassword, edtEmail;
    private Button btnLogin, btnSignup;
    private Uri imageUri;
    private StorageReference mStorage;
    private FirebaseAuth mAuth;
    private FirebaseFirestore mFireStore = FirebaseFirestore.getInstance();
    private ProgressBar mRegisterProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        edtUsername = findViewById(R.id.register_edtUsername);
        edtEmail = findViewById(R.id.register_edtEmail);
        edtPassword = findViewById(R.id.register_edtPassword);
        mImageView = findViewById(R.id.mImageView);
        mRegisterProgressBar = findViewById(R.id.registerProgressBar);
        imageUri=null;


        mStorage = FirebaseStorage.getInstance().getReference().child("images");
        mAuth = FirebaseAuth.getInstance();
        mFireStore = FirebaseFirestore.getInstance();

        btnLogin = findViewById(R.id.register_btnLogin);
        btnSignup = findViewById(R.id.register_btn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select a Picture"),PICK_IMAGE);

            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageUri == null){
                    mRegisterProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(RegisterActivity.this,"Please select an image first",Toast.LENGTH_SHORT).show();

                }else{
                    mRegisterProgressBar.setVisibility(View.VISIBLE);
                    String name = edtUsername.getText().toString();
                    String email =  edtEmail.getText().toString();
                    String password = edtPassword.getText().toString();

                    if(name.isEmpty() || email.isEmpty() || password.isEmpty()){
                        mRegisterProgressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(RegisterActivity.this, "Please Enter all the fields properly!",Toast.LENGTH_SHORT).show();
                    }else {

                        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){

                                    String userId = mAuth.getCurrentUser().getUid();
                                    StorageReference  userProfile = mStorage.child(userId+".jpg");
                                    userProfile.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                        @Override
                                        public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> uploadTask) {
                                            if(uploadTask.isSuccessful()){

                                                //TODO downloadURL no working
                                                String downloadUrl=null;

                                                Map<String,Object> userMap = new HashMap<>();
                                                userMap.put("name",name);
                                                userMap.put("image",downloadUrl);


                                                mFireStore.collection("Users").document(userId).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void aVoid) {
                                                        sendToMain();
                                                    }
                                                });

                                            }else {
                                                mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                                Toast.makeText(RegisterActivity.this,"Error : "+uploadTask.getException(),Toast.LENGTH_SHORT).show();

                                            }

                                            }
                                    });


                                }else {
                                    mRegisterProgressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(RegisterActivity.this,"Error : "+task.getException(),Toast.LENGTH_SHORT).show();

                                }
                            }
                        });

                    }

                }
            }
        });

    }

    private void sendToMain() {
        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(mainIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PICK_IMAGE){
            imageUri=data.getData();
            mImageView.setImageURI(imageUri);
        }
    }
}