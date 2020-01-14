package com.example.notification.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.R;
import com.example.notification.activities.TeacherDetailsActivity;
import com.example.notification.models.ModelStudent;
import com.example.notification.models.ModelTeacher;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterTeacherList extends RecyclerView.Adapter<AdapterTeacherList.ViewHolder> {
    private List<ModelTeacher> teacherList;
    private  ModelTeacher modelTeacher;
    private ModelStudent modelStudent;
    public Context context;

    public AdapterTeacherList(List<ModelTeacher> teacherList, Context context, ModelTeacher modelTeacher, ModelStudent modelStudent) {
        this.teacherList = teacherList;
        this.context = context;
        this.modelTeacher = modelTeacher;
        this.modelStudent = modelStudent;
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
        viewHolder.tName.setText(teacherList.get(i).getFullName());
        viewHolder.dept.setText(teacherList.get(i).getDepartment());
        try {
            Picasso.get().load(teacherList.get(i).getImageLink()).into(viewHolder.tImg);
        } catch (Exception e){
            Picasso.get().load(R.drawable.avatar).into(viewHolder.tImg);
        }

    }

    @Override
    public int getItemCount() {
        return teacherList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tName,dept, noOfReq;
        private ImageView tImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tName    = itemView.findViewById(R.id.teacherName);
            dept       = itemView.findViewById(R.id.department);
            noOfReq = itemView.findViewById(R.id.noOfApnt);
            tImg = itemView.findViewById(R.id.t_avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelTeacher teacher = teacherList.get(getAdapterPosition());
                    Intent intent = new Intent(context, TeacherDetailsActivity.class);
                    intent.putExtra("teacher", teacher);
                    intent.putExtra("modelStudent", modelStudent);
                    intent.putExtra("modelTeacher", modelTeacher);
                    context.startActivity(intent);

                }
            });
        }
    }
}
