/*
 * Copyright (c) 2015 - 2016, Christine Karman
 * This project is free software: you can redistribute it and/or modify it under the terms of
 * the Apache License, Version 2.0. You can find a copy of the license at
 * http://www. apache.org/licenses/LICENSE-2.0.
 */

package eu.motogymkhana.competition.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import javax.inject.Inject;

import eu.motogymkhana.competition.Constants;
import eu.motogymkhana.competition.R;
import eu.motogymkhana.competition.rider.RiderManager;
import toothpick.Scope;
import toothpick.Toothpick;

/**
 * Created by christine on 19-5-15.
 * Activity for entering a text line that is uploaded to the server and shown both
 * in the app and on the web site.
 */
public class TextActivity extends BaseActivity {

    @Inject
    protected RiderManager riderManager;
    private Scope scope;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        scope = Toothpick.openScopes(Constants.DEFAULT_SCOPE, this);
        super.onCreate(savedInstanceState);
        Toothpick.inject(this, scope);

        setContentView(R.layout.activity_text);

        final EditText textView = (EditText) findViewById(R.id.edit_text);

        Button okButton = (Button) findViewById(R.id.okButton);

        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                riderManager.sendText(textView.getText().toString(),null);
                     finish();
            }
        });

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Toothpick.closeScope(this);
    }
}
