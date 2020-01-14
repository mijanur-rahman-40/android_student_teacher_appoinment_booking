package com.example.notification.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;

import com.example.notification.R;
import com.example.notification.adapters.AdapterFreeTime;
import com.example.notification.adapters.AdapterRequestList;
import com.example.notification.models.ModelRequest;

import java.util.List;
import java.util.Objects;

public class PendingRequestActivity extends AppCompatActivity {

    List<ModelRequest> modelRequests;
    RecyclerView seeReqRv;

    AdapterRequestList adapterRequestList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);


        setContentView(R.layout.activity_pending_request);

        Intent bundle = getIntent();

        assert bundle != null;
        modelRequests = Objects.requireNonNull(bundle.getExtras()).getParcelableArrayList("requestList");

        seeReqRv = findViewById(R.id.seereqRv);


        adapterRequestList = new AdapterRequestList(modelRequests, PendingRequestActivity.this);

        LinearLayoutManager linearLayout = new LinearLayoutManager(PendingRequestActivity.this);

        seeReqRv.setLayoutManager(linearLayout);

        seeReqRv.setAdapter(adapterRequestList);


    }
}
