package com.senpai.firebasepushnotification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SendActivity extends AppCompatActivity {
    private TextView user_id_view;
    private EditText mMessageView;
   private Button btnSend;
   private ProgressBar progressBar;

   private FirebaseFirestore firestore;

   private String user_id, current_id ,sendName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        user_id_view = findViewById(R.id.user_name_view);
        user_id = getIntent().getStringExtra("userID");

        current_id = FirebaseAuth.getInstance().getUid();

        mMessageView = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);
        progressBar = findViewById(R.id.sendProgress);

        firestore = FirebaseFirestore.getInstance();
        //sendName = getIntent().getStringExtra("name");
        //user_id_view.setText("Send to "+sendName);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = mMessageView.getText().toString();
                if(!message.isEmpty()){
                    progressBar.setVisibility(View.VISIBLE);
                    Map<String,Object> notificationMessage = new HashMap<>();
                    notificationMessage.put("message",message);
                    notificationMessage.put("from",current_id);

                    firestore.collection("Users/"+user_id+"/Notifications").add(notificationMessage).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Toast.makeText(SendActivity.this,"Sent!",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(SendActivity.this,"Error : "+e.getMessage(),Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });


                }


            }
        });


    }
}