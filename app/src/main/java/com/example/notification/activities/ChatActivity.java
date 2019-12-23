package com.example.notification.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.R;
import com.example.notification.adapters.AdapterMessageList;
import com.example.notification.models.ModelMessage;
import com.example.notification.models.ModelStudent;
import com.example.notification.models.ModelTeacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static final String NODE_CHATS= "chats";
    private TextView userName;
    private ImageView backBtn, fileChooser, voiceInput;
    private EditText messagInput;
    private ImageButton msgSend;
    private String myUid, hisUid;
    private FirebaseAuth firebaseAuth;
    private String hisImage;
    private RecyclerView chatRecyle;


    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;

    List<ModelMessage> chatList;

    AdapterMessageList adapterChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_chat);

        setupViews();

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        chatRecyle.setHasFixedSize(true);
        chatRecyle.setLayoutManager(linearLayoutManager);



        Intent intent = getIntent();
        ModelStudent modelStudent = (ModelStudent) intent.getSerializableExtra("modelStudent");
        ModelTeacher modelTeacher = (ModelTeacher) intent.getSerializableExtra("modelTeacher");

        if (modelStudent != null) {
            userName.setText(modelStudent.getFullName());
            hisUid = modelStudent.getToken();
        } else if (modelTeacher != null) {
            userName.setText(modelTeacher.getName());
            hisUid = modelTeacher.getToken();
        }


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        msgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = messagInput.getText().toString().trim();

                if (TextUtils.isEmpty(message)){
                    Toast.makeText(ChatActivity.this, "Empty message cannot be sent!", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(message);
                }
            }
        });

        readMessage();

        seenMessage();

    }

    private void seenMessage() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelMessage message = ds.getValue(ModelMessage.class);
                    if (message.getReceiver().equals(myUid) && message.getSender().equals(hisUid)) {
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("isSeen", true);

                        ds.getRef().updateChildren(hashMap);
                    }
                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessage() {
        chatList = new ArrayList<>();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("chats");

        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();

                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    ModelMessage message = ds.getValue(ModelMessage.class);

                    if (message.getReceiver().equals(myUid) && message.getSender().equals(hisUid) ||
                            message.getReceiver().equals(hisUid) && message.getSender().equals(myUid)) {
                        chatList.add(message);
                    }

                    adapterChat = new AdapterMessageList(chatList, ChatActivity.this);
                    adapterChat.notifyDataSetChanged();

                    chatRecyle.setAdapter(adapterChat);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(String message) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference(NODE_CHATS);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("EEE, MMM dd  |  hh:mm a");

        Date date = new Date();

        String dateTime = formatter.format(date);


        HashMap<String, Object> messagePack = new HashMap<>();

        messagePack.put("sender", myUid);
        messagePack.put("receiver", hisUid);
        messagePack.put("message", message);
        messagePack.put("dateTime", dateTime);
        messagePack.put("isSeen", false);

        dbRef.push().setValue(messagePack);

        messagInput.setText("");

    }

    private void setupViews() {
        userName = findViewById(R.id.tvUserName);
        backBtn = findViewById(R.id.back_btn);
        fileChooser = findViewById(R.id.file_chooser);
        voiceInput = findViewById(R.id.voice);
        messagInput = findViewById(R.id.edittext_chatbox);
        msgSend = findViewById(R.id.button_chatbox_send);
        firebaseAuth = FirebaseAuth.getInstance();
        myUid = firebaseAuth.getUid();
        chatRecyle = findViewById(R.id.reyclerview_message_list);

    }


    @Override
    protected void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
    }
}
