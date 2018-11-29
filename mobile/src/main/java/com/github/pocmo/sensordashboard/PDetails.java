package com.github.pocmo.sensordashboard;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * @author: Sangwon
 */
public class PDetails extends Fragment {

    TextView textView, date;
    SharedPreferences sharedPreferences;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdetails, container, false);

        textView = (TextView) view.findViewById(R.id.textdetails);
        date = (TextView) view.findViewById(R.id.date);

        sharedPreferences = getActivity().getSharedPreferences("smoKICK", Context.MODE_PRIVATE);
        textView.setText("\n\n\nHi,  " + sharedPreferences.getString("Name", "Name"));
        Date d = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd,MMM,yyyy");
        date.setText("Today: " + dateFormat.format(d).toString() + "\nWe Appreciate your effort to quit Smoking.");
        return view;
    }

}