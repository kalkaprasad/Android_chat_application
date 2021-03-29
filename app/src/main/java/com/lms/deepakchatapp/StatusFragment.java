package com.lms.deepakchatapp;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lms.deepakchatapp.main.ContactAdapter;
import com.lms.deepakchatapp.main.StatusAdapter;
import com.lms.deepakchatapp.main.Statusmodel;

import java.util.ArrayList;
import java.util.List;

import xute.storyview.StoryModel;
import xute.storyview.StoryPlayer;
import xute.storyview.StoryView;


public class StatusFragment extends Fragment {


//    List<Statusmodel> statusmodels;
//    StatusAdapter statusAdapter;
//    RecyclerView recyclerView;

StoryView storyView;
    public StatusFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_group, container, false);
        // Inflate the layout for this fragment
        storyView= view.findViewById(R.id.storyViewkp);

//        recyclerView=view.findViewById(R.id.status);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL,false));
//        statusmodels =new ArrayList<>();
//        statusmodels.add(new Statusmodel("http://www.kalkaprasad.com/assets/images/kalka.png","Kalka","Today:6:59PM"));
//        statusmodels.add(new Statusmodel("http://www.kalkaprasad.com/assets/images/kalka.png","Kalka","Today:6:59PM"));
//        statusmodels.add(new Statusmodel("http://www.kalkaprasad.com/assets/images/kalka.png","Kalka","Today:6:59PM"));
//        statusmodels.add(new Statusmodel("http://www.kalkaprasad.com/assets/images/kalka.png","Kalka","Today:6:59PM"));
//        statusmodels.add(new Statusmodel("http://www.kalkaprasad.com/assets/images/kalka.png","Kalka","Today:6:59PM"));
//        statusmodels.add(new Statusmodel("http://www.kalkaprasad.com/assets/images/kalka.png","Kalka","Today:6:59PM"));
//        statusAdapter=new StatusAdapter(statusmodels, getActivity());
//        recyclerView.setAdapter(statusAdapter);



        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
//
//        for(int i=0;i<5;i++)
//        {
//
//            storyView.resetStoryVisits();
//            ArrayList<StoryModel> uris = new ArrayList<>();
////        Log.e("storyid",""+statusmodels.get(position).getImgurl());
//
//            // uris.add(new StoryModel(statusmodels.get(position).getImgurl(),statusmodels.get(position).getName(),statusmodels.get(position).getDay()));
//            uris.add(new StoryModel("https://image.freepik.com/free-photo/abstract-background-with-low-poly-design_1048-8478.jpg","Grambon","10:15 PM"));
//            uris.add(new StoryModel("https://image.freepik.com/free-photo/abstract-background-with-low-poly-design_1048-8478.jpg","Grambon","10:15 PM"));
//            storyView.setImageUris(uris);
//        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
