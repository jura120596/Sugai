package ru.sugai.village.ui.components;

import android.content.Context;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import ru.sugai.village.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class AppEditText extends FrameLayout {
    TextInputLayout l;
    TextInputEditText et;
    final String xmlns="http://schemas.android.com/apk/res/android";
    final String app="http://schemas.android.com/apk/res-auto";

    public AppEditText(@NonNull Context context) {
        this(context, null);
    }

    public AppEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, com.google.android.material.R.attr.layout_constraintVertical_chainStyle);
    }

    public AppEditText(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        try {
            int hint = Integer.parseInt(attrs.getAttributeValue(xmlns, "hint").substring(1));
            l.setHint(getResources().getString(hint));
        }catch (Exception e){
            if (attrs != null) l.setHint(attrs.getAttributeValue(xmlns, "hint"));
        }
        int inputType = attrs == null ? 0 : attrs.getAttributeIntValue(xmlns, "inputType", 0);
        if (inputType != 0) et.setInputType(inputType);
        if ((inputType & InputType.TYPE_TEXT_VARIATION_PASSWORD) > 0) {
            l.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
        }
    }

    private void initView() {
        View v = inflate(getContext(), R.layout.app_edit_text_layout, this);
        et = v.findViewById(R.id.et);
        l = v.findViewById(R.id.til);
        et.setTypeface(ResourcesCompat.getFont(getContext(),R.font.vela_sans_bold));
    }
    public String getText() {
        return  et.getText().toString();
    }
    public void setText(String text) {
        et.setText(text);
    }
    public void setHint(String text) {
        l.setHint(text);
    }
    public void setError(String text) {
        l.setError(text);
        l.setErrorEnabled(text != null);
    }
    public void setHint(int text) {
        l.setHint(text);
    }

    public void addTextChangedListener(TextWatcher w) {
        et.addTextChangedListener(w);
    }
    public void setOnEditorActionListener(TextView.OnEditorActionListener w) {
        et.setOnEditorActionListener(w);
    }

    public TextInputEditText getEt() {
        return et;
    }
}
