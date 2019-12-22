package com.example.notification.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notification.R;
import com.example.notification.models.ModelTeacher;
import com.example.notification.activities.LoginActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class TeacherRegFragment extends Fragment {

    private static final String NODE_USERS = "users";
    public static final String USER_TYPE= "teacher";
    private DatabaseReference databaseUser;
    private EditText emailInput, fullnameInput, passInput, rePassInput, deptInput, desigInput;
    private FirebaseAuth firebaseAuth;
    private Button signInBtn;

    public TeacherRegFragment() {

    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View itemView = inflater.inflate(R.layout.fragment_reg_teacher, container, false);
        setUpView(itemView);
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createUser();
            }
        });


        return itemView;
    }

    private void setUpView(View item) {
        emailInput = item.findViewById(R.id.temail_input);
        fullnameInput = item.findViewById(R.id.tfullname_input);
        passInput = item.findViewById(R.id.tpass_input);
        rePassInput = item.findViewById(R.id.tre_pass_input);
        deptInput = item.findViewById(R.id.tdept_input);
        desigInput = item.findViewById(R.id.designation_input);
        signInBtn = item.findViewById(R.id.tsign_up_btn);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseUser = FirebaseDatabase.getInstance().getReference(NODE_USERS);
    }

    private void createUser() {
        final String email = emailInput.getText().toString().trim();
        final String password = passInput.getText().toString().trim();
        final String rePassword = rePassInput.getText().toString().trim();
        final String fullName = fullnameInput.getText().toString().trim();
        final String department = deptInput.getText().toString().trim();
        final String designation = desigInput.getText().toString().trim();

        if (fullName.isEmpty()) {
            fullnameInput.setError("Name is required.");
            fullnameInput.requestFocus();
            return;
        }

        if (department.isEmpty()) {
            deptInput.setError("Department is required.");
            deptInput.requestFocus();
            return;
        }
        if (designation.isEmpty()) {
            desigInput.setError("Designation is required.");
            desigInput.requestFocus();
            return;
        }


        if (email.isEmpty()) {
            emailInput.setError("Email is required.");
            emailInput.requestFocus();
            return;
        }

        if (password.isEmpty()) {
            passInput.setError("Password is required.");
            passInput.requestFocus();
            return;
        }

        if (password.length() < 6) {
            passInput.setError("Password should be at least 6 character long.");
            passInput.requestFocus();
            return;
        }

        if (!rePassword.equals(password)) {
            rePassInput.setError("Password doesn't match!");
            rePassInput.requestFocus();
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            writeUser(fullName, department, designation, email,firebaseAuth.getUid());
                            startLoginActivity();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Teacher user creation failed!", Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void startLoginActivity() {
        Intent intent = new Intent(getContext(), LoginActivity.class);
        ActivityOptions activityOptions = ActivityOptions.makeSceneTransitionAnimation(getActivity());
        startActivity(intent, activityOptions.toBundle());

    }

    private void writeUser(String name, String dept, String designation, String email, String token) {

        ModelTeacher modelTeacher = new ModelTeacher(name,dept,designation,email,token,USER_TYPE);
        databaseUser.child(token).setValue(modelTeacher)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getContext(), "Teacher user created successfully!", Toast.LENGTH_SHORT).show();

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Data Saving failed!", Toast.LENGTH_SHORT).show();
            }
        });

        firebaseAuth.signOut();
    }
}
