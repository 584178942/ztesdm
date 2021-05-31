package com.zte.mdm.custom.device.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatEditText;

import com.zte.mdm.custom.device.R;

/**
 * @author ZT
 * @date
 */
public class MyEditText extends AppCompatEditText {
    public int splitNumber = 4;

    private int editTextMode = 1;

    public MyEditText(@NonNull Context context) {
        super(context);
    }

    public MyEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    //清空内容图标
    private Drawable mClearDrawdle;

    //初始化方法
    private void init(AttributeSet attr){
        //设置单行显示
        setSingleLine();
        //设置可获取焦点
        setFocusable(true);
        setFocusableInTouchMode(true);
        TypedArray t = this.getResources().obtainAttributes(attr, R.styleable.MyEditText);
        editTextMode = t.getIndex(R.styleable.MyEditText_editTextMode);
        splitNumber = t.getIndex(R.styleable.MyEditText_splitNumber);
        t.recycle();
        mClearDrawdle = this.getResources().getDrawable(R.mipmap.ic_launcher);
        mClearDrawdle.setBounds(0,0,mClearDrawdle.getIntrinsicWidth(),mClearDrawdle.getIntrinsicHeight());
        initEvent();
    }

    private boolean isTextChanged = false;
    private void initEvent(){
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (isTextChanged){
                    isTextChanged = false;
                    return;
                }
                isTextChanged = true;
                //处理内容空格与位数以及光标规位置的逻辑
               // handleInputContent(s,start,before,count);
                //处理清除图标的显示与隐藏
                handleClearIcon(true);

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }

    //卡号内容
    private String count;

    //卡号最大长度，卡号一般为19位
    public static final int MAX_CARD_NUMBER_LENGTH = 19;

    //手机号码长度
    public static final int MAX_PHONE_NUMBER_LENGTH = 11;

    //缓冲分离后的新内容串
    private String result = "";

    /**
     * 处理内容空格与位数以及光标规位置的逻辑
     * @param s
     * @param start
     * @param before
     * @param count
     */
    private void handleInputContent(CharSequence s, int start, int before, int count){

    }

    private void handleClearIcon(boolean focused){

    }

}