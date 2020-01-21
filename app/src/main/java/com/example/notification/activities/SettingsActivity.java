package com.example.notification.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.notification.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingsActivity extends AppCompatActivity {

    SwitchCompat scheduleSwitch;

    SharedPreferences sp;
    SharedPreferences.Editor editor;

    private static final String TOPIC_SCHEDULE_NOTIFICATION = "SCHEDULE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        scheduleSwitch = findViewById(R.id.scheduleSwitch);

        sp = getSharedPreferences("Notification_SP",MODE_PRIVATE);

        boolean isPostEnabled = sp.getBoolean(""+TOPIC_SCHEDULE_NOTIFICATION, false);

        if (isPostEnabled){
            scheduleSwitch.setChecked(true);
        } else {
            scheduleSwitch.setChecked(false);
        }

        scheduleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = sp.edit();
                editor.putBoolean(""+TOPIC_SCHEDULE_NOTIFICATION, isChecked);
                editor.apply();

                if (isChecked){
                    subscribeToNotification();
                } else {
                    unsubscribeToNotification();
                }
            }
        });
    }

    private void unsubscribeToNotification() {
        FirebaseMessaging.getInstance().unsubscribeFromTopic(""+ TOPIC_SCHEDULE_NOTIFICATION)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "You will not receive schedule notifications";

                        if(!task.isSuccessful()){
                            msg = "Subscription failed";
                        }

                        Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void subscribeToNotification() {
        FirebaseMessaging.getInstance().subscribeToTopic(""+ TOPIC_SCHEDULE_NOTIFICATION)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "You will receive schedule notifications";

                        if(!task.isSuccessful()){
                            msg = "Subscription failed";
                        }

                        Toast.makeText(SettingsActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
