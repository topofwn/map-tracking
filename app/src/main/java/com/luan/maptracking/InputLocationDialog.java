package com.luan.maptracking;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InputLocationDialog extends Dialog {
    private Context mContext;
    private InputLocationDialogListener mListener;
    private MLocation mData;
    private EditText la,lo;

    public InputLocationDialog(@NonNull Context context,InputLocationDialogListener listener) {
        super(context);
        this.mContext = context;
        this.mListener = listener;
        if(getWindow() != null){
            getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_location_input);
        if(getWindow() != null){
            View v = getWindow().getDecorView();
            v.setBackgroundResource(android.R.color.transparent);
        }
       la = findViewById(R.id.edtLat);
        lo = findViewById(R.id.edtLongt);
        Button ok = findViewById(R.id.btnOk);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!la.getText().toString().equals("")  && !lo.getText().toString().equals("")){
                    MLocation location = new MLocation(Double.parseDouble(la.getText().toString()),Double.parseDouble(lo.getText().toString()));
                    mListener.setInput(location);
                    dismiss();
                }else{
                    Toast.makeText(mContext,"Please enter latitude and longtitude",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public InputLocationDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }
}
