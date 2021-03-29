package com.lms.deepakchatapp.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.lms.deepakchatapp.MessageActivity;
import com.lms.deepakchatapp.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ContactAdapter extends  RecyclerView.Adapter<ContactAdapter.myviewholder> {
Context mcotext;
//    List<ContactModel> Contctdata;
    List<ContactModel> Contctdata;

    public ContactAdapter(Context mcotext, List<ContactModel> Contctdata) {

        this.mcotext = mcotext;
        this.Contctdata = Contctdata;
    }

    @NonNull
    @Override
    public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        view= LayoutInflater.from(mcotext).inflate(R.layout.show_contact_layout,parent,false);
        myviewholder myviewholder= new myviewholder(view);

        return myviewholder;

    }

    @Override
    public void onBindViewHolder(@NonNull final myviewholder holder, int position) {


        holder.username.setText(Contctdata.get(position).getUsername());
        holder.status.setText(Contctdata.get(position).getStatus());
        String imageurl= Contctdata.get(position).getUserprofile();
        Picasso.get().load(imageurl).placeholder(R.drawable.profile_image).into(holder.userprofile);
        holder.cardViewevent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i= new Intent(mcotext, MessageActivity.class);
                
            }
        });

    }

    @Override
    public int getItemCount() {
        return Contctdata.size();
    }

    public  class myviewholder extends RecyclerView.ViewHolder{


        TextView username;
        TextView status;
        ImageView userprofile;
        CardView cardViewevent;



        public myviewholder(@NonNull View itemView) {
            super(itemView);

            username=itemView.findViewById(R.id.Cont_username);
            status=itemView.findViewById(R.id.Cont_Status);
            userprofile=itemView.findViewById(R.id.Cont_profile_image);
            cardViewevent=itemView.findViewById(R.id.card_view);

        }
    }

}
