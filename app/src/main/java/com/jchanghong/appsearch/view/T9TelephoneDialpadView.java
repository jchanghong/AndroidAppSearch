package com.jchanghong.appsearch.view;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import com.jchanghong.appsearch.R;


public class T9TelephoneDialpadView extends LinearLayout implements
        OnClickListener {

    private final Context mContext;
    public EditText mT9InputEt;
    public OnT9TelephoneDialpadView mOnT9TelephoneDialpadView = null;
    /**
     * Inflate Custom T9 phone dialpad View hierarchy from the specified xml
     * resource.
     */
    private View mDialpadView; // this Custom View As the T9TelephoneDialpadView
    // of children
    private MydeleteButton mDialDeleteBtn;

    public T9TelephoneDialpadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initView();
        initListener();

    }

    private void initView() {
        LayoutInflater inflater = (LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mDialpadView = inflater.inflate(R.layout.t9_telephone_dialpad_layout,
                this);

        mDialDeleteBtn = (MydeleteButton) mDialpadView
                .findViewById(R.id.dial_delete_btn);

        mT9InputEt = (EditText) mDialpadView
                .findViewById(R.id.dial_input_edit_text);
        mT9InputEt.setCursorVisible(false);
    }

    private void initListener() {
        mDialDeleteBtn.setOnClickListener(this);
        for (int i = 0; i < 9; i++) {
            View v = mDialpadView.findViewById(R.id.dialNum1 + i);
            v.setOnClickListener(this);
        }

        mT9InputEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // TODO Auto-generated method stub

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                String inputStr = s.toString();
                mOnT9TelephoneDialpadView.onDialInputTextChanged(inputStr);
                mT9InputEt.setSelection(inputStr.length());
            }
        });
        mT9InputEt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dial_delete_btn:
                mT9InputEt.setText("");
                break;
            case R.id.dial_input_edit_text:

                break;
            case R.id.dialNum1:
            case R.id.dialNum2:
            case R.id.dialNum3:
            case R.id.dialNum4:
            case R.id.dialNum5:
            case R.id.dialNum6:
            case R.id.dialNum7:
            case R.id.dialNum8:
            case R.id.dialNum9:
                addSingleDialCharacter(((MyButton) v).tag);
                break;
            default:
                break;
        }

    }

    private void addSingleDialCharacter(String addCharacter) {
        String preInputStr = mT9InputEt.getText().toString();
        mT9InputEt.setText(preInputStr + addCharacter);
        mT9InputEt.setSelection(mT9InputEt.getText().length());
    }

    public interface OnT9TelephoneDialpadView {
        void onDialInputTextChanged(String curCharacter);

    }
}
