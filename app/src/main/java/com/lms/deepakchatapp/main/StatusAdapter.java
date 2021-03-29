package com.lms.deepakchatapp.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lms.deepakchatapp.R;
import com.lms.deepakchatapp.UserAdapter;

import java.util.ArrayList;
import java.util.List;

import xute.storyview.StoryModel;
import xute.storyview.StoryView;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.ViewHolder> {

    List<Statusmodel> statusmodels;
    Context mcontext;
    StoryView storyView;

    public StatusAdapter(List<Statusmodel> statusmodels, Context mcontext) {
        this.statusmodels = statusmodels;
        this.mcontext = mcontext;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(mcontext).inflate(R.layout.statuslayout,parent,false);
        return new StatusAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        for(int i=0;i<5;i++)
        {
            storyView.resetStoryVisits();
            ArrayList<StoryModel> uris = new ArrayList<>();
            Log.e("storyid",""+statusmodels.get(position).getImgurl());

            // uris.add(new StoryModel(statusmodels.get(position).getImgurl(),statusmodels.get(position).getName(),statusmodels.get(position).getDay()));
            uris.add(new StoryModel("https://image.freepik.com/free-photo/abstract-background-with-low-poly-design_1048-8478.jpg","Grambon","10:15 PM"));
            uris.add(new StoryModel("https://image.freepik.com/free-photo/abstract-background-with-low-poly-design_1048-8478.jpg","Grambon","10:15 PM"));

            storyView.setImageUris(uris);

        }

    }

    @Override
    public int getItemCount() {
        return statusmodels.size();
    }

    public class ViewHolder  extends   RecyclerView.ViewHolder{



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            storyView= itemView.findViewById(R.id.storyView);

        }
    }
}


