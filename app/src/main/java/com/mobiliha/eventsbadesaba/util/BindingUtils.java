package com.mobiliha.eventsbadesaba.util;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.widget.ImageView;
import android.widget.RadioButton;

import androidx.databinding.BindingAdapter;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.data.local.db.entity.TaskColor;

public class BindingUtils {

    @BindingAdapter("taskColor")
    public static void setTaskColor(ImageView imageView, TaskColor taskColor) {
        if(taskColor == null)
            return;

        Drawable background = imageView.getBackground();
        if(background != null) {
            background.setColorFilter(
                    taskColor.getColorCode(), PorterDuff.Mode.SRC_ATOP
            );
        }
    }

    @BindingAdapter("taskColor")
    public static void setTaskColor(RadioButton radioButton, TaskColor taskColor) {
        if(taskColor == null)
            return;

        GradientDrawable checkedShape = new GradientDrawable();
        checkedShape.setShape(GradientDrawable.OVAL);
        checkedShape.setColor(taskColor.getColorCode());
        int strokeWidth = (int) radioButton.getResources().getDimension(
                R.dimen.task_modify_colored_radio_button_stroke_width
        );
        checkedShape.setStroke(strokeWidth, Color.BLACK);

        GradientDrawable uncheckedShape = new GradientDrawable();
        uncheckedShape.setShape(GradientDrawable.OVAL);
        uncheckedShape.setColor(taskColor.getColorCode());

        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(
                new int[]{android.R.attr.state_checked}, checkedShape
        );
        drawable.addState(new int[]{}, uncheckedShape);

        radioButton.setBackground(drawable);
    }

}
