package com.example.android.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by rdas6313 on 2/4/17.
 */

public class CutomAdapter extends ArrayAdapter<Earthquake> {

    public CutomAdapter(Context context, ArrayList<Earthquake> objects) {
        super(context, 0, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_layout,parent,false);
        }
        Earthquake currentObj = getItem(position);
        TextView magView = (TextView)convertView.findViewById(R.id.emagnitude);
        magView.setText(currentObj.getMag());

        GradientDrawable magbackground = (GradientDrawable) magView.getBackground();
        int currnetMag = (int)Double.parseDouble(currentObj.getMag());
        int magColor = R.color.magnitude10plus;
        switch(currnetMag){
            case 0:
            case 1:
                magColor = R.color.magnitude1;
                break;
            case 2:
                magColor = R.color.magnitude2;
                break;
            case 3:
                magColor = R.color.magnitude3;
                break;
            case 4:
                magColor = R.color.magnitude4;
                break;
            case 5:
                magColor = R.color.magnitude5;
                break;
            case 6:
                magColor = R.color.magnitude6;
                break;
            case 7:
                magColor = R.color.magnitude7;
                break;
            case 8:
                magColor = R.color.magnitude8;
                break;
            case 9:
                magColor = R.color.magnitude9;
                break;

        }
        magbackground.setColor(ContextCompat.getColor(getContext(),magColor));


        String place = currentObj.getPlace();
        String upperplace,lowerplace;

        int indexOf = place.indexOf("of");
        if(indexOf != -1){
            upperplace = place.substring(0,indexOf+2);
            upperplace = upperplace.toUpperCase();
            lowerplace = place.substring(indexOf+3,place.length());
        }else{
            upperplace = "NEAR THE";
            lowerplace = place;
        }



        TextView upplaceView = (TextView)convertView.findViewById(R.id.uppereplace);
        upplaceView.setText(upperplace);

        TextView lowerplaceView = (TextView)convertView.findViewById(R.id.lowereplace);
        lowerplaceView.setText(lowerplace);

        TextView dateView = (TextView)convertView.findViewById(R.id.edate);
        dateView.setText(currentObj.getDate());

        TextView timeView = (TextView)convertView.findViewById(R.id.etime);
        timeView.setText(currentObj.getTime());

        return convertView;
    }

    public void setData(ArrayList<Earthquake>data){
        clear();
        addAll(data);
        notifyDataSetChanged();
    }
}
