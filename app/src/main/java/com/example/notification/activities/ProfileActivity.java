package com.example.notification.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.example.notification.R;
import com.example.notification.adapters.AdapterFreeTime;
import com.example.notification.adapters.AdapterViewPager;
import com.example.notification.fragments.StudentRegFragment;
import com.example.notification.fragments.TeacherRegFragment;
import com.example.notification.models.ModelFreeTime;
import com.example.notification.models.ModelStudent;
import com.example.notification.models.ModelTeacher;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;

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
//    DatePickerDialog datePickerDialog;
//    TimePickerDialog timePickerDialog;
//    NumberPicker numberPicker;
    List<ModelFreeTime> freeTimeList;
    AdapterFreeTime adapterFreeTime;
    RecyclerView freeTimeRv;

    ViewPager aptPager;
    TabLayout aptTab;

    boolean clicked = false;


    Calendar cldr;
//    int day, month, year, hour, minute;
    private StorageReference storageReference;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private Button addFreeTimeBtn, submitBtn, cancelBtn;
    private ImageView tpImage, spImage, backBtn;
//    private CardView addCard;
    private LinearLayout tpLayout, spLayout;
    private Animation animation;
    private LinearLayout tProLv;
    private TextView tName, department, designation, email, regNo, session, semester, proDetails;
//    private TextView datePick, startTimePick, endTimePick, freeSlot;

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
        databaseReference = FirebaseDatabase.getInstance().getReference();

        if (modelStudent != null) {
            setContentView(R.layout.activity__student_profile);
            setupStudentViews();
            setActionsToStudent();

        } else if (modelTeacher != null) {
            setContentView(R.layout.activity_teacher_profile);

            setupTeacherViews();

            setUpViewPager(aptPager);
            aptTab.setupWithViewPager(aptPager);


            setActionsToTeacher();
//            retrieveMyAppointment();

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

    private void setUpViewPager(ViewPager aptPager) {
        AdapterViewPager adapter = new AdapterViewPager(getSupportFragmentManager());
        adapter.addFragment(new StudentRegFragment(),"Owned");
        adapter.addFragment(new TeacherRegFragment(),"Requested");
        aptPager.setAdapter(adapter);

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
        proDetails.setOnClickListener(new View.OnClickListener() {

            Drawable hide = getResources().getDrawable(R.drawable.eye_off);
            Drawable show = getResources().getDrawable(R.drawable.eye);

            @Override
            public void onClick(View v) {
                clicked = !clicked;
                if (clicked){
                    tProLv.setVisibility(View.VISIBLE);
                    proDetails.setCompoundDrawablesWithIntrinsicBounds( null, null, hide, null);
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uptodown);
                    tProLv.setAnimation(animation);
                } else {
                    tProLv.setVisibility(View.GONE);
                    proDetails.setCompoundDrawablesWithIntrinsicBounds( null, null, show, null);
                }
            }
        });

        tName.setText(modelTeacher.getName());
        department.setText("Department: " + modelTeacher.getDept());
        designation.setText("Designation: " + modelTeacher.getDesignation());
        email.setText("Email: " + modelTeacher.getEmail());
        try {
            Picasso.get().load(modelTeacher.getImageLink()).into(tpImage);
        } catch (Exception e) {
            e.printStackTrace();
        }


//        datePick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setAppointmentDate(datePick);
//            }
//        });

//        startTimePick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                setAppointmentTime(startTimePick);
//            }
//        });
//        endTimePick.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setAppointmentTime(endTimePick);
//            }
//        });

        tpImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickPhotoFrom();
            }
        });

//        freeSlot.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pickNumber();
//            }
//        });

//        addFreeTimeBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.setVisibility(View.GONE);
//                cancelBtn.setVisibility(View.VISIBLE);
//                addCard.setVisibility(View.VISIBLE);
//                animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uptodown);
//                addCard.setAnimation(animation);
//            }
//        });

//        submitBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String date = datePick.getText().toString();
//                String startTime = startTimePick.getText().toString();
//                String endTime = endTimePick.getText().toString();
//                String slot = freeSlot.getText().toString();
//                String owner = user.getUid();
//
//                Log.i("ERROR", date + startTime+ owner);
//
//                if (!date.isEmpty() && !startTime.isEmpty() && !endTime.isEmpty()){
//                    storeFreeTimeToDatabase(date, startTime, endTime, owner, slot);
//                } else {
//                    Toast.makeText(ProfileActivity.this, "These Field Are Required", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

//        cancelBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                datePick.setText("");
//                startTimePick.setText("");
//                endTimePick.setText("");
//                freeSlot.setText("");
//                addCard.setVisibility(View.GONE);
//                addFreeTimeBtn.setVisibility(View.VISIBLE);
//                v.setVisibility(View.GONE);
//
//            }
//        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

