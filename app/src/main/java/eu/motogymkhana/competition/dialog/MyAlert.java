/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eu.motogymkhana.competition.R;


public class MyAlert extends Dialog {

    private Button alertClose;
    private TextView alertText;
    private MyDialogListener listener;

    public MyAlert(Context context) {
        super(context);

        setContentView(R.layout.dialog_alert);

        alertText = (TextView) findViewById(R.id.alertText);
        alertClose = (Button) findViewById(R.id.alertClose);
        alertClose.setText(context.getString(R.string.ok));
        alertClose.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (listener != null) {
                    listener.onDismiss();
                }
            }
        });

        //getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void setText(String text) {
        alertText.setText(text);
    }

    public void setClose(String text) {
        alertClose.setText(text);
    }

    public void setOnClickListener(View.OnClickListener onCLickListener) {
        alertClose.setOnClickListener(onCLickListener);
    }

    public void setListener(MyDialogListener listener) {
        this.listener = listener;
    }
}
