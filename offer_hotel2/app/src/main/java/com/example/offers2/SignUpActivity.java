package com.example.offers2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ImageView home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });

        ImageView login_icon = findViewById(R.id.login_icon);
        login_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SignUpActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });


        EditText fullNameV = findViewById(R.id.editTextFullName);
        EditText EmailV = findViewById(R.id.editTextEmail);
        EditText password1V = findViewById(R.id.editTextPassword1);
        EditText password2V = findViewById(R.id.editTextPassword2);

        Button btnSignInSubmitV = findViewById(R.id.btnSignInSubmit);

        btnSignInSubmitV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName= fullNameV.getText().toString().trim();
                String Email = EmailV.getText().toString().trim();
                String password1 = password1V.getText().toString().trim();
                String password2 = password2V.getText().toString().trim();

                if (fullName.isEmpty()) {
                    fullNameV.setError("FullName is required");
                    return;
                }
                if (Email.isEmpty()) {
                    EmailV.setError("FullName is required");
                    return;
                }
                if (password1.isEmpty()) {
                    password1V.setError("FullName is required");
                    return;
                }
                if (password1.length() < 8) {
                    password1V.setError("Password must be at least 8 characters long");
                    return;
                }
                if (password2.isEmpty()) {
                    password2V.setError("FullName is required");
                    return;
                }
                if (password2.length() < 8) {
                    password2V.setError("Password must be at least 8 characters long");
                    return;
                }
                if (!(password1.equals(password2))) {
                    password1V.setText("");
                    password2V.setText("");
                    password1V.setError("please Match Password");

                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("user").whereEqualTo("email",Email)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    if (!task.getResult().isEmpty()) {
                                        EmailV.setError(" This Email is already exist");


                                    }else {

                                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                                        Map<String, Object> client = new HashMap<>();
                                        client.put("fullName", fullName);
                                        client.put("email", Email);
                                        client.put("password", password1);
                                        client.put("privilege", "client");


                                        db.collection("user").add(client)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(SignUpActivity.this, "account created successfully", Toast.LENGTH_SHORT).show();
                                                        Intent i = new Intent(SignUpActivity.this, LoginActivity.class);
                                                        startActivity(i);
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(SignUpActivity.this, "account not created successfully", Toast.LENGTH_SHORT).show();
                                                    }
                                                });




                                    }
                                }

                            }
                        });





            }
        });
    }
}