package com.example.notification.activities;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.notification.R;
import com.example.notification.adapters.AdapterMessageList;
import com.example.notification.models.ModelMessage;
import com.example.notification.models.ModelStudent;
import com.example.notification.models.ModelTeacher;
import com.example.notification.notifications.Data;
import com.example.notification.notifications.MySingleton;
import com.example.notification.notifications.Sender;
import com.example.notification.notifications.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class ChatActivity extends AppCompatActivity {

    public static final String NODE_CHATS = "chats";
    ValueEventListener seenListener;
    DatabaseReference userRefForSeen;
    List<ModelMessage> chatList;
    AdapterMessageList adapterChat;
    private TextView userName;
    private ImageView backBtn, fileChooser, voiceInput, hisImg;
    private EditText messagInput;
    private ImageButton msgSend;
    private String myUid, hisUid, hisName, hisID;
    private String hisImage;
    private FirebaseAuth firebaseAuth;
    private RecyclerView chatRecyle;
    private String userId, userType;
    private boolean notify = false;
    private ModelStudent modelStudent;
    private ModelTeacher modelTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_chat);

        setupViews();
        updateToken(FirebaseInstanceId.getInstance().getToken());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        chatRecyle.setHasFixedSize(true);
        chatRecyle.setLayoutManager(linearLayoutManager);


        Intent intent = getIntent();

        userId = intent.getStringExtra("userId");


        getUser(userId);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        msgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notify = true;
                String message = messagInput.getText().toString().trim();

                if (TextUtils.isEmpty(message)) {
                    Toast.makeText(ChatActivity.this, "Empty message cannot be sent!", Toast.LENGTH_SHORT).show();
                } else {
                    sendMessage(message);
                }
            }
        });

        readMessage();

        seenMessage();

    }

    private void getUser(final String userId) {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        if (firebaseUser != null) {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        if (Objects.requireNonNull(ds.child("token").getValue()).toString().equals(userId)) {
                            userType = Objects.requireNonNull(ds.child("userType").getValue()).toString();
                            if (userType.equals("teacher")) {
                                modelTeacher = ds.getValue(ModelTeacher.class);
                                hisName = modelTeacher.getFullName();
                                userName.setText(hisName);
                                hisUid = modelTeacher.getToken();
                                hisImage = modelTeacher.getImageLink();
                                try {
                                    Picasso.get().load(modelTeacher.getImageLink()).into(hisImg);
                                } catch (Exception e) {
                                    Picasso.get().load(R.drawable.avatar).into(hisImg);
                                }
                            } else if (userType.equals("student")) {
                                modelStudent = ds.getValue(ModelStudent.class);
                                hisName = modelStudent.getFullName();
                                userName.setText(hisName);
                                hisUid = modelStudent.getToken();
                                hisImage = modelStudent.getImageLink();
                                try {
                                    Picasso.get().load(modelStudent.getImageLink()).into(hisImg);
                                } catch (Exception e) {
                                    Picasso.get().load(R.drawable.avatar).into(hisImg);
                                }
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void seenMessage() {
        userRefForSeen = FirebaseDatabase.getInstance().getReference("chats");
        seenListener = userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
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

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    ModelMessage message = ds.getValue(ModelMessage.class);

                    if (message.getReceiver().equals(myUid) && message.getSender().equals(hisUid) ||
                            message.getReceiver().equals(hisUid) && message.getSender().equals(myUid)) {
                        chatList.add(message);
                    }

                    adapterChat = new AdapterMessageList(chatList, ChatActivity.this, hisImage);
                    adapterChat.notifyDataSetChanged();

                    chatRecyle.setAdapter(adapterChat);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendMessage(final String message) {

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

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child(myUid);

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String myname = (String) dataSnapshot.child("fullName").getValue();

                Log.d("fullName", hisUid);

                if (notify) {
                    sendNotification(hisUid, myname, message);
                }

                notify = false;

                messagInput.setText("");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private void sendNotification(final String hisId, final String hisName, final String message) {


        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("tokens");

        Query query = dbRef.orderByKey().equalTo(hisId);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    if (firebaseAuth.getCurrentUser() != null) {

                        Token token = ds.getValue(Token.class);


                        final Data data = new Data(myUid, hisName + ": " + message, "New Massage", hisId, R.drawable.avatar);

                        assert token != null;
                        Sender sender = new Sender(data, token.getToken());


                        try {
                            JSONObject senderJsonObj = new JSONObject(new Gson().toJson(sender));

                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderJsonObj, new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response) {
                                    Log.d("JSON_RESPONSE", "onResponse: " + response.toString() + "\nonMsg: " + data.getBody());

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d("JSON_ERROR", "onResponse: " + error.toString());

                                }
                            }) {
                                @Override
                                public Map<String, String> getHeaders() throws AuthFailureError {
                                    Map<String, String> headers = new HashMap<>();
                                    headers.put("Content-Type", "application/json");
                                    headers.put("Authorization", "key=AAAAU0EpuEU:APA91bHE5EK3qdTJWegs9JKYCAlc8gZvbEvp41wQGOUlhGo-mtPDm6gRkOkM2JcY7tyz5iHLB0rGYFGvgoQ8FMdJfRcP0JbH9nkjcHsZakeK0Hfa29oAehUt0y0cAzCqEvUksbIkUpmq");
                                    return headers;
                                }
                            };


                            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void setupViews() {
        userName = findViewById(R.id.tvUserName);
        backBtn = findViewById(R.id.back_btn);
        fileChooser = findViewById(R.id.file_chooser);
        voiceInput = findViewById(R.id.voice);
        messagInput = findViewById(R.id.edittext_chatbox);
        msgSend = findViewById(R.id.button_chatbox_send);
        hisImg = findViewById(R.id.his_avatar);
        firebaseAuth = FirebaseAuth.getInstance();
        myUid = firebaseAuth.getUid();
        chatRecyle = findViewById(R.id.reyclerview_message_list);

    }

    @Override
    protected void onResume() {
        super.onResume();
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    protected void onPause() {
        super.onPause();
        userRefForSeen.removeEventListener(seenListener);
        updateToken(FirebaseInstanceId.getInstance().getToken());
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(this);
            startActivity(intent, activityOptions.toBundle());
        } else {
            updateToken(FirebaseInstanceId.getInstance().getToken());
        }


    }

    private void updateToken(String newToken) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("tokens");

        Token token = new Token(newToken);

        assert user != null;
        dbRef.child(user.getUid()).setValue(token);

    }


}
