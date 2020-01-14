package com.example.notification.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.notification.R;
import com.example.notification.adapters.AdapterViewPager;
import com.example.notification.fragments.MessageFragment;
import com.example.notification.fragments.NotificationFragment;
import com.example.notification.models.ModelStudent;
import com.example.notification.models.ModelTeacher;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Objects;


public class MainActivity extends AppCompatActivity {

    TabLayout tabMenu;
    ViewPager viewPager;
    private FirebaseAuth firebaseAuth;
    ImageView option, myImg;
    private TextView tvMyName;
    private String userType, name, imageLink;
    LinearLayout namePhoto;
    ModelTeacher modelTeacher;
    ModelStudent modelStudent;
    boolean isDoubleClicked = false;



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
        myImg = findViewById(R.id.my_img);
        namePhoto = findViewById(R.id.namePhoto);
    }


    private void checkUserStatus() {

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        if (firebaseUser != null){
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot dsUser : dataSnapshot.getChildren()){
                            if (Objects.requireNonNull(dsUser.child("token").getValue()).toString().equals(firebaseAuth.getUid())){
                                userType = Objects.requireNonNull(dsUser.child("userType").getValue()).toString();
                                if (userType.equals("teacher")){
                                    modelTeacher = dsUser.getValue(ModelTeacher.class);
                                    assert modelTeacher != null;
                                    name = modelTeacher.getFullName();
                                    imageLink = modelTeacher.getImageLink();
                                    userType = modelTeacher.getUserType();
                                } else if(userType.equals("student")){
                                    modelStudent = dsUser.getValue(ModelStudent.class);
                                    assert modelStudent != null;
                                    name = modelStudent.getFullName();
                                    imageLink = modelStudent.getImageLink();
                                    userType = modelStudent.getUserType();
                                }

                                tvMyName.setText(name);
                                try {
                                    if (imageLink.length() == 0){
                                        myImg.setImageResource(R.drawable.avatar);
                                    } else {
                                        Picasso.get().load(imageLink).into(myImg);
                                        Log.i("ImgUri", imageLink);
                                    }
                                } catch (Exception e){
                                    Picasso.get().load(R.drawable.avatar).into(myImg);
                                }
                            }
                        }
                    }

                    namePhoto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goProfileActivity();
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
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

        if (userType.equals("student")){
            intent.putExtra("modelStudent", modelStudent);
        } else {
            intent.putExtra("modelTeacher", modelTeacher);
        }

        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() == null) {
            logOut();
        }

    }

    @Override
    public void onBackPressed() {
        if (isDoubleClicked){
            super.onBackPressed();
            finishAffinity();
        }

        this.isDoubleClicked = true;

        Toast.makeText(this, "Press back again to exit.", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                isDoubleClicked = false;
            }
        }, 2000);
    }
}
