package com.example.notification;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {

    RecyclerView rvPopularFood,rvMoreFood;
    AdapterPopularFood mAdapter;
    AdapterMoreFood mAdapter2;
    String foods[]={"food1","food1","food3","food4"};
    String price[]={"5000","10000","5000","10000"};

    public UserFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_food, container, false);


        rvPopularFood   = v.findViewById(R.id.rvPopularFood);
        rvMoreFood      = v.findViewById(R.id.rvMoreFood);

        rvPopularFood.setHasFixedSize(true);
        rvMoreFood.setHasFixedSize(true);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL,false);
        rvPopularFood.setLayoutManager(layoutManager);

        RecyclerView.LayoutManager layoutManager2 = new LinearLayoutManager(getActivity());
        rvMoreFood.setLayoutManager(layoutManager2);

        ArrayList<DataUser> dataUsers = getData();

        mAdapter = new AdapterPopularFood(dataUsers,getActivity());
        rvPopularFood.setAdapter(mAdapter);

        mAdapter2 = new AdapterMoreFood(dataUsers,getActivity());
        rvMoreFood.setAdapter(mAdapter2);
        return v;
    }

    private ArrayList<DataUser> getData() {
        ArrayList<DataUser> foodArrayList = new ArrayList<>();
        for (int i = 0; i<foods.length;i++){
            DataUser dataUser = new DataUser();
            dataUser.setFullName(foods[i]);
            dataUser.setEmail(price[i]);
            foodArrayList.add(dataUser);
        }
        return foodArrayList;
    }
}
