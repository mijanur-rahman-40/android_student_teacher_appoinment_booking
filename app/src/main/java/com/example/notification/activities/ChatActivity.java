package com.example.notification.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.R;
import com.example.notification.models.ModelStudent;
import com.example.notification.models.ModelTeacher;

public class ChatActivity extends AppCompatActivity {

    private TextView userName;
    private ImageView backBtn, fileChooser, voiceInput;
    private EditText messagInput;
    private ImageButton msgSend;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_chat);

        setupViews();

        Intent intent = getIntent();
        ModelStudent modelStudent = (ModelStudent) intent.getSerializableExtra("modelStudent");
        ModelTeacher modelTeacher = (ModelTeacher) intent.getSerializableExtra("modelTeacher");

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        if (modelStudent != null) {
            userName.setText(modelStudent.getFullName());
        } else if (modelTeacher != null) {
            userName.setText(modelTeacher.getName());
        }

    }

    private void setupViews() {
        userName = findViewById(R.id.tvUserName);
        backBtn = findViewById(R.id.back_btn);
        fileChooser = findViewById(R.id.file_chooser);
        voiceInput = findViewById(R.id.voice);
        messagInput = findViewById(R.id.edittext_chatbox);
        msgSend = findViewById(R.id.button_chatbox_send);
    }


}
