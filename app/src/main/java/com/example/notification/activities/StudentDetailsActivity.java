package com.example.notification.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.notification.models.ModelStudent;
import com.example.notification.R;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

public class StudentDetailsActivity extends AppCompatActivity {

    TextView name, dept, session, semester, regNo, email;
    Button sendBtn;
    ImageView backBtn, stDImg;
    ModelStudent modelStudent;
    FirebaseAuth firebaseAuth;


    private Animation animation;
    private LinearLayout stDetailsLv;
    private TextView stDetailsTv;
    boolean clicked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_student_details);


        setUpViews();

        modelStudent =  (ModelStudent) getIntent().getSerializableExtra("modelStudent");

        if (modelStudent != null) {
            setToTheViews(modelStudent);
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

        intent.putExtra("userId", modelStudent.getToken());

        startActivity(intent);
    }


    private void setUpViews() {
        name = findViewById(R.id.tvName);
        dept = findViewById(R.id.stDDept);
        session = findViewById(R.id.stDSes);
        semester = findViewById(R.id.stDSem);
        regNo = findViewById(R.id.stDReg);
        email = findViewById(R.id.stDEmail);
        sendBtn = findViewById(R.id.btnSendMsg);
        backBtn = findViewById(R.id.back_btn);
        stDImg = findViewById(R.id.std_img);
        firebaseAuth = FirebaseAuth.getInstance();
        stDetailsLv= findViewById(R.id.stDetailsLayout);
        stDetailsTv = findViewById(R.id.stDetails);

    }


    @SuppressLint("SetTextI18n")
    private void setToTheViews(ModelStudent modelStudent) {


        String depnt = "<b>"+"Department: "+"</b>"+ modelStudent.getDepartment();
        String eml = "<b>"+"Email: "+"</b>"+ modelStudent.getEmail();
        String ses = "<b>"+"Session: "+"</b>"+ modelStudent.getSession();
        String sem = "<b>"+"Semester: "+"</b>"+ modelStudent.getSemester();
        String reg = "<b>"+"Reg No: "+"</b>"+ modelStudent.getRegNo();

        name.setText(modelStudent.getFullName());
        dept.setText(Html.fromHtml(depnt));
        session.setText(Html.fromHtml(ses));
        semester.setText(Html.fromHtml(sem));
        regNo.setText(Html.fromHtml(reg));
        email.setText(Html.fromHtml(eml));
        try {
            Picasso.get().load(modelStudent.getImageLink()).into(stDImg);
        } catch (Exception e){
            Picasso.get().load(R.drawable.avatar).into(stDImg);
        }

        stDetailsTv.setOnClickListener(new View.OnClickListener() {

            Drawable hide = getResources().getDrawable(R.drawable.eye_off);
            Drawable show = getResources().getDrawable(R.drawable.eye);


            @Override
            public void onClick(View v) {
                clicked = !clicked;
                if (clicked){
                    stDetailsLv.setVisibility(View.GONE);
                    stDetailsTv.setCompoundDrawablesWithIntrinsicBounds( null, null, show, null);

                } else {
                    animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.uptodown);
                    stDetailsLv.setAnimation(animation);
                    stDetailsLv.setVisibility(View.VISIBLE);
                    stDetailsTv.setCompoundDrawablesWithIntrinsicBounds( null, null, hide, null);
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, activityOptions.toBundle());
        }

    }


    @Override
    protected void onResume() {
        super.onResume();


        SharedPreferences sp = getSharedPreferences("SP_USER",MODE_PRIVATE);

        SharedPreferences.Editor editor = sp.edit();

        editor.putString("Current_USERID", firebaseAuth.getCurrentUser().getUid());

        editor.apply();
    }
}
