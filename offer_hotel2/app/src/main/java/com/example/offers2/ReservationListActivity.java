package com.example.offers2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ReservationListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation_list);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ImageView home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReservationListActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });
        ImageView login_icon = findViewById(R.id.login_icon);
        login_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ReservationListActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        ListView reservationListView = findViewById(R.id.reservationListView);

        String managerId = getIntent().getStringExtra("managerId");
        String offerId = getIntent().getStringExtra("offerId");


        ArrayList<String> list = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reservation").whereEqualTo("offerId", offerId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                String fullName = document.getString("name");
                                String cin = document.getString("cin");
                                String phoneNumber = document.getString("phoneNumber");
                                list.add("\n Client Name: " + fullName + "\n Phone Number: " + phoneNumber + "\n CIN: " +cin);
                                ArrayAdapter<String> adapter = new ArrayAdapter<>(ReservationListActivity.this, android.R.layout.simple_list_item_1, list);
                                reservationListView.setAdapter(adapter);
                            }
                        }
                    }
                });
    }
}