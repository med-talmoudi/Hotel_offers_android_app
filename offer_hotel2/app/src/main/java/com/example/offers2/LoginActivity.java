package com.example.offers2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }


        Button btnSignIn = findViewById(R.id.btnSignIn);
        ImageView home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });

        Intent i2 = new Intent(LoginActivity.this, SignUpActivity.class);


        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i2);
            }
        });

        Button btnLogin = findViewById(R.id.btnLogin);



        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                EditText editTextEmail = findViewById(R.id.editTextEmail);
                EditText editTextPassword = findViewById(R.id.editTextPassword);

                String email = editTextEmail.getText().toString().trim();
                String password = editTextPassword.getText().toString().trim();

                if (email.isEmpty()) {
                    editTextEmail.setError("Email is required");
                    return;
                }

                if (password.isEmpty()) {
                    editTextPassword.setError("Password is required");
                    return;
                }

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                Log.d("email",email + password);

                db.collection("user").whereEqualTo("email",email)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                                if (task.isSuccessful()){

                                    if (!task.getResult().isEmpty()) {
                                        DocumentSnapshot document = task.getResult().getDocuments().get(0); // Get the first document

                                        if(document.getString("email").equals(email)) {

                                            String x = document.getString("password");
                                            Log.d("123",x);
                                            if(x.equals(password)){

                                                if (document.getString("privilege").equals("client")){
                                                    Intent i = new Intent(LoginActivity.this, ClientInterfaceActivity.class);
                                                    String id = document.getId();
                                                    i.putExtra("clientId",id);
                                                    startActivity(i);
                                                    Toast.makeText(LoginActivity.this, "Welcome "+document.getString("fullName"), Toast.LENGTH_SHORT).show();
                                                } else if (document.getString("privilege").equals("manager")) {
                                                    Intent i = new Intent(LoginActivity.this, ManagerInterfaceActivity.class);
                                                    String id = document.getId();
                                                    i.putExtra("managerId",id);
                                                    startActivity(i);
                                                    Toast.makeText(LoginActivity.this, "Welcome "+document.getString("fullName"), Toast.LENGTH_SHORT).show();


                                                }
                                            } else {
                                                Toast.makeText(LoginActivity.this, "Password Not correct", Toast.LENGTH_SHORT).show();
                                            }

                                        }

                                    }else {
                                        Log.d("tag1254", "i am in else ");
                                        Toast.makeText(LoginActivity.this, "Account Not Found!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });


            }
        });

    }
}

