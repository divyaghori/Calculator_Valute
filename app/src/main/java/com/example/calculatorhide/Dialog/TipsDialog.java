package com.example.calculatorhide.Dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.calculatorhide.Activity.SplashActivity;
import com.example.calculatorhide.R;


public class TipsDialog extends Dialog {

    Context context;
    private Button Done;
    TextView setpassword,setyourpassword;
    public TipsDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_tips);
        setpassword = findViewById(R.id.setpassword);
        setyourpassword = findViewById(R.id.setyourpassword);
        setpassword.setText(SplashActivity.resources.getString(R.string.SetPass));
        setyourpassword.setText(SplashActivity.resources.getString(R.string.SetPassLabel));
        findViewById(R.id.done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

    }
}
