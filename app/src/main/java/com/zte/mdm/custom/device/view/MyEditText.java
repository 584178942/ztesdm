package com.zte.mdm.custom.device.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;

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
            public void beforeTextChanged(CharSequence s, int start, int before, int count) {
                if (isTextChanged){
                    isTextChanged = false;
                    return;
                }
                isTextChanged = true;
                //处理内容空格与位数以及光标规位置的逻辑
                handleInputContent(s,start,before,count);
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
    private String content;

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
    private void handleInputContent(CharSequence s, int start, int before, int count) {
        content = s.toString();
        result = content.replace("","");
        switch (editTextMode){
            case 1://普通模式

                break;
            case 2://银行卡模式
                if (content != null && content.length() <= MAX_CARD_NUMBER_LENGTH){
                    result = "";
                    int i = 0;
                    //先把splitNmuber倍的字符串进行分割
                    while (i + splitNumber < content.length()){
                        result += content.substring(i,i+splitNumber)+" ";
                        i += splitNumber;
                    }
                    result += content.substring(i,content.length());
                } else {
                    result = result.substring(0 , result.length()-1);
                }

                break;
            case 3://手机号模式
                if (content != null && content.length() <= MAX_PHONE_NUMBER_LENGTH){
                    int length = s.toString().length();
                    if (length == 3 && length == 8){
                        result += " ";
                    }
                } else {
                    result = result.substring(0, result.length() - 1);
                }
                break;
        }
        // 获取光标开始位置
        // 必须放在设置内容之前
        int j = getSelectionStart();
        setText(result);
        //处理光标位置
        handCursor(before,j);
    }

    private void handCursor(int before, int j) {
        try {
            if (j + 1 < result.length()) {
                // 添加字符
                if (before == 0){
                    // 遇到空格，光标跳过空格，定位到空格后的位置
                    if (j%splitNumber+1 == 0){
                        setSelection(j+1);
                    } else {
                        // 否则，光标定位到内容之后 （光标默认定位方式）
                        setSelection(result.length());
                    }
                    // 回退清除一个字符
                } else if (before == 1) {
                    // 回退到上一个位置（遇空格跳过）
                    setSelection(j);
                }
            } else {

            }

        } catch (Exception e) {
            MyEditText.this.setSelection(result.length());
        }

    }

    private void handleClearIcon(boolean focused) {
        if (content != null && content.length() > 0) {
            if (focused){
                setEditTextIcon(null, null, mClearDrawdle, null);
            } else {
                setEditTextIcon(null, null, null, null);
            }
        } else {
            setEditTextIcon(null, null, null, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        // 当用户抬起手指时，判断坐标是否在图标交互区域，如果在则清空输入框内容，同时隐藏图标自己
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (x > (getWidth() - getPaddingRight() - mClearDrawdle.getIntrinsicWidth())) {
                setText("");
                setEditTextIcon(null, null, null, null);
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    protected void onFocusChanged(boolean focused, int direction, Rect previouslyFocusedRect) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect);
        handleClearIcon(focused);
        initEvent();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 设置输入框的左，上，右，下图标
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void setEditTextIcon(Drawable left, Drawable top, Drawable right, Drawable bottom) {

        setCompoundDrawables(left, top, right, bottom);
    }

}