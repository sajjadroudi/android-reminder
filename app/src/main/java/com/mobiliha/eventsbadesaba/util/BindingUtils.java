package com.mobiliha.eventsbadesaba.util;

import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

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

}
