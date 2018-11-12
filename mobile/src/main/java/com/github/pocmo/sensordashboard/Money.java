package com.github.pocmo.sensordashboard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
/*
 * @author: Sangwon
 */
public class Money extends Fragment {

    TextView textView;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_money, container, false);
        textView=(TextView)view.findViewById(R.id.moneysaved);
        String[] p = {Util.number, Util.wasted, Util.saved};
        Cursor c1 = getContext().getContentResolver().query(Util.u, p, null, null, null);
        int saved = 0, wasted = 0, b = 0;
        if (c1 != null && c1.getCount() > 0) {
            while (c1.moveToNext()) {
                saved = saved + c1.getInt(2);
                wasted = wasted + c1.getInt(1);
                b += c1.getInt(0);
            }
        }
        sharedPreferences=getContext().getSharedPreferences("smokingInformation",Activity.MODE_PRIVATE);
        int temp=sharedPreferences.getInt("smokingInformation",-1);
        textView.setText("Money Saved:    " + saved + "\nMoney Spent:    " + wasted + "\nCigarettes smoked:    " + temp);
        return view;
    }

}
