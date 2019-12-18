package com.example.notification;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;


public class MainActivity extends AppCompatActivity {

    TabLayout tabMenu;
    ViewPager viewPager;
    private FirebaseAuth firebaseAuth;
    ImageView option;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        firebaseAuth = FirebaseAuth.getInstance();
        tabMenu     = findViewById(R.id.tabMenu);
        viewPager   = findViewById(R.id.viewPager);

        option = findViewById(R.id.option);

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

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
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
