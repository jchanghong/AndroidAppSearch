package com.jchanghong.appsearch.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import com.jchanghong.appsearch.R;


public class T9TelephoneDialpadView extends LinearLayout implements
		OnClickListener {

	public interface OnT9TelephoneDialpadView {
		void onAddDialCharacter(String addCharacter);

		void onDeleteDialCharacter(String deleteCharacter);

		void onDialInputTextChanged(String curCharacter);

	}

	private Context mContext;
	/**
	 * Inflate Custom T9 phone dialpad View hierarchy from the specified xml
	 * resource.
	 */
	private View mDialpadView; // this Custom View As the T9TelephoneDialpadView
								// of children
	private MydeleteButton mDialDeleteBtn;
	private EditText mT9InputEt;
	private OnT9TelephoneDialpadView mOnT9TelephoneDialpadView = null;

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
		/**
		 * set click listener for button("0-9",'*','#')
		 */
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
				if (null != mOnT9TelephoneDialpadView) {
					String inputStr=s.toString();
					mOnT9TelephoneDialpadView.onDialInputTextChanged(inputStr);
					mT9InputEt.setSelection(inputStr.length());
					
					// Toast.makeText(mContext,
					// "onDialInputTextChanged[" + s.toString() + "]",
					// Toast.LENGTH_SHORT).show();
				}
			}
		});

		mT9InputEt.setOnTouchListener(new OnTouchListener() {

			@SuppressLint("ClickableViewAccessibility")
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// In order to prevent the soft keyboard pops up,but also can
				// not make EditText get focus.
				return true; // the listener has consumed the event
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.dial_delete_btn:
			deleteAllDialCharacter();
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
			addSingleDialCharacter(v.getTag().toString());
			break;
		default:
			break;
		}

	}


	public void setOnT9TelephoneDialpadView(
			OnT9TelephoneDialpadView onT9TelephoneDialpadView) {
		mOnT9TelephoneDialpadView = onT9TelephoneDialpadView;
	}

	public EditText getT9InputEt() {
		return mT9InputEt;
	}


	public void deleteAllDialCharacter() {
		String curInputStr = mT9InputEt.getText().toString();
		if (curInputStr.length() > 0) {
			String deleteCharacter = curInputStr.substring(0,
					curInputStr.length());
			if (null != mOnT9TelephoneDialpadView) {
				mOnT9TelephoneDialpadView
						.onDeleteDialCharacter(deleteCharacter);
			}
			mT9InputEt.setText("");
		}
	}

	private void addSingleDialCharacter(String addCharacter) {
		String preInputStr = mT9InputEt.getText().toString();
		if (!TextUtils.isEmpty(addCharacter)) {
			mT9InputEt.setText(preInputStr + addCharacter);
			mT9InputEt.setSelection(mT9InputEt.getText().length());
			if (null != mOnT9TelephoneDialpadView) {
				mOnT9TelephoneDialpadView.onAddDialCharacter(addCharacter);
			}
		}

		// Toast.makeText(mContext, "addSingleDialCharacter[" + addCharacter +
		// "]",
		// Toast.LENGTH_SHORT).show();
	}



	public void hideT9TelephoneDialpadView() {
		if (this.getVisibility() != View.GONE) {
			this.setVisibility(View.GONE);
		}
	}


	public String getT9Input() {
		return mT9InputEt.getText().toString();
	}

	public void refreshView(){
	}
}
