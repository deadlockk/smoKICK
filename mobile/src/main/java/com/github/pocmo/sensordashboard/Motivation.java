package com.github.pocmo.sensordashboard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
/*
 * @author: Sangwon
 */
public class Motivation extends Fragment {

    ArrayList<String> quotes;
    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_motivation, container, false);
        textView=(TextView)view.findViewById(R.id.motivationquote);
        quotes=new ArrayList<>();
        quotes.add("Smoking is a habit that drains your money and kills you slowly, one puff after another. Quit smoking, start living.");
        quotes.add("Replacing the smoke on your face with a smile today will replace illness in your life with happiness tomorrow. Quit now.");
        quotes.add("Looking for motivation to quit smoking? Just look into the eyes of your loved ones and ask yourself if they deserve to die because of your second hand smoke.");
        quotes.add("If you don’t quit smoking, you risk disease and death. But if you do, you will get happiness and good health.");
        quotes.add("All the suffering, stress and addiction comes from not realizing you already are what you are looking for.");
        quotes.add("It is in your moments of decision that your destiny is shaped.");
        quotes.add("The secret of getting ahead is getting started.");
        quotes.add("People often say that motivation doesn’t last. Well, neither does bathing – that’s why we recommend it daily.");
        quotes.add("The surest way not to fail is to determine to succeed.");
        quotes.add("Believe you can and you’re halfway there.");
        quotes.add("Our strength grows out of our weakness.");
        quotes.add("At least three times every day take a moment and ask yourself what is really important. Have the wisdom and the courage to build your life around your answer.");
        quotes.add("All behavioral or mood disorders – including depression, OCD, ADHD and addiction – have some neurochemical components, but sufferers can still work to overcome them.");
        quotes.add("Checking your ego, abandoning it, letting it go, is a huge part of recovery from addiction.");
        Collections.shuffle(quotes);
        textView.setText(quotes.get(0));
        return view;
    }

}