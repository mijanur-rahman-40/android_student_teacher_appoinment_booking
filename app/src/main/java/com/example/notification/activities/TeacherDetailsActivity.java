package com.example.notification.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.R;
import com.example.notification.models.ModelTeacher;

public class TeacherDetailsActivity extends AppCompatActivity {

    TextView name, dept, designation,email;
    Button sendBtn;
    ImageView backBtn;
    ModelTeacher modelTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_teacher_details);


        setUpViews();

        modelTeacher =  (ModelTeacher) getIntent().getSerializableExtra("modelTeacher");

        if (modelTeacher != null) {
            setToTheViews(modelTeacher);
        }

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChatActivity();
            }
        });


    }

    private void goToChatActivity() {
        Intent intent = new Intent(this, ChatActivity.class);

        intent.putExtra("modelTeacher", modelTeacher);
        startActivity(intent);
    }



    private void setUpViews() {
        name = findViewById(R.id.tvTName);
        dept = findViewById(R.id.tDDept);
        designation = findViewById(R.id.tDesig);
        email = findViewById(R.id.tDEmail);
        sendBtn = findViewById(R.id.btnSendMsg);
        backBtn = findViewById(R.id.back_btn);
    }


    @SuppressLint("SetTextI18n")
    private void setToTheViews(ModelTeacher modelTeacher) {
        name.setText(modelTeacher.getName());
        dept.setText("Department: " + modelTeacher.getDept());
        email.setText("Email: " + modelTeacher.getEmail());
        designation.setText("Designation: " + modelTeacher.getDesignation());
    }



}
