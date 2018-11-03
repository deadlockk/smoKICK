package com.github.pocmo.sensordashboard;

import android.graphics.Color;
import android.graphics.Typeface;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;

import java.util.Date;
/**
 * @author AHJ
 */
public class OneDayDecorator implements DayViewDecorator {
    private CalendarDay date;

    public OneDayDecorator() { // 오늘 날짜로 생성
        date = CalendarDay.today();
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) { //오늘 날짜인지 아닌지 판단
        return date != null && day.equals(date);
    }

    @Override
    public void decorate(DayViewFacade view) { // 오늘 날짜 데코
        view.addSpan(new StyleSpan(Typeface.BOLD)); // 글씨체
        view.addSpan(new RelativeSizeSpan(1.4f)); // 사이즈
        //view.addSpan(new ForegroundColorSpan(Color.GREEN); // 색깔
    }
    public void setDate(Date date) {
        this.date = CalendarDay.from(date);
    }
}
