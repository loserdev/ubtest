package com.kft.mfs.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.kft.mfs.R;

import butterknife.BindView;
import butterknife.ButterKnife;



public class DoubleButtonCustomDialog extends Dialog {
    @BindView(R.id.double_button_dialog_title_tv)
    TextView titleTv;

    @BindView(R.id.double_button_dialog_message_tv)
    TextView messageTv;

    @BindView(R.id.double_button_dialog_positive_btn)
    Button positiveBtn;

    @BindView(R.id.double_button_dialog_negative_btn)
    Button negativeBtn;

    private String title, message;
    private int positiveBtnTextId, negativeBtnTextId;
    private OnPositiveBtnClickListener positiveBtnListener;
    private OnNegativeBtnClickListener negativeBtnListener;

    public DoubleButtonCustomDialog(Context context, String title, String message, int positiveBtnTextId, OnPositiveBtnClickListener positiveBtnListener, int negativeBtnTextId, OnNegativeBtnClickListener negativeBtnListener) {
        super(context,R.style.CustomDialogTheme);

        this.title = title;
        this.message = message;
        this.positiveBtnTextId = positiveBtnTextId;
        this.negativeBtnTextId = negativeBtnTextId;
        this.positiveBtnListener = positiveBtnListener;
        this.negativeBtnListener = negativeBtnListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_double_button);
        ButterKnife.bind(this);

        titleTv.setText(title);
        messageTv.setText(message);
        positiveBtn.setText(positiveBtnTextId);
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positiveBtnListener.onPositiveBtnClick(DoubleButtonCustomDialog.this);
            }
        });
        negativeBtn.setText(negativeBtnTextId);
        negativeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                negativeBtnListener.onNegativeBtnClick(DoubleButtonCustomDialog.this);
            }
        });
    }

    public interface OnPositiveBtnClickListener {
        void onPositiveBtnClick(DoubleButtonCustomDialog dialog);
    }

    public interface OnNegativeBtnClickListener {
        void onNegativeBtnClick(DoubleButtonCustomDialog dialog);
    }
}
