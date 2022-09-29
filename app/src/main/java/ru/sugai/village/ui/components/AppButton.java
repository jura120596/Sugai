package ru.sugai.village.ui.components;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import ru.sugai.village.R;


public class AppButton extends androidx.appcompat.widget.AppCompatButton {
    public AppButton(@NonNull Context context) {
        this(context, null);
    }

    public AppButton(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, com.google.android.material.R.attr.buttonStyle);
    }

    public AppButton(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setTypeface(ResourcesCompat.getFont(context,R.font.vela_sans_bold));
        setBackgroundResource(R.drawable.blue_btn_small);
        setTextColor(Color.WHITE);
        setPadding(0,0,0,0);
        setAllCaps(false);

    }
}
