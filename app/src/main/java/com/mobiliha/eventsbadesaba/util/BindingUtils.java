package com.mobiliha.eventsbadesaba.util;

import android.text.Editable;
import android.text.TextWatcher;

import androidx.databinding.BindingAdapter;
import androidx.databinding.InverseBindingAdapter;
import androidx.databinding.InverseBindingListener;

import com.mobiliha.eventsbadesaba.ui.custom.CustomEditText;

public class BindingUtils {

    @BindingAdapter("customEditTextTextAttrChanged")
    public static void setListener(
            CustomEditText editText,
            final InverseBindingListener listener
    ) {
        if(listener != null) {
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void afterTextChanged(Editable s) {
                    listener.onChange();
                }
            });
        }
    }

    @BindingAdapter("customEditTextText")
    public static void setText(CustomEditText editText, String text) {
        if(!Utils.equals(text, editText.getCustomEditTextText())) {
            editText.setCustomEditTextText(text);
        }
    }

    @InverseBindingAdapter(attribute = "customEditTextText")
    public static String getText(CustomEditText editText) {
        return editText.getCustomEditTextText();
    }

}
