package com.example.notification.activities;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.notification.R;
import com.example.notification.models.ModelStudent;
import com.example.notification.models.ModelTeacher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    public static final String NODE_USERS = "users";
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;

    String[] cameraPermissions;
    String[] storagePermissions;
    Uri image_uri;
    String storePath = "imageLink";
    ModelTeacher modelTeacher;
    ModelStudent modelStudent;
    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private Button addBtn, cancelBtn;
    private ImageView tpImage,spImage, backBtn;
    private CardView addCard;
    private LinearLayout tpLayout, spLayout;
    private Animation animation;
    private TextView tName, department, designation, email, regNo, session, semester;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        Intent intent = getIntent();
        modelStudent = (ModelStudent) intent.getSerializableExtra("modelStudent");
        modelTeacher = (ModelTeacher) intent.getSerializableExtra("modelTeacher");


        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference("profile_pictures");
        databaseReference = FirebaseDatabase.getInstance().getReference("users");

        if (modelStudent != null) {
            setContentView(R.layout.activity__student_profile);
            setupStudentViews();
            setActionsToStudent();

        } else if (modelTeacher != null) {
            setContentView(R.layout.activity_teacher_profile);

            setupTeacherViews();
            setActionsToTeacher();

        }


        //this basically return a task
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (task.isSuccessful()) {
//                            String token = task.getResult().getToken();
//                            //saveToken(token);
//                        } else {
//                            //textView.setText(task.getException().getMessage());
//                        }
//                    }
//
//                });
    }

    private void setActionsToStudent() {
        tName.setText(modelStudent.getFullName());
        session.setText("Session: " + modelStudent.getSession());
        semester.setText("Session: " + modelStudent.getSemester());
        regNo.setText("Session: " + modelStudent.getRegNo());
        department.setText("Department: " + modelStudent.getDepartment());
        email.setText("Email: " + modelStudent.getEmail());

        spImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhotoFrom();
            }
        });

        try {
            Picasso.get().load(modelStudent.getImageLink()).into(spImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setupStudentViews() {
        tName = findViewById(R.id.tvName);
        department = findViewById(R.id.stPDept);
        email = findViewById(R.id.stPEmail);
        spImage = findViewById(R.id.stp_img);
        regNo = findViewById(R.id.stPReg);
        spLayout = findViewById(R.id.sPLayout);
        session = findViewById(R.id.stPSes);
        semester = findViewById(R.id.stPSem);
        backBtn = findViewById(R.id.back_btn);

    }

    private void setActionsToTeacher() {
        tName.setText(modelTeacher.getName());
        department.setText("Department: " + modelTeacher.getDept());
        designation.setText("Designation: " + modelTeacher.getDesignation());
        email.setText("Email: " + modelTeacher.getEmail());
        try {
            Picasso.get().load(modelTeacher.getImageLink()).into(tpImage);
        } catch (Exception e) {
            e.printStackTrace();
        }

        tpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhotoFrom();
            }
        });


        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                addCard.setVisibility(View.VISIBLE);
                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uptodown);
                addCard.setAnimation(animation);
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addCard.setVisibility(View.GONE);
                addBtn.setVisibility(View.VISIBLE);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void pickPhotoFrom() {
        String[] options = {"Camera", "Gallery"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }

                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }

                }
            }
        });

        builder.create().show();
    }


    private void setupTeacherViews() {
        addBtn = findViewById(R.id.btnAddTime);
        cancelBtn = findViewById(R.id.cancelBtn);
        addCard = findViewById(R.id.addFreeTimeCard);
        tName = findViewById(R.id.tvTPName);
        department = findViewById(R.id.tPDept);
        designation = findViewById(R.id.tPDesig);
        tpLayout = findViewById(R.id.tPLayout);
        email = findViewById(R.id.tPEmail);
        tpImage = findViewById(R.id.tp_img);
        backBtn = findViewById(R.id.back_btn);

    }


    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, storagePermissions, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, cameraPermissions, CAMERA_REQUEST_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode) {
            case CAMERA_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Please enable camera and storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;


            }
            case STORAGE_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;

                    if (writeStorageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please enable storage permission", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            }

        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");

        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);

        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                assert data != null;
                image_uri = data.getData();
                uploadProPic(image_uri);
            }
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                uploadProPic(image_uri);

            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProPic(Uri uri) {
        String filePath = user.getUid();
        StorageReference storageReference1 = storageReference.child(filePath);

        storageReference1.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                while (!uriTask.isSuccessful()) ;
                Uri downloadUri = uriTask.getResult();

                if (uriTask.isSuccessful()) {
                    HashMap<String, Object> results = new HashMap<>();

                    assert downloadUri != null;
                    results.put(storePath, downloadUri.toString());

                    databaseReference.child(user.getUid()).updateChildren(results)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(ProfileActivity.this, "Image uploaded", Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this, "Error uploading!", Toast.LENGTH_SHORT).show();

                                }
                            });
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (user == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }

    }
//
//    private void saveToken(String token) {
//        String email = firebaseAuth.getCurrentUser().getEmail();
//        ModelStudent user = new ModelStudent(email, token);
//
//        DatabaseReference databaseUser = FirebaseDatabase.getInstance().getReference(NODE_USERS);
//
//        databaseUser.child(firebaseAuth.getCurrentUser().getUid()).setValue(user)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(ProfileActivity.this, "Token Saved", Toast.LENGTH_SHORT).show();
//                        } else {
//
//                        }
//                    }
//                });
//    }
}
