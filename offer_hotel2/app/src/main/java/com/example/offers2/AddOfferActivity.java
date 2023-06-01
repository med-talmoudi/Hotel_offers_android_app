package com.example.offers2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddOfferActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_IMAGE_PICKER = 2;
    private Uri selectedImageUri;
    private StorageReference storageReference;
    private ImageView photoImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offer);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }

        String managerId = getIntent().getStringExtra("managerId");

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
        Button selectImageButton = findViewById(R.id.uploadImageButton);
        EditText editTextTitle = findViewById(R.id.editTextTitle);
        EditText editTextHotelName = findViewById(R.id.editTextHotelName);
        EditText editTextHotelLocation = findViewById(R.id.editTextHotelLocation);
        TextView StartTextView = findViewById(R.id.StartTextView);
        TextView endDateTextView = findViewById(R.id.endDateTextView);
        EditText editTextPrice = findViewById(R.id.editTextPrice);
        EditText editTextDescription = findViewById(R.id.editTextDescription);
        Button btnStartDate = findViewById(R.id.btnStartDate);
        Button btnEndDate = findViewById(R.id.btnEndDate);
        Button buttonSave = findViewById(R.id.buttonSave);


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("user").whereEqualTo(FieldPath.documentId(), managerId).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if(task.isSuccessful()){
                                    if (!task.getResult().isEmpty()) {
                                        DocumentSnapshot document = task.getResult().getDocuments().get(0);

                                        String hotelName = document.getString("hotelName");
                                        String hotelLocation = document.getString("hotelLocation");
                                        editTextHotelName.setText(hotelName);
                                        editTextHotelLocation.setText(hotelLocation);
                                    }

                                }
                            }
                        });

        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, REQUEST_CODE_IMAGE_PICKER);
            }
        });

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        AddOfferActivity.this,
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
                        AddOfferActivity.this,
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

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = editTextTitle.getText().toString().trim();
                String hotelName = editTextHotelName.getText().toString().trim();
                String hotelLocation = editTextHotelLocation.getText().toString().trim();
                String startDate = StartTextView.getText().toString().trim();
                String endDate = endDateTextView.getText().toString().trim();
                String description = editTextDescription.getText().toString().trim();
                String price = editTextPrice.getText().toString();
                if (title.isEmpty()) {
                    editTextTitle.setError("title is required");
                    return;
                }
                if (hotelName.isEmpty()) {
                    editTextHotelName.setError("hotelName is required");
                    return;
                }
                if (hotelLocation.isEmpty()) {
                    editTextHotelLocation.setError("hotelLocation is required");
                    return;
                }
                if (startDate.equals("Start date")) {
                    StartTextView.setError("startDate is required");
                    return;
                }
                if (endDate.equals("End date  ")) {
                    endDateTextView.setError("endDate is required");
                    return;
                }
                if (description.isEmpty()) {
                    editTextDescription.setError("description is required");
                    return;
                }
                if (price.isEmpty()) {
                    editTextPrice.setError("price is required");
                    return;
                }


                Log.d("tagll", "Title: " + title + ", Hotel Name: " + hotelName + ", Hotel Location: " + hotelLocation +
                        ", Start Date: " + startDate + ", End Date: " + endDate + ", Description: " + description);

                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection("offer").whereEqualTo("managerId",managerId).whereEqualTo("title",title).get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()){
                                    if (!task.getResult().isEmpty()) {
                                        editTextTitle.setError("This offer already exists");
                                    }else {

                                        FirebaseFirestore db = FirebaseFirestore.getInstance();

                                        Map<String, Object> offer = new HashMap<>();
                                        offer.put("title", title);
                                        offer.put("hotelName", hotelName);
                                        offer.put("hotelLocation", hotelLocation);
                                        offer.put("startDate", startDate);
                                        offer.put("endDate", endDate);
                                        offer.put("price", price);
                                        offer.put("description", description);
                                        offer.put("managerId", managerId);

                                        if (selectedImageUri != null) {
                                            // Upload the image to Firestore Storage and get the download URL
                                            uploadImageToFirestoreStorage(selectedImageUri, new OnImageUploadListener() {
                                                @Override
                                                public void onImageUploadSuccess(String imageUrl) {
                                                    // Add the image URL to the offer map
                                                    if (imageUrl.isEmpty()) {
                                                        Toast.makeText(getApplicationContext(), "Image is required", Toast.LENGTH_SHORT).show();
                                                        return;
                                                    }
                                                    offer.put("imageUrl", imageUrl);

                                                    // Add the offer to Firestore
                                                    db.collection("offer")
                                                            .add(offer)
                                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                                @Override
                                                                public void onSuccess(DocumentReference documentReference) {
                                                                    Log.d("sucTAG", "offer added with ID: " + documentReference.getId());
                                                                    Toast.makeText(AddOfferActivity.this, "Offer has been added successfully!", Toast.LENGTH_SHORT).show();
                                                                    Intent i = new Intent(AddOfferActivity.this, ManagerInterfaceActivity.class);
                                                                    startActivity(i);
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Log.w("errTAG", "Error adding offer", e);
                                                                    Toast.makeText(AddOfferActivity.this, "Offer has not been added successfully!", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onImageUploadFailure(Exception e) {
                                                    Log.e("errTAG", "Error uploading image", e);
                                                    Toast.makeText(AddOfferActivity.this, "Error uploading image", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        } else {
                                            // No image selected, add the offer without an image
                                            db.collection("offer")
                                                    .add(offer)
                                                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                        @Override
                                                        public void onSuccess(DocumentReference documentReference) {
                                                            Log.d("sucTAG", "offer added with ID: " + documentReference.getId());
                                                            Toast.makeText(AddOfferActivity.this, "Offer has been added successfully!", Toast.LENGTH_SHORT).show();
                                                            Intent i = new Intent(AddOfferActivity.this, ManagerInterfaceActivity.class);
                                                            startActivity(i);
                                                        }
                                                    })
                                                    .addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Log.w("errTAG", "Error adding offer", e);
                                                            Toast.makeText(AddOfferActivity.this, "Offer has not been added successfully!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        }

                                    }

                                }
                            }
                        });




            }
        });

        storageReference = FirebaseStorage.getInstance().getReference();
    }

    // Helper method to upload the image to Firestore Storage
    private void uploadImageToFirestoreStorage(Uri imageUri, OnImageUploadListener listener) {
        String imageName = UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageReference.child("images/" + imageName);

        imageRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        imageRef.getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        String imageUrl = uri.toString();
                                        listener.onImageUploadSuccess(imageUrl);
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        listener.onImageUploadFailure(e);
                                    }
                                });
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        listener.onImageUploadFailure(e);
                    }
                });
    }

    // Helper interface for image upload callbacks
    private interface OnImageUploadListener {
        void onImageUploadSuccess(String imageUrl);
        void onImageUploadFailure(Exception e);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_IMAGE_PICKER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            selectedImageUri = data.getData();
            photoImageView.setImageURI(selectedImageUri);
        }
    }
}
