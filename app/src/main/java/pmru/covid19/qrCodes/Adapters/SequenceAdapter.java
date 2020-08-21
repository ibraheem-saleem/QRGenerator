package pmru.covid19.qrCodes.Adapters;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
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


public class SequenceAdapter extends RecyclerView.Adapter<SequenceAdapter.MyViewHolder> {

    private ArrayList<String> dataSet;

    Context context;
    int seletion;
    private String prefex_Seven;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView textViewVersion;
        ImageView imageViewIcon;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.sequence_txt);
        }
    }

    public SequenceAdapter(Context context, ArrayList<String> data) {

        this.dataSet = data;
        this.context = context;
        Log.d("executed",data.size()+"");
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.used_sequence_row, parent, false);



        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

        TextView textViewName = holder.textViewName;

        String tempSequence=dataSet.get(listPosition);


        String[] sequence=tempSequence.split(" ");

        String start =sequence[0];
        String end =sequence[1];

        textViewName.setText(addprefValues(start)+"   To   "+addprefValues(end));


    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public String addprefValues(String number){

        int i= Integer.parseInt(number);
        if (i<=9){
            prefex_Seven="000000"+i;
        }
        else if (i>9 && i<=99){
            prefex_Seven="00000"+i;
        }

        else if (i>99 && i<=999){
            prefex_Seven="0000"+i;
        }

        else if (i>999 && i<=9999){
            prefex_Seven="000"+i;
        }
        else if (i>9999 && i<=99999){
            prefex_Seven="00"+i;
        }

        else if (i>99999 && i<=999999){
            prefex_Seven="0"+i;
        }
        else if (i>999999 && i<=9999999){
            prefex_Seven= String.valueOf(i);
        }
        return prefex_Seven;
    }
}