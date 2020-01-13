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
import com.example.notification.adapters.AdapterFreeTime;
import com.example.notification.adapters.AdapterRequestList;
import com.example.notification.models.ModelRequest;
import com.example.notification.models.ModelTeacher;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class TeacherRequestedScheduleFragment extends Fragment {

    private List<ModelRequest> modelRequests;
    private AdapterRequestList adapterRequestList;
    private RecyclerView teacherReqRv;

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;



    public TeacherRequestedScheduleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View itemView = inflater.inflate(R.layout.fragment_teacher_requested_schedule, container, false);
        setUpView(itemView);
        retrieveMyRequests();
        return itemView;
    }



    private void retrieveMyRequests() {
        modelRequests = new ArrayList<>();

        try {
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("requests");


            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    modelRequests.clear();

                    for (DataSnapshot ds : dataSnapshot.getChildren()){

                        ModelRequest modelRequest = ds.getValue(ModelRequest.class);

                        Log.i("Retrieve", ds.toString());

                        assert modelRequest != null;
                        if (modelRequest.getSenderId().equals(user.getUid())){
                            modelRequests.add(modelRequest);
                        }
                    }

                    adapterRequestList = new AdapterRequestList(modelRequests, getContext());

                    LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());

                    teacherReqRv.setLayoutManager(linearLayout);

                    teacherReqRv.setAdapter(adapterRequestList);

                    Log.i("LIST", (String.valueOf(modelRequests.size())));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }catch (Exception e){
            e.printStackTrace();
        }

    }



    private void setUpView(View itemView) {

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        teacherReqRv = itemView.findViewById(R.id.teacherRequestRv);

    }

}
