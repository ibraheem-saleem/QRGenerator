package pmru.covid19.qrCodes.Adapters;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import pmru.covid19.R;
import pmru.covid19.qrCodes.Model.DataModel;
import pmru.covid19.qrCodes.activities.CheckSequence;
import pmru.covid19.qrCodes.activities.Show_Qr_Images;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<DataModel> dataSet;

    Context context;
    int seletion;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.folder_name_txt);

            this.imageViewIcon = (ImageView) itemView.findViewById(R.id.imageView);
        }
    }

    public CustomAdapter(Context context,ArrayList<DataModel> data,int seletion) {

        this.dataSet = data;
        this.context = context;
        this.seletion = seletion;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.folder_list_row, parent, false);



        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;
        TextView textViewVersion = holder.textViewVersion;
        ImageView imageView = holder.imageViewIcon;

        textViewName.setText(dataSet.get(listPosition).getName());




            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (seletion==0){
                        context.startActivity(new Intent(context, Show_Qr_Images.class)
                                .putExtra("folder", dataSet.get(listPosition).getName()));
                    }
                    else {
                        context.startActivity(new Intent(context, CheckSequence.class)
                                .putExtra("folder", dataSet.get(listPosition).getName()));

                    }
                }
            });







    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}