//    private void storeFreeTimeToDatabase(String date, String startTime, String endTime, String owner, String slot) {
//
//        String scheduleID = databaseReference.child("freeTimes").push().getKey();
//
//        ModelFreeTime modelFreeTime = new ModelFreeTime(date, startTime, endTime, owner, slot, scheduleID);
//
//        assert scheduleID != null;
//        databaseReference.child("freeTimes").child(scheduleID).setValue(modelFreeTime)
//                .addOnCompleteListener(new OnCompleteListener<Void>() {
//
//
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        datePick.setText("");
//                        startTimePick.setText("");
//                        endTimePick.setText("");
//                        freeSlot.setText("");
//                        addCard.setVisibility(View.GONE);
//                        addFreeTimeBtn.setVisibility(View.VISIBLE);
//                        cancelBtn.setVisibility(View.GONE);
//
//                        Toast.makeText(ProfileActivity.this, "Free Time Added", Toast.LENGTH_SHORT).show();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(ProfileActivity.this, "Data Sent Failed", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

//    private void retrieveMyAppointment() {
//        freeTimeList = new ArrayList<>();
//
//        try {
//            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("freeTimes");
//
//
//            dbRef.addValueEventListener(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    freeTimeList.clear();
//                    for (DataSnapshot ds : dataSnapshot.getChildren()){
//
//                        ModelFreeTime modelFreeTime = ds.getValue(ModelFreeTime.class);
//
//                        Log.i("Retrieve", ds.toString());
//
//                        assert modelFreeTime != null;
//                        if (modelFreeTime.getOwner().equals(user.getUid())){
//                            freeTimeList.add(modelFreeTime);
//                        }
//
//                    }
//
//
//                    adapterFreeTime = new AdapterFreeTime(freeTimeList, ProfileActivity.this, modelTeacher);
//
//                    LinearLayoutManager linearLayout = new LinearLayoutManager(ProfileActivity.this);
//
//                    freeTimeRv.setLayoutManager(linearLayout);
//
//                    freeTimeRv.setAdapter(adapterFreeTime);
//
//                    Log.i("LIST", (String.valueOf(freeTimeList.size())));
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });
//        }catch (Exception e){
//            e.printStackTrace();
//        }
//
//
//    }

//    private void pickNumber() {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        LayoutInflater inflater = getLayoutInflater();
//        View theView = inflater.inflate(R.layout.number_picker_dialog, null);
//        numberPicker = theView.findViewById(R.id.number_picker);
//
//        builder.setView(theView)
//                .setPositiveButton("Ok",new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        freeSlot.setText(String.valueOf(numberPicker.getValue()));
//                        Log.d("DBG","Price is: "+numberPicker.getValue());
//                    }
//                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//
//            }
//        });
//
//        numberPicker.setMinValue(1);
//        numberPicker.setMaxValue(20);
//
//        builder.create();
//        builder.show();
//
//    }

//    private void setAppointmentDate(final TextView datePick) {
//        datePickerDialog = new DatePickerDialog(ProfileActivity.this,
//                new DatePickerDialog.OnDateSetListener() {
//
//                    @Override
//                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
//
//                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM, yyyy");
//
//                        Calendar cal = Calendar.getInstance();
//
//                        cal.setTimeInMillis(0);
//                        cal.set(year, monthOfYear, dayOfMonth, 0, 0, 0);
//                        Date date = cal.getTime();
//                        String dateTime = formatter.format(date);
//                        datePick.setText(dateTime);
//
//                    }
//                }, year, month, day);
//        datePickerDialog.show();
//    }

//    private void setAppointmentTime(final TextView timePick) {
//        timePickerDialog = new TimePickerDialog(ProfileActivity.this,
//                new TimePickerDialog.OnTimeSetListener() {
//
//                    @Override
//                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//
//                        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("hh:mm a");
//
//                        Calendar cal = Calendar.getInstance();
//
//                        cal.setTimeInMillis(0);
//                        cal.set(0, 0, 0, hourOfDay, minute, 0);
//                        Date date = cal.getTime();
//                        String dateTime = formatter.format(date);
//                        timePick.setText(dateTime);
//
//                    }
//                }, hour, minute, false);
//        timePickerDialog.show();
//    }

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
//        addFreeTimeBtn = findViewById(R.id.btnAddTime);
//        submitBtn = findViewById(R.id.addBtn);
//        cancelBtn = findViewById(R.id.cancelBtn);
//        addCard = findViewById(R.id.addFreeTimeCard);
        tName = findViewById(R.id.tvTPName);
        department = findViewById(R.id.tPDept);
        designation = findViewById(R.id.tPDesig);
        //tpLayout = findViewById(R.id.tPLayout);
        email = findViewById(R.id.tPEmail);
        tpImage = findViewById(R.id.tp_img);
        backBtn = findViewById(R.id.back_btn);
//        datePick = findViewById(R.id.dateFreePick);
//        startTimePick = findViewById(R.id.startTimePick);
//        endTimePick = findViewById(R.id.endTimePick);
//        freeSlot = findViewById(R.id.noOfSlot);
        aptPager = findViewById(R.id.tAptPager);
        aptTab = findViewById(R.id.tabApt);
        proDetails = findViewById(R.id.proDetails);
        tProLv= findViewById(R.id.tProLv);

//        freeTimeRv = findViewById(R.id.tFreeTimeRv);

//        cldr = Calendar.getInstance();
//        day = cldr.get(Calendar.DAY_OF_MONTH);
//        month = cldr.get(Calendar.MONTH);
//        year = cldr.get(Calendar.YEAR);
//        hour = cldr.get(Calendar.HOUR_OF_DAY);
//        minute = cldr.get(Calendar.MINUTE);


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

                    databaseReference.child("users").child(user.getUid()).updateChildren(results)
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
