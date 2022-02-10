package com.poona.agrocart.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatCheckedTextView;
import androidx.appcompat.widget.AppCompatRadioButton;

import com.poona.agrocart.R;

/**
 * Created by Rahul Dasi on 6/10/2020
 */

public class CustomCheckedTextView extends AppCompatCheckedTextView {

    private static final String TAG = CustomRadioButton.class.getSimpleName();

    public CustomCheckedTextView(Context context) {
        super(context);
    }

    public CustomCheckedTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CustomCheckedTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context ctx, AttributeSet attrs) {
        if (!isInEditMode()) {
            TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomCheckedTextView);
            String customFont = a.getString(R.styleable.CustomCheckedTextView_setCustomFont);
            setCustomFont(ctx, customFont);
            a.recycle();
        }
    }

    public boolean setCustomFont(Context ctx, String asset) {
        Typeface typeface = null;
        try {
            typeface = Typeface.createFromAsset(ctx.getAssets(), asset);
        } catch (Exception e) {
            Log.e(TAG, "Unable to load typeface: "+e.getMessage());
            return false;
        }

        setTypeface(typeface);
        return true;
    }
}