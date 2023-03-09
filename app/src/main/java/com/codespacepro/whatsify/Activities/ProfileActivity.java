package com.codespacepro.whatsify.Activities;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.codespacepro.whatsify.Models.Users;
import com.codespacepro.whatsify.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    ImageView Cover, SelectImage;
    CircleImageView Profile;
    TextView Username, FullName, Email, Gender;
    Button Submit;
    MaterialButton Logout;

    CardView EditProfilePic;

    FirebaseAuth mAuth;
    FirebaseDatabase db;
    DatabaseReference myRef;

    private static final int REQUEST_CODE_PICK_IMAGE = 1;
    private static final int REQUEST_CODE_PICK_COVER = 2;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 12;
    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    ProgressDialog progressDialog;
    ImageView backFromProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        myRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid());


        fetchingProfilePic();
        fetchingCoverPic();


        receivingdata();

        init();

        backFromProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        SelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectCoverImage();
            }
        });

        Logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mAuth.getCurrentUser() != null) {
                    mAuth.signOut();
                    startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                }
            }
        });

        Submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitdata();
            }
        });

        EditProfilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadProfilePic();
            }
        });

    }

    private void selectCoverImage() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_COVER);
    }

    private void fetchingProfilePic() {

        // Get a reference to the Firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

// Get a reference to the user's profile image in Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("pic");

// Retrieve the profile image URL from Firebase Realtime Database
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imageUrl = snapshot.getValue(String.class);

                    // Load the profile image into the ImageView using Glide
                    Glide.with(getApplicationContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.avatar_profile)
                            .into(Profile);
                } else {
                    // Handle the case where the user doesn't have a profile image set
                    Log.w(TAG, "User profile image not found in Firebase Realtime Database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Log.e(TAG, "Error loading profile image", error.toException());
            }
        });
    }


    private void fetchingCoverPic() {

        // Get a reference to the Firebase storage
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

// Get a reference to the user's profile image in Firebase Realtime Database
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("cover");

// Retrieve the profile image URL from Firebase Realtime Database
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String imageUrl = snapshot.getValue(String.class);

                    // Load the profile image into the ImageView using Glide
                    Glide.with(getApplicationContext())
                            .load(imageUrl)
                            .placeholder(R.drawable.cover)
                            .into(Cover);
                } else {
                    // Handle the case where the user doesn't have a profile image set
                    Log.w(TAG, "User profile image not found in Firebase Realtime Database");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle the error
                Log.e(TAG, "Error loading profile image", error.toException());
            }
        });
    }

    private void uploadProfilePic() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            String imagePath = getPath(selectedImageUri);
            uploadImageToFirebase(imagePath);
            // Show a progress dialog
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading image...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        if (requestCode == REQUEST_CODE_PICK_COVER && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            String imagePath = getPath(selectedImageUri);
            uploadCoverToFirebase(imagePath);
            // Show a progress dialog
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Uploading image...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    private String getPath(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        return filePath;
    }

    private void uploadImageToFirebase(String imagePath) {
        try {
            File file = new File(imagePath);
            String fileName = file.getName().replaceAll("[^a-zA-Z0-9.-]", "").replace(".", "_");
            mStorage = FirebaseStorage.getInstance().getReference().child("images").child(fileName);
            mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("pic");

            Uri imageUri = Uri.fromFile(file);
            mStorage.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                mStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = uri.toString();
                    mDatabase.setValue(url);
                    progressDialog.dismiss();
                    fetchingProfilePic();
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show());
        } catch (Exception exception) {
            exception.getLocalizedMessage();
            progressDialog.dismiss();
        }

    }

    private void uploadCoverToFirebase(String imagePath) {
        try {
            File file = new File(imagePath);
            String fileName = file.getName().replaceAll("[^a-zA-Z0-9.-]", "").replace(".", "_");
            mStorage = FirebaseStorage.getInstance().getReference().child("cover").child(fileName);
            mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(mAuth.getCurrentUser().getUid()).child("cover");

            Uri imageUri = Uri.fromFile(file);
            mStorage.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
                mStorage.getDownloadUrl().addOnSuccessListener(uri -> {
                    String url = uri.toString();
                    mDatabase.setValue(url);
                    progressDialog.dismiss();
                    fetchingCoverPic();
                    Toast.makeText(this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();
                });
            }).addOnFailureListener(e -> Toast.makeText(this, "Failed to upload image", Toast.LENGTH_SHORT).show());
        } catch (Exception exception) {
            exception.getLocalizedMessage();
            progressDialog.dismiss();
        }

    }

    public void receivingdata() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Users users = snapshot.getValue(Users.class);
                Username.setText(users.getUsername());
                FullName.setText(users.getFullname());
                Email.setText(users.getEmail());
                Gender.setText(users.getGender());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfileActivity.this, "Errors" + error, Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void submitdata() {

        Dialog customeDialog = new Dialog(ProfileActivity.this);
        customeDialog.setContentView(R.layout.bottom_sheet_dialog);
        customeDialog.setCancelable(false);
        customeDialog.show();

        Button Update = customeDialog.findViewById(R.id.btnUpdate);
        MaterialButton Cancel = customeDialog.findViewById(R.id.btnCancel);

        TextInputEditText Username = customeDialog.findViewById(R.id.edit_username_update);
        TextInputEditText FullName = customeDialog.findViewById(R.id.edit_fullname_update);
        TextInputEditText Email = customeDialog.findViewById(R.id.edit_email_update);
        TextInputEditText Password = customeDialog.findViewById(R.id.edit_pass_update);


        Cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customeDialog.dismiss();
                Toast.makeText(ProfileActivity.this, "Update Cancelled", Toast.LENGTH_SHORT).show();
            }
        });
        Update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("Users");

                String usernameUpdate = Username.getText().toString().trim();
                String fullnameUpdate = FullName.getText().toString().trim();
                String emailUpdate = Email.getText().toString().trim();
                String passwordUpdate = Password.getText().toString().trim();

                if (TextUtils.isEmpty(usernameUpdate) || TextUtils.isEmpty(fullnameUpdate) || TextUtils.isEmpty(emailUpdate) || TextUtils.isEmpty(passwordUpdate)) {
                    Toast.makeText(ProfileActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user == null) {
                        Toast.makeText(ProfileActivity.this, "Unable to update user information", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String uid = user.getUid();
                    reference.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            Users existingUser = dataSnapshot.getValue(Users.class);
                            if (existingUser == null) {
                                Toast.makeText(ProfileActivity.this, "Unable to update user information", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            String pictureUrl = existingUser.getPic();
                            String coverUrl = existingUser.getCover();
                            String Gender = existingUser.getGender();
                            Users updatedUser = new Users(usernameUpdate, fullnameUpdate, emailUpdate, passwordUpdate, Gender, pictureUrl, coverUrl);
                            reference.child(uid).setValue(updatedUser).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    customeDialog.dismiss();
                                    Toast.makeText(ProfileActivity.this, "Successfully updated user information", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    customeDialog.dismiss();
                                    Toast.makeText(ProfileActivity.this, "Failed to update user information", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            customeDialog.dismiss();
                            Toast.makeText(ProfileActivity.this, "Failed to update user information", Toast.LENGTH_SHORT).show();
                        }
                    });
                }


            }
        });

    }

    public void init() {
        Cover = (ImageView) findViewById(R.id.cover_profile);
        SelectImage = (ImageView) findViewById(R.id.select_image);
        Profile = (CircleImageView) findViewById(R.id.circleImageView_profile);
        EditProfilePic = (CardView) findViewById(R.id.edit_profile_pic);
        backFromProfile = (ImageView) findViewById(R.id.backfromProfile);

        Username = (TextView) findViewById(R.id.username_profile);
        FullName = (TextView) findViewById(R.id.fullname_profile);
        Email = (TextView) findViewById(R.id.email_profile);
        Gender = (TextView) findViewById(R.id.gender_profile);

        Submit = (Button) findViewById(R.id.btnSubmit);
        Logout = (MaterialButton) findViewById(R.id.btnCancel);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}