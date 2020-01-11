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
import com.example.notification.models.ModelFreeTime;
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

    private List<ModelFreeTime> freeTimeList;
    private AdapterFreeTime adapterFreeTime;
    private RecyclerView teacherreqRv;
    private ModelTeacher modelTeacher;

    private FirebaseUser user;
    private FirebaseAuth firebaseAuth;



    public TeacherRequestedScheduleFragment() {
        // Required empty public constructor
    }
    public TeacherRequestedScheduleFragment(ModelTeacher modelTeacher) {
        this.modelTeacher = modelTeacher;
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
        freeTimeList = new ArrayList<>();

        try {
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("freeTimes");


            dbRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    freeTimeList.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()){

                        ModelFreeTime modelFreeTime = ds.getValue(ModelFreeTime.class);

                        Log.i("Retrieve", ds.toString());

                        assert modelFreeTime != null;
                        if (modelFreeTime.getOwner().equals(user.getUid())){
                            freeTimeList.add(modelFreeTime);
                        }

                    }


                    adapterFreeTime = new AdapterFreeTime(freeTimeList, getContext(), modelTeacher);

                    LinearLayoutManager linearLayout = new LinearLayoutManager(getContext());

                    teacherreqRv.setLayoutManager(linearLayout);

                    teacherreqRv.setAdapter(adapterFreeTime);

                    Log.i("LIST", (String.valueOf(freeTimeList.size())));
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
        teacherreqRv = itemView.findViewById(R.id.teacherRequestRv);

    }

}
