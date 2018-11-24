package com.github.pocmo.sensordashboard;

import android.app.Activity;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import static com.github.pocmo.sensordashboard.Util.date;

/**
 * @author AHJ
 */

public class Calendar extends AppCompatActivity {
    MaterialCalendarView MCV;
    TextView txt;
    SharedPreferences sp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        txt = findViewById(R.id.textview);
        MCV = findViewById(R.id.calendarView);
        MCV.addDecorators(new SundayDecorator(), new SaturdayDecorator(), new OneDayDecorator());

        MCV.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                txt.setText("");
                String y = Integer.toString(date.getYear());
                String m;
                String d;
                int temp = date.getMonth()+1; //0부터 시작하므로 1 더해주기
                if (temp < 10)
                    m = "0" + Integer.toString(temp);
                else
                    m = Integer.toString(temp);
                temp = date.getDay();
                if (temp < 10)
                    d = "0" + Integer.toString(temp);
                else
                    d = Integer.toString(temp);
                sp = getSharedPreferences("smokingInformation", Activity.MODE_PRIVATE);
                int cnt = 0;
                String time = sp.getString("smokingTime", "");

                if (!time.equalsIgnoreCase("")) { //담배핀게 있으면
                    String[] str = time.split("/");

                    for (int i = 0; i < str.length; i++) {
                        if (!str[i].substring(0, 4).equalsIgnoreCase(y))
                            continue;
                        if (!str[i].substring(5, 7).equalsIgnoreCase(m))
                            continue;
                        if (!str[i].substring(8, 10).equalsIgnoreCase(d))
                            continue;

                        txt.append("Smoking Time: "+str[i].substring(11) + "\n");
                        cnt++;
                    }
                    if (cnt == 0)
                        txt.setText("There is no smoking record");
                    else
                        txt.append("Total smoking : " + cnt);
                }
                else
                    txt.setText("There is no smoking record");
            }
        });
    }


}
