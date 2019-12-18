package com.example.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterStudentList extends RecyclerView.Adapter<AdapterStudentList.ViewHolder> {
    List<UserStudent> studentList;
    Context context;

    public AdapterStudentList(List<UserStudent> studentList, Context context) {
        this.studentList = studentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_student,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.fullName.setText(studentList.get(i).getFullName());
        viewHolder.regNo.setText("Reg No: "+ studentList.get(i).getRegNo());
        viewHolder.dept.setText("Department: "+ studentList.get(i).getDepartment());
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fullName, regNo, dept;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.tvFullName);
            regNo = itemView.findViewById(R.id.tvRegNo);
            dept = itemView.findViewById(R.id.tvDept);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    UserStudent userStudent = studentList.get(getAdapterPosition());
                    Intent intent = new Intent(context, StudentDetailsActivity.class);
                    intent.putExtra("userStudent", userStudent);
                    context.startActivity(intent);

                }
            });
        }
    }
}
