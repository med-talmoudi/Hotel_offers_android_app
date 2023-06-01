package com.example.offers2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ManagerDetailedOfferActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE_PICKER = 2;
    private Uri selectedImageUri;
    private StorageReference storageReference;
    private ImageView photoImageView;


    private EditText titleV;
    private EditText hotelNameV;
    private EditText hotelLocationV;
    private TextView StartTextView;

    private TextView endDateTextView;

    private EditText descriptionV;
    private EditText priceTextV;
    private Button btnStartDate;
    private Button btnEndDate;


    private Button btnUpdateOffer;
    private Button btnDeleteOffer;

    private String managerId;
    private String offerId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_detailed_offer);

        managerId = getIntent().getStringExtra("managerId");
        offerId = getIntent().getStringExtra("offerId");

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        ImageView home = findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManagerDetailedOfferActivity.this,HomeActivity.class);
                startActivity(i);
            }
        });
        ImageView login_icon = findViewById(R.id.login_icon);
        login_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ManagerDetailedOfferActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });



        Log.d("managerId and offerId received ",managerId +"+"+offerId);

        TextView welcome = findViewById(R.id.welcome);
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





        photoImageView = findViewById(R.id.photoImageView);
        titleV = findViewById(R.id.titleEditText);
        hotelNameV = findViewById(R.id.hotelNameEditText);
        hotelLocationV = findViewById(R.id.hotelLocationEditText);
        StartTextView = findViewById(R.id.StartTextView);
        endDateTextView = findViewById(R.id.endDateTextView);
        hotelLocationV = findViewById(R.id.hotelLocationEditText);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        descriptionV = findViewById(R.id.descriptionEditText);
        priceTextV = findViewById(R.id.priceEditText);

        btnUpdateOffer = findViewById(R.id.btnUpdateOffer);
        btnDeleteOffer = findViewById(R.id.btnDeleteOffer);

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ManagerDetailedOfferActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                StartTextView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });


        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ManagerDetailedOfferActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                endDateTextView.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                            }
                        },
                        year, month, day);
                datePickerDialog.show();
            }
        });


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference offerRef = db.collection("offer").document(offerId);
        offerRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String title = document.getString("title");
                        String hotelName = document.getString("hotelName");
                        String hotelLocation = document.getString("hotelLocation");
                        String startDate = document.getString("startDate");
                        String endDate = document.getString("endDate");
                        String price = document.getString("price");
                        String imageUrl = document.getString("imageUrl");
                        String description = document.getString("description");

                        titleV.setText(title);
                        hotelNameV.setText(hotelName);
                        hotelLocationV.setText(hotelLocation);
                        StartTextView.setText(startDate);
                        endDateTextView.setText(endDate);
                        priceTextV.setText(price);
                        Picasso.get().load(imageUrl).into(photoImageView);
                        descriptionV.setText(description);
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });

        btnDeleteOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog dialog = new Dialog(ManagerDetailedOfferActivity.this);
                dialog.setContentView(R.layout.delete_dialog);
                Button yes = dialog.findViewById(R.id.yes);
                Button no = dialog.findViewById(R.id.no);

                yes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteOffer();
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
        });


        btnUpdateOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateOffer();
            }
        });

        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagePicker();
            }
        });
    }

    private void deleteOffer() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference offerRef = db.collection("offer").document(offerId);
        offerRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ManagerDetailedOfferActivity.this, "Offer Deleted!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ManagerDetailedOfferActivity.this,ManagerInterfaceActivity.class);
                        i.putExtra("managerId", managerId);
                        startActivity(i);
                        deleteReservationsForOffer(offerId);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("TAG", "Error deleting document: ", e);
                        Toast.makeText(ManagerDetailedOfferActivity.this, "Offer Not Deleted!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteReservationsForOffer(String offerId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("reservation")
                .whereEqualTo("offerId", offerId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null && !querySnapshot.isEmpty()) {
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    document.getReference().delete();
                                }
                            } else {
                                Log.d("No reservations found ", "No reservations found for offer with ID: " + offerId);
                            }
                        } else {
                            Log.d("reservations", "Error getting reservations: ", task.getException());
                        }
                    }
                });
    }




    private void updateOffer() {
        String title = titleV.getText().toString().trim();
        String hotelName = hotelNameV.getText().toString().trim();
        String hotelLocation = hotelLocationV.getText().toString().trim();
        String startDate = StartTextView.getText().toString().trim();
        String endDate = endDateTextView.getText().toString().trim();
        String description = descriptionV.getText().toString().trim();
        String price = priceTextV.getText().toString();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference offerRef = db.collection("offer").document(offerId);
        offerRef.update("title", title,
                        "hotelName", hotelName,
                        "hotelLocation", hotelLocation,
                        "startDate", startDate,
                        "endDate", endDate,
                        "description", description,
                        "price", price)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(ManagerDetailedOfferActivity.this, "Offer Updated!", Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(ManagerDetailedOfferActivity.this,ManagerInterfaceActivity.class);
                        i.putExtra("managerId", managerId);
                        startActivity(i);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("TAG", "Error updating offer", e);
                        Toast.makeText(ManagerDetailedOfferActivity.this, "Offer Not Updated!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            uploadImageToStorage();
        }
    }

    private void uploadImageToStorage() {
        if (selectedImageUri != null) {
            storageReference = FirebaseStorage.getInstance().getReference().child("offer_images/" + offerId);
            storageReference.putFile(selectedImageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String imageUrl = uri.toString();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    DocumentReference offerRef = db.collection("offer").document(offerId);
                                    offerRef.update("imageUrl", imageUrl)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void aVoid) {
                                                    Picasso.get().load(imageUrl).into(photoImageView);
                                                    Toast.makeText(ManagerDetailedOfferActivity.this, "Image uploaded successfully!", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Log.w("TAG", "Error updating image URL", e);
                                                    Toast.makeText(ManagerDetailedOfferActivity.this, "Failed to upload image!", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w("TAG", "Error uploading image", e);
                            Toast.makeText(ManagerDetailedOfferActivity.this, "Failed to upload image!", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }
}
