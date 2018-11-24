package com.github.pocmo.sensordashboard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
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
    SharedPreferences sharedPreferences2;
    SharedPreferences.Editor editor;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_money, container, false);
        textView = (TextView) view.findViewById(R.id.moneysaved);

        /*
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
        */


        sharedPreferences = getContext().getSharedPreferences("smokingInformation", Activity.MODE_PRIVATE);
        int smokingCnt = sharedPreferences.getInt("smokingInformation", -1);

        sharedPreferences2 = getContext().getSharedPreferences("smoKICK", Activity.MODE_PRIVATE);
        int day = sharedPreferences2.getInt("dly", -1); //하루에 피는 개수
        float price = sharedPreferences2.getFloat("prc", -1); //한 까치 가격
        long time = sharedPreferences2.getLong("Time", 0);

        long now=System.currentTimeMillis();

        int t=(int)((now-time)/1000/60);
        float spent=price*smokingCnt;
        float saved=(day*price)/1440*t-spent;

        if(saved<0)
            textView.setTextColor(Color.RED);
        textView.setText("Money Saved:  " + saved + " won\nMoney Spent:  " + spent+" won\nCigarettes smoked:  " + smokingCnt);
        return view;
    }

}
