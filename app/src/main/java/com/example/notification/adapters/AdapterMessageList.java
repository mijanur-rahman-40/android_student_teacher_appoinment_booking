package com.example.notification.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.notification.R;
import com.example.notification.models.ModelMessage;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.List;

public class AdapterMessageList extends RecyclerView.Adapter<AdapterMessageList.MessageViewHolder> {
    private static final int SENT = 0;
    private static final int RECEIVED = 1;


    private Context context;


    private List<ModelMessage> messageEntities;

    AdapterMessageList(List<ModelMessage> messageEntities, Context context) {
        this.messageEntities = messageEntities;
        this.context = context;

    }


    @Override
    public int getItemViewType(int position) {
        if (messageEntities.get(position).getUserID().equals("some id"))
            return SENT;
        return RECEIVED;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        int layoutId = (i == SENT) ? R.layout.sent_message_holder : R.layout.received_message_holder;

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);


        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder messageViewHolder, int i) {
        final ModelMessage m = messageEntities.get(i);

        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("EEE, MMM dd  |  hh:mm a");


        messageViewHolder.text.setText(m.getMessage());
        messageViewHolder.date.setText(format.format(m.getDate()));
        messageViewHolder.text.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final Snackbar snackbar = Snackbar.make(v,"Delete message?", Snackbar.LENGTH_LONG)
                        .setAction("DELETE", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        });
                snackbar.setActionTextColor(Color.RED);
                snackbar.show();
                return true;
            }
        });
;
    }

    @Override
    public int getItemCount() {
        return messageEntities.size();
    }

    public class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView text;
        TextView date;

        MessageViewHolder(@NonNull final View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.messageText);
            date = itemView.findViewById(R.id.date);
            text.setOnClickListener(this);
        }

        void setDateVisibility(Boolean p) {
            if (p) {
                date.setVisibility(View.VISIBLE);
            } else {
                date.setVisibility(View.GONE);
            }
        }

        @Override
        public void onClick(View view) {
            ModelMessage entity = messageEntities.get(getAdapterPosition());

        }
    }


    void updateData(List<ModelMessage> lst) {
        messageEntities = lst;
        notifyDataSetChanged();
    }
}
