package com.jchanghong.appsearch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jchanghong.appsearch.R;

public class MyButton extends LinearLayout {
    public final String tag;

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout root;
        root = (LinearLayout) inflater.inflate(R.layout.mybutton,
                this);
          TextView textViewnumber;
          TextView textchars;
        textViewnumber = root.findViewById(R.id.textview_number);
        textchars = root.findViewById(R.id.textView_chars);
        tag = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "tag");
        switch (tag) {
            case "1":
                textViewnumber.setText("1");
                textchars.setText("");
                break;
            case "2":
                textViewnumber.setText("2");
                textchars.setText("ABC");
                break;
            case "3":
                textViewnumber.setText("3");
                textchars.setText("DEF");
                break;
            case "4":
                textViewnumber.setText("4");
                textchars.setText("GHI");
                break;
            case "5":
                textViewnumber.setText("5");
                textchars.setText("JKL");
                break;
            case "6":
                textViewnumber.setText("6");
                textchars.setText("MNO");
                break;
            case "7":
                textViewnumber.setText("7");
                textchars.setText("PQRS");
                break;
            case "8":
                textViewnumber.setText("8");
                textchars.setText("TUV");
                break;
            case "9":
                textViewnumber.setText("9");
                textchars.setText("WXYZ");
                break;
            default:
        }
    }
}
