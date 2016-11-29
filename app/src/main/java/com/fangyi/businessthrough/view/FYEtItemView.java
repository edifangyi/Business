package com.fangyi.businessthrough.view;

import android.content.Context;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangyi.businessthrough.R;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * Created by FANGYI on 2016/9/9.
 */

public class FYEtItemView extends LinearLayout {


    @BindView(R.id.tvet_title)
    TextView tvetTitle;
    @BindView(R.id.tvet_input)
    EditText tvetInput;

    private String title;
    private String hint;
    private String text;


    /**
     * 初始化布局文件
     */
    private void initView(Context context) {
        //inflate方法的作用：把布局文件--》View
        //最后一个参数：添加谁进来，谁就是setting_item_view的父亲，布局文件挂载在传进来的这个控件上
        View view = View.inflate(context, R.layout.view_et_view, FYEtItemView.this);
        ButterKnife.bind(this, view);

    }



    //在代码中实例化的时候使用
    public FYEtItemView(Context context) {
        super(context);
        initView(context);
    }

    //在布局文件实例化的时候使用
    public FYEtItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
        title = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "title");
        hint = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "hint");
        text = attrs.getAttributeValue("http://schemas.android.com/apk/res-auto", "text");
    }

    //要这只样式的时候使用
    public FYEtItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    /**
     * 设置组合控件的标题
     */
    public void setTitle(String title) {
        tvetTitle.setText(title);
    }

    public void setHint(String hint) {
        tvetInput.setHint(hint);
    }

    public void setSelection( int length) {
        tvetInput.setSelection(length);
    }

    public void setText(String text) {
        tvetInput.setText(text);
    }

    public EditText getEditText() {
        return tvetInput;
    }


    public void setHintColor(int color) {
        tvetInput.setHintTextColor(color);
    }

    public void setTextColor(int color) {
        tvetInput.setTextColor(color);
    }

    public void setInputTypePassWord() {
        tvetInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }

    public void setInputTypeNumberDecimal() {
        tvetInput.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
    }

    public void setInputTypeNumber() {
        tvetInput.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
    }


    public String getInput() {
        return tvetInput.getText().toString();
    }

    public String getInputIsEmpty() {

        if (TextUtils.isEmpty(tvetInput.getText().toString())) {
            return "0";
        } else if (".".equals(tvetInput.getText().toString())){
            return "0";
        } else {
            return tvetInput.getText().toString();
        }
    }


    public String getHint() {
        return tvetInput.getHint().toString();
    }

    public void setOnEditorActionListener(TextView.OnEditorActionListener onEditorActionListener) {
        tvetInput.setOnEditorActionListener(onEditorActionListener);
    }

    public void addTextChangedListener(TextWatcher textWatcher) {
        tvetInput.addTextChangedListener(textWatcher);
    }

    public void setInputType(int typeNull) {
        tvetInput.setInputType(typeNull);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }


    public void setTitleColor(int titleColor) {
        tvetTitle.setTextColor(titleColor);
    }

    public void setTvEtInputOnFocusChangeListener(OnFocusChangeListener titleColor) {
        tvetInput.setOnFocusChangeListener(titleColor);
    }



}
