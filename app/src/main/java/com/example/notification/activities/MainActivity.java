package com.example.notification.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.notification.fragments.MessageFragment;
import com.example.notification.fragments.NotificationFragment;
import com.example.notification.R;
import com.example.notification.adapters.AdapterViewPager;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    TabLayout tabMenu;
    ViewPager viewPager;
    private FirebaseAuth firebaseAuth;
    ImageView option;
    private TextView tvMyName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setupViews();
        checkUserStatus();

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(MainActivity.this, option);

                popupMenu.getMenuInflater().inflate(R.menu.menu, popupMenu.getMenu());

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case(R.id.my_acc):
                                goProfileActivity();
                                return true;

                            case (R.id.logout):
                                logOut();
                                return true;
                            default:
                                return true;
                        }
                    }
                });
                popupMenu.show();
            }
        });

        setupViewPager(viewPager);

        tabMenu.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(1);  // 0 = drink , 1=food

    }

    private void setupViews() {
        firebaseAuth = FirebaseAuth.getInstance();
        tvMyName = findViewById(R.id.tvMyName);
        tabMenu     = findViewById(R.id.tabMenu);
        viewPager   = findViewById(R.id.viewPager);
        option = findViewById(R.id.option);
    }


    private void checkUserStatus() {

        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        if (firebaseUser != null){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dsUser : dataSnapshot.getChildren()){
                            if (Objects.requireNonNull(dsUser.child("token").getValue()).toString().equals(firebaseAuth.getUid())){
                                String name = Objects.requireNonNull(dsUser.child("fullName").getValue()).toString();
                                tvMyName.setText(name);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } else {

        }
    }

    private void setupViewPager(ViewPager viewPager) {
        AdapterViewPager adapter = new AdapterViewPager(getSupportFragmentManager());
        adapter.addFragment(new NotificationFragment(),"Notification");
        adapter.addFragment(new MessageFragment(),"Messages");
        viewPager.setAdapter(adapter);
    }

    private void logOut(){
        firebaseAuth.signOut();
        finish();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void goProfileActivity(){
        Intent intent = new Intent(MainActivity.this, ProfileActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this);
            startActivity(intent, activityOptions.toBundle());
        }

    }

  }
