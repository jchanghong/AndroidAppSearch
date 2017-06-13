package com.jchanghong.appsearch.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jchanghong.appsearch.R;

public class MyButton extends LinearLayout {
    public final String tag;
    private final TextView textViewnumber;
    private final TextView textchars;

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        LinearLayout root;
        root = (LinearLayout) inflater.inflate(R.layout.mybutton,
                this);
        textViewnumber = (TextView) root.findViewById(R.id.textview_number);
        textchars = (TextView) root.findViewById(R.id.textView_chars);
        tag = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "tag");
        init();
    }

    private void init() {
        switch (tag) {
            case "1":
                do1();
                break;
            case "2":
                do2();
                break;
            case "3":
                do3();
                break;
            case "4":
                do4();
                break;
            case "5":
                do5();
                break;
            case "6":
                do6();
                break;
            case "7":
                do7();
                break;
            case "8":
                do8();
                break;
            case "9":
                do9();
                break;
            default:
        }
    }

    private void do9() {
        textViewnumber.setText("9");
        textchars.setText("WXYZ");
    }

    private void do8() {
        textViewnumber.setText("8");
        textchars.setText("TUV");
    }

    private void do7() {
        textViewnumber.setText("7");
        textchars.setText("PQRS");
    }

    private void do6() {
        textViewnumber.setText("6");
        textchars.setText("MNO");
    }

    private void do5() {
        textViewnumber.setText("5");
        textchars.setText("JKL");
    }

    private void do4() {
        textViewnumber.setText("4");
        textchars.setText("GHI");
    }

    private void do3() {
        textViewnumber.setText("3");
        textchars.setText("DEF");
    }

    private void do2() {
        textViewnumber.setText("2");
        textchars.setText("ABC");
    }

    private void do1() {
        textViewnumber.setText("1");
        textchars.setText("");
    }
}
