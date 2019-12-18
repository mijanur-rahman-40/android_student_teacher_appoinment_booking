package com.example.notification;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class RegisterActivity extends AppCompatActivity {

    TabLayout regTab;
    ViewPager regPager;
    TextView tvSignUp;
    ImageView backLogin;

    private Animation animation;
    private CardView tCard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_register);


        regTab     = findViewById(R.id.tabReg);
        regPager   = findViewById(R.id.registerPager);
        tCard = findViewById(R.id.cv2);
        tvSignUp = findViewById(R.id.tvSignUp);
        backLogin = findViewById(R.id.back_btn);

        setUpViewPager(regPager);

        regTab.setupWithViewPager(regPager);


        animation = AnimationUtils.loadAnimation(this, R.anim.uptodowndiagonal);
        tCard.setAnimation(animation);
        regPager.setAnimation(animation);

        backLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent   = new Intent(RegisterActivity.this, LoginActivity.class);
                Pair[] pairs    = new Pair[1];
                pairs[0] = new Pair<View,String>(tvSignUp,"tvSignUp");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this,pairs);
                startActivity(intent,activityOptions.toBundle());
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new StudentRegFragment(),"Student");
        adapter.addFragment(new TeacherRegFragment(),"Teacher");
        viewPager.setAdapter(adapter);
    }



//    private void writeNewUser(final String email, final String fullName, final String uid) {
//
//        saveToken(email, fullName, uid);
//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (task.isSuccessful()) {
//                            String token = task.getResult().getToken();
//                            saveToken(email, token, fullName, uid);
//                        } else {
//                            //textView.setText(task.getException().getMessage());
//                        }
//                    }
//
//                });
//    }



}
