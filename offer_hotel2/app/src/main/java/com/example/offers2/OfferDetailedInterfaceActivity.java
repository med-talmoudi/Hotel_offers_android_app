package com.example.offers2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class OfferDetailedInterfaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_detailed_interface);


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

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        ImageView home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OfferDetailedInterfaceActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });

        ImageView login_icon = findViewById(R.id.login_icon);
        login_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(OfferDetailedInterfaceActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });


        String offerId = getIntent().getStringExtra("offerId");


        TextView titleV = findViewById(R.id.titleTextView);
        TextView hotelNameV = findViewById(R.id.hotelNameTextView);
        TextView hotelLocationV = findViewById(R.id.hotelLocationTextView);
        TextView startDateV = findViewById(R.id.startDateTextView);
        TextView endDateV = findViewById(R.id.endDateTextView);
        TextView descriptionV = findViewById(R.id.descriptionTextView);
        TextView priceTextV = findViewById(R.id.priceTextView);
        ImageView imageView = findViewById(R.id.imageView);
        Button btnPurchaseOfferV = findViewById(R.id.btnPurchaseOffer);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("offer").whereEqualTo(FieldPath.documentId(), offerId)
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String title = document.getString("title");
                                String hotelName = document.getString("hotelName");
                                String hotelLocation = document.getString("hotelLocation");
                                String startDate = document.getString("startDate");
                                String endDate = document.getString("endDate");
                                String price = document.getString("price");
                                String imageUrl  = document.getString("imageUrl");

                                String description = document.getString("description");





                                titleV.setText(title);
                                hotelNameV.setText("Hotel: "+hotelName);
                                hotelLocationV.setText("Location: "+hotelLocation);
                                startDateV.setText("Start At: "+startDate);
                                endDateV.setText("End At: "+endDate);
                                priceTextV.setText("Total Price "+price+ "DT");
                                Picasso.get().load(imageUrl).into(imageView);
                                descriptionV.setText("Content: \n" + description);


                                btnPurchaseOfferV.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Dialog dialog =new Dialog(OfferDetailedInterfaceActivity.this);
                                        dialog.setContentView(R.layout.purchase_offer_dialog_form);
                                            if (clientId.equals("visitor")){
                                                Intent i = new Intent(OfferDetailedInterfaceActivity.this,LoginActivity.class);
                                                startActivity(i);
                                                Toast.makeText(OfferDetailedInterfaceActivity.this, "You must login first to purchase an offer!", Toast.LENGTH_SHORT).show();
                                            }else {
                                                EditText name_edittext = dialog.findViewById(R.id.name_edittext);
                                                EditText cin_edittext = dialog.findViewById(R.id.cin_edittext);
                                                EditText phoneNumber_edittext = dialog.findViewById(R.id.phoneNumber_edittext);
                                                CheckBox ageCheckbox = dialog.findViewById(R.id.age_checkbox);

                                                Button submit = dialog.findViewById(R.id.submit);
                                                Button no = dialog.findViewById(R.id.no);
                                                FirebaseFirestore db2 = FirebaseFirestore.getInstance();
                                                db2.collection("user").whereEqualTo(FieldPath.documentId(),clientId).get()
                                                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                if (task.isSuccessful()){
                                                                    QuerySnapshot querySnapshot = task.getResult();
                                                                    if (!querySnapshot.isEmpty()){
                                                                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                                                        String fullName = document.getString("fullName");
                                                                        name_edittext.setText(fullName);


                                                                    }
                                                                }
                                                            }
                                                        });

                                                dialog.show();
                                                no.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        dialog.dismiss();

                                                    }
                                                });

                                                submit.setOnClickListener(new View.OnClickListener() {
                                                    @Override
                                                    public void onClick(View v) {
                                                        String name = name_edittext.getText().toString().trim();
                                                        String cin = cin_edittext.getText().toString().trim();
                                                        String phoneNumber = phoneNumber_edittext.getText().toString().trim();
                                                        boolean isOver18 = ageCheckbox.isChecked();

                                                        if (name.isEmpty()) {
                                                            name_edittext.setError("Title is required");
                                                            return;
                                                        }
                                                        if (cin.isEmpty()) {
                                                            cin_edittext.setError("Title is required");
                                                            return;
                                                        }
                                                        if (phoneNumber.isEmpty()) {
                                                            phoneNumber_edittext.setError("phone Number is required");
                                                            return;
                                                        }
                                                        if (!isOver18) {
                                                            Toast.makeText(OfferDetailedInterfaceActivity.this, "You must be over 18", Toast.LENGTH_SHORT).show();
                                                            return;
                                                        }

                                                        FirebaseFirestore db= FirebaseFirestore.getInstance();

                                                        Map<String, Object> reservation = new HashMap<>();
                                                        reservation.put("offerId", offerId);
                                                        reservation.put("clientId", clientId);
                                                        reservation.put("name", name);
                                                        reservation.put("phoneNumber", phoneNumber);
                                                        reservation.put("cin", cin);

                                                        FirebaseFirestore db3 =FirebaseFirestore.getInstance();
                                                        db3.collection("reservation").whereEqualTo("cin",cin).get()
                                                                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                                                        if (task.isSuccessful()){
                                                                            QuerySnapshot querySnapshot = task.getResult();
                                                                            if (querySnapshot.isEmpty()){
                                                                                db.collection("reservation").add(reservation)
                                                                                        .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                                            @Override
                                                                                            public void onSuccess(DocumentReference documentReference) {
                                                                                                Log.d("sucTAG", "offer is reserved " + documentReference.getId());
                                                                                                Toast.makeText(OfferDetailedInterfaceActivity.this, "Offer has been reserved successfully!", Toast.LENGTH_SHORT).show();
                                                                                                Intent i = new Intent(OfferDetailedInterfaceActivity.this, ClientInterfaceActivity.class);
                                                                                                startActivity(i);
                                                                                            }
                                                                                        }).addOnFailureListener(new OnFailureListener() {
                                                                                            @Override
                                                                                            public void onFailure(@NonNull Exception e) {
                                                                                                Log.d("errTAG", "offer not reserved! " + e);
                                                                                                Toast.makeText(OfferDetailedInterfaceActivity.this, "Offer has not been reserved successfully!", Toast.LENGTH_SHORT).show();
                                                                                            }
                                                                                        });


                                                                            }else {
                                                                                Toast.makeText(OfferDetailedInterfaceActivity.this, "You are already reserved this offer! ", Toast.LENGTH_SHORT).show();
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
                    }
                });

    }
}