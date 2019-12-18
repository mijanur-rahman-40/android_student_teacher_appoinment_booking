package com.example.notification;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterTeacherList extends RecyclerView.Adapter<AdapterTeacherList.ViewHolder> {
    List<UserTeacher> teacherList;
    Context context;

    AdapterTeacherList(List<UserTeacher> teacherList, Context context) {
        this.teacherList = teacherList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_teacher,viewGroup,false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.tName.setText(teacherList.get(i).getName());
        viewHolder.dept.setText(teacherList.get(i).getDept());
        viewHolder.noOfReq.setText(teacherList.get(i).getNoOfStApll());

    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tName,dept, noOfReq;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tName    = itemView.findViewById(R.id.teacherName);
            dept       = itemView.findViewById(R.id.dept);
            noOfReq = itemView.findViewById(R.id.noOfApnt);
        }
    }
}
