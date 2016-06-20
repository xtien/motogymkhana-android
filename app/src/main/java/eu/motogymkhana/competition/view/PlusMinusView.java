/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import eu.motogymkhana.competition.R;

/**
 * Created by christine on 13-5-15.
 * This view displays a plus and a minus sign, to allow for quick entry of penalty seconds. Plus adds
 * a penalty second, minus subtracts one.
 */
public class PlusMinusView extends LinearLayout {

    private final Context context;
    private TextView numberView;
    private Button plusButton;
    private Button minusButton;


    public PlusMinusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;

        init();
    }

    public PlusMinusView(Context context) {
        super(context);
        this.context = context;

        init();
    }

    private void init() {

        LayoutInflater.from(context).inflate(R.layout.plus_minus_view, this, true);


        numberView = (TextView) findViewById(R.id.number_view);
        numberView.setText("0");
        plusButton = (Button) findViewById(R.id.plus_button);
        minusButton = (Button) findViewById(R.id.minus_button);

        plusButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int number = Integer.parseInt(numberView.getText().toString());
                numberView.setText(Integer.toString(++number));
            }
        });

        minusButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                int number = Integer.parseInt(numberView.getText().toString());
                if (number > 0) {
                    numberView.setText(Integer.toString(--number));
                }
            }
        });
    }

    public void setNumber(int number) {
        numberView.setText(Integer.toString(number));
    }

    public int getNumber() {
        return Integer.parseInt(numberView.getText().toString());
    }
}
