package com.mobiliha.eventsbadesaba.ui.modify;

import android.content.Context;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.util.PersianCalendar;
import com.mobiliha.eventsbadesaba.util.TimeUtils;

import java.util.Calendar;

import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

public class DatePickerDialog {

    public interface OnDateSelectListener {
        void onSelect(Calendar calendar);
    }

    private final Context context;
    private final OnDateSelectListener callback;
    private final Calendar initDate;

    public DatePickerDialog(Context context, Calendar initDate, OnDateSelectListener callback) {
        this.context = context;
        this.callback = callback;
        this.initDate = initDate;
    }

    public void show() {
        PersianCalendar initPersianDate = new PersianCalendar();
        if(initDate != null) {
            initPersianDate = TimeUtils.toPersianCalendar(initDate);
        }

        PersianCalendar now = new PersianCalendar();
        final int minYear = now.get(Calendar.YEAR);
        final int maxYear = now.get(Calendar.YEAR) + 100;

        new PersianDatePickerDialog(context)
                .setPositiveButtonResource(R.string.ok)
                .setNegativeButtonResource(R.string.cancel)
                .setTodayButton(context.getString(R.string.today))
                .setTodayButtonVisible(true)
                .setMinYear(minYear)
                .setMaxYear(maxYear)
                .setInitDate(initPersianDate.getTime())
                .setTitleType(PersianDatePickerDialog.WEEKDAY_DAY_MONTH_YEAR)
                .setShowInBottomSheet(false)
                .setListener(new PersianPickerListener() {
                    @Override
                    public void onDateSelected(PersianPickerDate persianPickerDate) {
                        Calendar calendar = Calendar.getInstance();
                        calendar.setTimeInMillis(persianPickerDate.getTimestamp());
                        calendar.set(Calendar.HOUR_OF_DAY, 0);
                        calendar.set(Calendar.MINUTE, 0);
                        callback.onSelect(calendar);
                    }

                    @Override
                    public void onDismissed() {

                    }
                }).show();
    }


}
