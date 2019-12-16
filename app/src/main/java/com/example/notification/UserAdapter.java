package com.example.notification;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<UserStudent> userStudentList;

    public UserAdapter(Context context, List<UserStudent> userStudentList) {
        this.context = context;
        this.userStudentList = userStudentList;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recyclerview_users, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        UserStudent userStudent = userStudentList.get(position);
        holder.textEmail.setText(userStudent.getEmail());
        holder.textFulname.setText(userStudent.getFullName());
    }

    @Override
    public int getItemCount() {
        return userStudentList.size();
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
                    UserStudent userStudent = userStudentList.get(getAdapterPosition());
                    Intent intent = new Intent(context, SendNotificationActivity.class);
                    intent.putExtra("userStudent", userStudent);
                    context.startActivity(intent);

                }
            });
        }
    }
}
