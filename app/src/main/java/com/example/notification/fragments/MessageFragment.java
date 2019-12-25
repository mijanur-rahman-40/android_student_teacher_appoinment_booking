package com.example.notification.fragments;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.R;
import com.example.notification.adapters.AdapterStudentList;
import com.example.notification.adapters.AdapterTeacherList;
import com.example.notification.models.ModelStudent;
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
import java.util.Objects;


public class MessageFragment extends Fragment {


    private List<ModelTeacher> modelTeacherList;
    private List<ModelStudent> modelStudentList;

    private RecyclerView rvTeachers;
    private RecyclerView rvStudents;

    private ProgressBar tprogressBar;
    private ProgressBar sprogressBar;

    public MessageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_message, container, false);

        tprogressBar = v.findViewById(R.id.tProgressBar);
        rvTeachers = v.findViewById(R.id.rvTeacher);

        sprogressBar = v.findViewById(R.id.sProgressBar);
        rvStudents = v.findViewById(R.id.rvStudent);

        getUserData();

        return v;
    }


    private void getUserData() {
        tprogressBar.setVisibility(View.VISIBLE);
        sprogressBar.setVisibility(View.VISIBLE);

        rvTeachers.setHasFixedSize(true);
        modelTeacherList = new ArrayList<>();

        rvStudents.setHasFixedSize(true);
        modelStudentList = new ArrayList<>();

        rvTeachers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));
        rvStudents.setLayoutManager(new LinearLayoutManager(getActivity()));


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tprogressBar.setVisibility(View.GONE);
                sprogressBar.setVisibility(View.GONE);

                if (dataSnapshot.exists()) {
                    for (DataSnapshot dsUser : dataSnapshot.getChildren()) {

                        if (Objects.requireNonNull(dsUser.child("userType").getValue()).toString().equals("teacher")){
                            ModelTeacher modelTeacher = dsUser.getValue(ModelTeacher.class);

                            assert modelTeacher != null;
                            assert firebaseUser != null;
                            if (!modelTeacher.getToken().equals(firebaseUser.getUid()))
                                modelTeacherList.add(modelTeacher);

                        }
                        if (dsUser.child("userType").getValue().toString().equals("student")){
                            ModelStudent modelStudent = dsUser.getValue(ModelStudent.class);

                            assert modelStudent != null;
                            assert firebaseUser != null;
                            if (!modelStudent.getToken().equals(firebaseUser.getUid()))
                                modelStudentList.add(modelStudent);

                        }
                    }
                    AdapterTeacherList teacherAdapter = new AdapterTeacherList(modelTeacherList,getContext());
                    rvTeachers.setAdapter(teacherAdapter);

                    AdapterStudentList studentAdapter = new AdapterStudentList(modelStudentList, getContext());
                    rvStudents.setAdapter(studentAdapter);
                } else {
                    Toast.makeText(getContext(), "No user found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
