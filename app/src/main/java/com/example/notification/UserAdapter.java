package com.example.notification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.activities.SendNotificationActivity;
import com.example.notification.models.ModelStudent;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<ModelStudent> modelStudentList;

    public UserAdapter(Context context, List<ModelStudent> modelStudentList) {
        this.context = context;
        this.modelStudentList = modelStudentList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_users, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        ModelStudent modelStudent = modelStudentList.get(position);
        holder.textEmail.setText(modelStudent.getEmail());
        holder.textFulname.setText(modelStudent.getFullName());
    }

    @Override
    public int getItemCount() {
        return modelStudentList.size();
    }

    class UserViewHolder extends RecyclerView.ViewHolder {

        TextView textEmail, textFulname;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            textFulname = itemView.findViewById(R.id.user_fullname);
            textEmail = itemView.findViewById(R.id.textViewEmail);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ModelStudent modelStudent = modelStudentList.get(getAdapterPosition());
                    Intent intent = new Intent(context, SendNotificationActivity.class);
                    intent.putExtra("modelStudent", modelStudent);
                    context.startActivity(intent);

                }
            });
        }
    }
}
