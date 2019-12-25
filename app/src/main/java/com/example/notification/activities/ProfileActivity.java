package com.example.notification.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.notification.R;
import com.example.notification.models.ModelStudent;
import com.example.notification.models.ModelTeacher;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    public static final String NODE_USERS = "users";
    private FirebaseAuth firebaseAuth;
    private Button addBtn, cancelBtn;
    private ImageView tpImage, backBtn;
    private CardView addCard;
    ModelTeacher modelTeacher;
    ModelStudent modelStudent;
    private TextView tName, department, designation, email;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        Intent intent = getIntent();
        modelStudent = (ModelStudent) intent.getSerializableExtra("modelStudent");
        modelTeacher = (ModelTeacher) intent.getSerializableExtra("modelTeacher");


        firebaseAuth = FirebaseAuth.getInstance();
        if (modelStudent != null){
            setContentView(R.layout.activity__student_profile);

        } else if(modelTeacher != null){
            setContentView(R.layout.activity_teacher_profile);
            setupTeacherViews();
            tName.setText(modelTeacher.getName());
            department.setText("Department: "+modelTeacher.getDept());
            designation.setText("Designation: "+modelTeacher.getDesignation());
            email.setText("Email: "+modelTeacher.getEmail());
            try {
                Picasso.get().load(modelTeacher.getImageLink()).into(tpImage);
            }catch (Exception e){
                e.printStackTrace();
            }
        }




        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setVisibility(View.GONE);
                addCard.setVisibility(View.VISIBLE);
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


    private void setupTeacherViews() {
        addBtn = findViewById(R.id.btnAddTime);
        cancelBtn = findViewById(R.id.cancelBtn);
        addCard = findViewById(R.id.addFreeTimeCard);
        tName = findViewById(R.id.tvTPName);
        department = findViewById(R.id.tPDept);
        designation = findViewById(R.id.tPDesig);
        email = findViewById(R.id.tPEmail);
        tpImage = findViewById(R.id.tp_img);
        backBtn = findViewById(R.id.back_btn);

    }


    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() == null) {
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
