package com.example.offers2;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import androidx.annotation.NonNull;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Objects;

public class ClientInterfaceActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_interface);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ImageView home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClientInterfaceActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });
        ImageView login_icon = findViewById(R.id.login_icon);
        login_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClientInterfaceActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });


      //  Objects.requireNonNull(getSupportActionBar()).setTitle("fullName");


        TextView welcome = findViewById(R.id.welcome);
        String clientId = getIntent().getStringExtra("clientId");

        if(clientId.equals("visitor")){
            welcome.setText("Visitor");
        }else {
            Log.d("tag12Id",clientId);
            FirebaseFirestore db1 = FirebaseFirestore.getInstance();
            db1.collection("user").whereEqualTo(FieldPath.documentId(), clientId).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                QuerySnapshot querySnapshot = task.getResult();
                                if (!querySnapshot.isEmpty()){
                                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                    String fullName = document.getString("fullName");
                                    welcome.setText("Client: "+fullName);
                                }
                            }
                        }
                    });
        }


        ListView listView = findViewById(R.id.clientlistView);
        ArrayList<String> list = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("offer")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            Log.d("firstTag", "where are here");
                            for(QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String hotelName = document.getString("hotelName");
                                String hotelLocation = document.getString("hotelLocation");
                                String startDate = document.getString("startDate");
                                String endDate = document.getString("endDate");



                                list.add("\n" + title +"\n Hotel Name : "+ hotelName+ "\n Hotel Location"+hotelLocation+  "\n Date From"  +startDate+" To "+endDate);

                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ClientInterfaceActivity.this, android.R.layout.simple_list_item_1, list);
                            listView.setAdapter(adapter);

                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    String offerTitle = ((TextView) view).getText().toString().split("\n")[1];


                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    db.collection("offer").whereEqualTo("title",offerTitle).get()
                                            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                @Override
                                                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                    if (task.isSuccessful()) {
                                                        for (QueryDocumentSnapshot document : task.getResult()) {
                                                            String hotelName = document.getString("hotelName");
                                                            String hotelLocation = document.getString("hotelLocation");
                                                            String description = document.getString("description");

                                                            Dialog dialog = new Dialog(ClientInterfaceActivity.this);
                                                            dialog.setContentView(R.layout.dialog);


                                                            TextView descriptionView = dialog.findViewById(R.id.description);
                                                            Button yes = dialog.findViewById(R.id.yes);
                                                            Button no = dialog.findViewById(R.id.no);


                                                            descriptionView.setText("Description: "+description);

                                                            yes.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    Intent i = new Intent(ClientInterfaceActivity.this, OfferDetailedInterfaceActivity.class);
                                                                    i.putExtra("offerId", document.getId());
                                                                    Log.d("offerId",document.getId());
                                                                    i.putExtra("clientId",clientId);

                                                                    Log.d("clientId",clientId   );
                                                                    startActivity(i);
                                                                }
                                                            });
                                                            no.setOnClickListener(new View.OnClickListener() {
                                                                @Override
                                                                public void onClick(View v) {
                                                                    dialog.dismiss();

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