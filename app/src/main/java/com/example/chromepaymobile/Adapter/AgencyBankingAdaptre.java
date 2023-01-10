package com.example.chromepaymobile.Adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.chromepaymobile.Models.AgencyBankingModel;
import com.example.chromepaymobile.R;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;

public class AgencyBankingAdaptre extends ArrayAdapter<AgencyBankingModel> {

    public AgencyBankingAdaptre(@NonNull Context context, ArrayList<AgencyBankingModel> courseModelArrayList) {
        super(context, 0, courseModelArrayList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listitemView = convertView;
        if (listitemView == null) {
            // Layout Inflater inflates each item to be displayed in GridView.
            listitemView = LayoutInflater.from(getContext()).inflate(R.layout.agency_banking_layout, parent, false);
        }

        AgencyBankingModel courseModel = getItem(position);
        TextView courseTV = listitemView.findViewById(R.id.idTVCourse);
        ImageView courseIV = listitemView.findViewById(R.id.idIVcourse);

        courseTV.setText(courseModel.getName());
        courseIV.setImageResource(courseModel.getImage());
        return listitemView;
    }
}
