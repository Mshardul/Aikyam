
package com.example.aikyam;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import static com.example.aikyam.modifyEvent.e_id;
import static com.example.aikyam.modifyEvent.venue;

public class AdapterModify extends RecyclerView.Adapter<AdapterModify.MyViewHolder> {

    private ArrayList<DataModel> dataSet;
//    private  String e_id;

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;
        TextView desc;
        TextView more;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.cv = (CardView)itemView.findViewById(R.id.card_view);
            this.textViewName = (TextView) itemView.findViewById(R.id.venues);
            this.textViewVersion = (TextView) itemView.findViewById(R.id.date);
            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
            this.desc=(TextView) itemView.findViewById(R.id.desc);
            this.more=(TextView) itemView.findViewById(R.id.more);

        }
    }

    public AdapterModify(ArrayList<DataModel> data) {
        this.dataSet = data;

    }

    static boolean state=false;
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_cards, parent, false);

        view.setOnClickListener(getAllEvents.myOnClickListener);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;
        CardView c=holder.cv;
        final TextView desc=holder.desc;
        textViewName.setText(dataSet.get(holder.getAdapterPosition()).getVenue());
        textViewVersion.setText(dataSet.get(holder.getAdapterPosition()).getDate());
        imageView.setImageResource(dataSet.get(holder.getAdapterPosition()).getImage());
        desc.setText(dataSet.get(holder.getAdapterPosition()).getDesc());
        final TextView text = holder.more;
//        final TextView des=holder.desc;
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(state==false)
                {
                    desc.setText(dataSet.get(holder.getAdapterPosition()).fullDesc());
                    text.setText("Less");
                    state=true;
                }
                else
                {
                    desc.setText(dataSet.get(holder.getAdapterPosition()).getDesc());
                    text.setText("More");
                    state=false;
                }
            }
        });
        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(v.getContext(),modifyEventInfo.class);
//            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                loginIntent.putExtra("e_id",dataSet.get(holder.getAdapterPosition()).getId());
                loginIntent.putExtra("cname",dataSet.get(holder.getAdapterPosition()).getVenue());
//            loginIntent.putExtra("desc",desc);
//            loginIntent.putExtra("date",date);

                v.getContext().startActivity(loginIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
