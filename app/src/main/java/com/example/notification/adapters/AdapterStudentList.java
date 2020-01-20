package com.example.notification.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.models.ModelStudent;
import com.example.notification.R;
import com.example.notification.activities.StudentDetailsActivity;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AdapterStudentList extends RecyclerView.Adapter<AdapterStudentList.ViewHolder> {
    private List<ModelStudent> studentList;
    public Context context;

    public AdapterStudentList(List<ModelStudent> studentList, Context context) {
        this.studentList = studentList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_student,viewGroup,false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        String depnt = "<b>"+"Department: "+"</b>"+ studentList.get(i).getDepartment();
        String reg = "<b>"+"Reg No: "+"</b>"+ studentList.get(i).getRegNo();

        viewHolder.fullName.setText(studentList.get(i).getFullName());
        viewHolder.regNo.setText(Html.fromHtml(reg));
        viewHolder.dept.setText(Html.fromHtml(depnt));

        String img = studentList.get(i).getImageLink();

        try {

            if (img.length() == 0){
                viewHolder.stImg.setImageResource(R.drawable.avatar);
            } else {
                Picasso.get().load(img).into(viewHolder.stImg);
            }

        } catch (Exception e){
            Picasso.get().load(R.drawable.avatar).into(viewHolder.stImg);
        }
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView fullName, regNo, dept;
        private ImageView stImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fullName = itemView.findViewById(R.id.tvFullName);
            regNo = itemView.findViewById(R.id.tvRegNo);
            dept = itemView.findViewById(R.id.tvDept);
            stImg = itemView.findViewById(R.id.st_avatar);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelStudent modelStudent = studentList.get(getAdapterPosition());
                    Intent intent = new Intent(context, StudentDetailsActivity.class);
                    intent.putExtra("modelStudent", modelStudent);
                    context.startActivity(intent);

                }
            });
        }
    }
}
