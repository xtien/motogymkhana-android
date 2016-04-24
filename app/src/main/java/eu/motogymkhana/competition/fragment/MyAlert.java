package eu.motogymkhana.competition.fragment;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import eu.motogymkhana.competition.R;

/**
 * Created by christine on 6-2-16.
 */
public class MyAlert extends Dialog {
    private Typeface tfBold, tfBook;

    private Button alertClose;
    private TextView alertText;

    public MyAlert(Context context) {
        super(context);

        setContentView(R.layout.dialog_alert);

        alertText = (TextView) findViewById(R.id.alertText);
        alertText.setTypeface(tfBook);
        alertClose = (Button) findViewById(R.id.alertClose);
        alertClose.setText(context.getString(R.string.alertOk));
        alertClose.setTypeface(tfBold);
        alertClose.setOnClickListener(new android.view.View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    }

    public void setText(String text) {
        alertText.setText(text);
    }

    public void setClose(String text) {
        alertClose.setText(text);
    }

    public void setOnClickListener(android.view.View.OnClickListener onCLickListener) {
        alertClose.setOnClickListener(onCLickListener);
    }

}
