package com.example.offers2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;

public class ManagerInterfaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_interface);
    }
    @Override
    protected void onResume() {

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        super.onResume();

        ImageView home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManagerInterfaceActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });
        ImageView login_icon = findViewById(R.id.login_icon);
        login_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManagerInterfaceActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });

        FloatingActionButton buttonAdd = findViewById(R.id.buttonAdd);
        TextView welcome = findViewById(R.id.welcome);
        String managerId = getIntent().getStringExtra("managerId");
        Log.d("last test", managerId);
        FirebaseFirestore db1 = FirebaseFirestore.getInstance();
        db1.collection("user").whereEqualTo(FieldPath.documentId(), managerId).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            QuerySnapshot querySnapshot = task.getResult();
                            if (!querySnapshot.isEmpty()){
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                String fullName = document.getString("fullName");
                                welcome.setText("Manager: "+fullName);
                            }
                        }
                    }
                });

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManagerInterfaceActivity.this,AddOfferActivity.class);
                i.putExtra("managerId", managerId);
               startActivity(i);
            }

        });

        ListView listView = findViewById(R.id.listView);
        ArrayList<String> list = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("offer").whereEqualTo("managerId",managerId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String hotelName = document.getString("hotelName");
                                String hotelLocation = document.getString("hotelLocation");
                                String startDate = document.getString("startDate");
                                String endDate = document.getString("endDate");
                                String description = document.getString("description");

                                list.add("\n" + title +"\n Hotel Name : "+ hotelName+ "\n Hotel Location: "+hotelLocation+  "\nFrom: "  +startDate+" To: "+endDate);

                            }


                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ManagerInterfaceActivity.this, android.R.layout.simple_list_item_1, list);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String offerTitle = ((TextView) view).getText().toString().split("\n")[1];
                                    Log.d("firstTag", offerTitle);


                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("offer").whereEqualTo("title",offerTitle).get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            String hotelName = document.getString("hotelName");
                                                            String offerId = document.getId();
                                                            String hotelLocation = document.getString("hotelLocation");
                                                            String description = document.getString("description");


                                                            Dialog dialog = new Dialog(ManagerInterfaceActivity.this);
                                                            dialog.setContentView(R.layout.dialog);


                                                            TextView descriptionView = dialog.findViewById(R.id.description);
                                                            Button yes = dialog.findViewById(R.id.yes);
                                                            Button moreOptionBtn = dialog.findViewById(R.id.no);
                                                            moreOptionBtn.setText("More Options");



                                                            descriptionView.setText("Description: \n"+description);

                                                            yes.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    Intent i = new Intent(ManagerInterfaceActivity.this,ReservationListActivity.class);
                                                                    i.putExtra("managerId",managerId);
                                                                    i.putExtra("offerId",offerId);
                                                                    startActivity(i);

                                                                }
                                                            });

                                                            moreOptionBtn.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    Intent i = new Intent(ManagerInterfaceActivity.this, ManagerDetailedOfferActivity.class);
                                                                    i.putExtra("managerId",managerId);
                                                                    i.putExtra("offerId",offerId);

                                                                    Log.d("managerId and offerId send",managerId +"+"+offerId);
                                                                    startActivity(i);

                                                                }
                                                            });

                                                            dialog.show();
                                                        }



                                                    }

                                                }
                                            });


                                }
                            });



                        }
                    }
                });
    }
}
