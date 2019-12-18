package com.example.notification;


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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class MessageFragment extends Fragment {


    private List<UserTeacher> userTeacherList;
    private List<UserStudent> userStudentList;

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

        getTeacherData();

        getStudentData();

        return v;
    }


    private void getTeacherData() {
        tprogressBar.setVisibility(View.VISIBLE);

        rvTeachers.setHasFixedSize(true);

        userTeacherList = new ArrayList<>();
        rvTeachers.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false));


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("teachers");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                tprogressBar.setVisibility(View.GONE);

                if (dataSnapshot.exists()) {
                    for (DataSnapshot dsUser : dataSnapshot.getChildren()) {
                        UserTeacher userTeacher = dsUser.getValue(UserTeacher.class);
                        if (!userTeacher.getToken().equals(firebaseUser.getUid()))
                            userTeacherList.add(userTeacher);
                    }
                    AdapterTeacherList teacherAdapter = new AdapterTeacherList(userTeacherList,getContext());
                    rvTeachers.setAdapter(teacherAdapter);
                } else {
                    Toast.makeText(getContext(), "No user found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void getStudentData() {
        sprogressBar.setVisibility(View.VISIBLE);

        rvStudents.setHasFixedSize(true);

        userStudentList = new ArrayList<>();
        rvStudents.setLayoutManager(new LinearLayoutManager(getActivity()));


        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users").child("students");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                sprogressBar.setVisibility(View.GONE);

                if (dataSnapshot.exists()) {
                    for (DataSnapshot dsUser : dataSnapshot.getChildren()) {
                        UserStudent userStudent = dsUser.getValue(UserStudent.class);
                        if (!userStudent.getToken().equals(firebaseUser.getUid()))
                            userStudentList.add(userStudent);
                    }
                    AdapterStudentList teacherAdapter = new AdapterStudentList(userStudentList, getContext());
                    rvStudents.setAdapter(teacherAdapter);
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
