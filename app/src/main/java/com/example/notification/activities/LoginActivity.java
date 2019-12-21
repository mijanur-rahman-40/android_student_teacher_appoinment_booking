package com.example.notification.activities;

import android.app.ActivityOptions;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.notification.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    /*
    1. Notification Channel
    2. Notification Builder -> to delete notification
    3. Notification Manager
    */
    public static final String CHANNEL_ID = "123";
    private static final String CHANNEL_NAME = "simplified";
    private static final String CHANNEL_DESC = "simplified Notification";
    private TextView textView;

    private EditText emailEditText, passwordEditText;
    private ProgressBar progressBar;
    private Button loginBtn;

    private FirebaseAuth firebaseAuth;

    private ImageButton btRegister;
    private TextView tvLogin;
    private Animation animation;
    private RelativeLayout logLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(CHANNEL_DESC);
            NotificationManager manager = getSystemService(NotificationManager.class);
            assert manager != null;
            manager.createNotificationChannel(channel);
        }

        //textView = findViewById(R.id.textViewToken);
        emailEditText = findViewById(R.id.editTextEmail);
        passwordEditText = findViewById(R.id.editTextPassword);
        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        logLayout = findViewById(R.id.logLayout);

        animation = AnimationUtils.loadAnimation(this, R.anim.downtoupdiagonal);
        logLayout.setAnimation(animation);

        firebaseAuth = FirebaseAuth.getInstance();

        btRegister = findViewById(R.id.btRegister);
        tvLogin = findViewById(R.id.tvLogin);
        loginBtn = findViewById(R.id.buttonSignUp);


        loginBtn.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                userLogin();
                progressBar.setVisibility(View.VISIBLE);

            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(tvLogin, "tvLogin");
                ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, pairs);
                startActivity(intent, activityOptions.toBundle());
            }
        });
    }


    private void userLogin() {
        final String emailID = emailEditText.getText().toString().trim();
        final String pass = passwordEditText.getText().toString().trim();

        if (emailID.isEmpty()) {
            emailEditText.setError("Email is required.");
            emailEditText.requestFocus();
            return;
        }
        if (pass.isEmpty()) {
            passwordEditText.setError("Password is required.");
            passwordEditText.requestFocus();
            return;
        }
        firebaseAuth.signInWithEmailAndPassword(emailID, pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startMainActivity();
                            Toast.makeText(LoginActivity.this, "Successfully logged in", Toast.LENGTH_SHORT).show();
                        } else {
                            progressBar.setVisibility(View.INVISIBLE);
                            Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();

        if (firebaseAuth.getCurrentUser() != null) {
            startMainActivity();
        }

    }

    private void startMainActivity() {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
