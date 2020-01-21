package com.example.notification.fragments;


import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.R;
import com.example.notification.adapters.AdapterNotificationList;
import com.example.notification.models.ModelNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {

    private RecyclerView rvNotificetion;
    private AdapterNotificationList adapterNotificationList;
    private List<ModelNotification> notifications;
    private FirebaseUser user;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_notification, container, false);
        rvNotificetion = v.findViewById(R.id.rvNotification);
        rvNotificetion.setHasFixedSize(true);

        user = FirebaseAuth.getInstance().getCurrentUser();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        rvNotificetion.setLayoutManager(layoutManager);

        getData();



        return v;
    }

    private void getData() {

        notifications = new ArrayList<>();

        try {
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("notifications");


            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    notifications.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()){

                        ModelNotification modelNotification = ds.getValue(ModelNotification.class);

                        Log.i("Retrieve", ds.toString());

                        assert modelNotification != null;
                        if (!modelNotification.getNotifierId().equals(user.getUid())){
                            notifications.add(modelNotification);
                        }
                    }

                    adapterNotificationList = new AdapterNotificationList(notifications, getContext());

                    LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());

                    rvNotificetion.setLayoutManager(linearLayout);

                    rvNotificetion.setAdapter(adapterNotificationList);

                    Log.i("LIST", (String.valueOf(notifications.size())));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
