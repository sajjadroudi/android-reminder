package com.mobiliha.eventsbadesaba.ui.modify;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Occasion;
import com.mobiliha.eventsbadesaba.data.local.db.entity.TaskColor;
import com.mobiliha.eventsbadesaba.databinding.FragmentTaskModifyBinding;
import com.mobiliha.eventsbadesaba.ui.custom.ColoredCircle;
import com.mobiliha.eventsbadesaba.ui.custom.CustomEditText;
import com.mobiliha.eventsbadesaba.util.PersianCalendar;
import com.mobiliha.eventsbadesaba.util.TimeUtils;
import com.mobiliha.eventsbadesaba.util.UserInputException;

import java.util.Calendar;

import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import ir.hamsaa.persiandatepicker.PersianDatePickerDialog;
import ir.hamsaa.persiandatepicker.api.PersianPickerDate;
import ir.hamsaa.persiandatepicker.api.PersianPickerListener;

/**
 * A fragment to add and edit task.
 */
public class TaskModifyFragment extends Fragment implements View.OnClickListener {

    private interface OnDateSelectListener {
        void onSelect(Calendar calendar);
    }

    private interface OnTimeSelectListener {
        void onSelect(int hour, int minute);
    }

    private interface OnOccasionSelectListener {
        void onSelect(Occasion occasion);
    }

    public static final String TAG = "ModifyFragment";

    private FragmentTaskModifyBinding binding;
    private TaskModifyViewModel viewModel;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TaskModifyViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState
    ) {
        setupUi(inflater, container);

        setupClickListener();

        setupAdditionalFields();

        return binding.getRoot();
    }

    private void setupClickListener() {
        View[] views = {
                binding.btnSave,
                binding.btnCancel,
                binding.txtDateTime,
                binding.txtOccasion,
                binding.blueCircle,
                binding.greenCircle,
                binding.redCircle,
                binding.yellowCircle,
                binding.txtLocation,
                binding.txtLink,
                binding.txtDesc
        };

        for(View view : views)
            view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_save:
                saveTask();
                break;
            case R.id.btn_cancel:
                navigateBack();
                break;
            case R.id.txt_date_time:
                showDatePickerDialog(selectedCalendar -> {
                    viewModel.setDateCalendar(selectedCalendar);

                    showTimePickerDialog((hour, minute) -> {
                        viewModel.setTime(hour, minute);
                    });
                });
                break;
            case R.id.txt_occasion:
                showOccasionDialog(occasion -> {
                    viewModel.setOccasion(occasion);
                });
                break;

            case R.id.blue_circle:
            case R.id.green_circle:
            case R.id.yellow_circle:
            case R.id.red_circle:
                int color = ((ColoredCircle) v).getCircleColor();
                TaskColor taskColor = TaskColor.getAssociatedTaskColor(color);
                viewModel.setTaskColor(taskColor);
                break;

            case R.id.txt_location:
            case R.id.txt_desc:
            case R.id.txt_link:
                AdditionalField field = (AdditionalField) v.getTag();
                viewModel.addToVisibleAdditionalFields(field);
                makeVisible(field);
                break;

        }
    }

    private void saveTask() {
        viewModel.saveTask()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onComplete() {
                        Toast.makeText(getContext(), R.string.saved, Toast.LENGTH_SHORT).show();
                        navigateBack();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        String message;
                        if(e instanceof UserInputException) {
                            message = e.getMessage();
                        } else {
                            message = getString(R.string.something_went_wrong);
                        }
                        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private CustomEditText getAssociatedEditText(AdditionalField field) {
        switch (field) {
            case LINK:
                return binding.edtLink;
            case LOCATION:
                return binding.edtLocation;
            case DESC:
                return binding.edtDesc;
        }
        throw new IllegalArgumentException();
    }

    private TextView getAssociatedTextView(AdditionalField field) {
        switch (field) {
            case LINK:
                return binding.txtLink;
            case LOCATION:
                return binding.txtLocation;
            case DESC:
                return binding.txtDesc;
        }
        throw new IllegalArgumentException();
    }

    private void setupAdditionalFields() {
        AdditionalField[] fields = viewModel.getVisibleAdditionalFields();
        for (AdditionalField field : fields) {
            makeVisible(field);
        }
    }

    private void makeVisible(AdditionalField field) {
        TextView textView = getAssociatedTextView(field);
        textView.setVisibility(View.GONE);

        CustomEditText editText = getAssociatedEditText(field);
        editText.setVisibility(View.VISIBLE);

        if(haveAllAdditionalFieldsBeenVisible())
            binding.additionalFieldsTextViewContainer.setVisibility(View.GONE);
    }

    private boolean haveAllAdditionalFieldsBeenVisible() {
        return viewModel.getVisibleAdditionalFields().length
                == AdditionalField.values().length;
    }

    private void showDatePickerDialog(OnDateSelectListener callback) {
        Calendar date = viewModel.getDateCalendar();
        PersianCalendar calendar = new PersianCalendar();
        if(date != null) {
            calendar = TimeUtils.toPersianCalendar(date);
        }

        PersianCalendar now = new PersianCalendar();
        final int minYear = now.get(Calendar.YEAR);
        final int maxYear = now.get(Calendar.YEAR) + 100;

        new PersianDatePickerDialog(getContext())
                .setPositiveButtonResource(R.string.ok)
                .setNegativeButtonResource(R.string.cancel)
                .setTodayButton(getString(R.string.today))
                .setTodayButtonVisible(true)
                .setMinYear(minYear)
                .setMaxYear(maxYear)
                .setInitDate(calendar.getTime())
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

    private void showTimePickerDialog(OnTimeSelectListener callback) {
        TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
            callback.onSelect(hourOfDay, minute);
        };

        Calendar time = viewModel.getTimeCalendar();
        int hour = 0, minute = 0;
        if(time != null) {
            hour = time.get(Calendar.HOUR_OF_DAY);
            minute = time.get(Calendar.MINUTE);
        }

        new TimePickerDialog(
                getContext(),
                listener,
                hour,
                minute,
                true
        ).show();
    }

    private void showOccasionDialog(OnOccasionSelectListener callback) {
        Occasion currentOccasion = viewModel.occasion.get();
        int index = (currentOccasion == null) ? -1 : currentOccasion.ordinal();

        new AlertDialog.Builder(getContext(), R.style.RightJustifyTheme)
                .setTitle(R.string.occasion)
                .setSingleChoiceItems(R.array.occasions, index, ((dialog, which) -> {
                    Occasion occasion = Occasion.values()[which];
                    callback.onSelect(occasion);
                    dialog.dismiss();
                }))
                .create()
                .show();
    }

    private void setupUi(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_task_modify, container, false
        );
        binding.setViewModel(viewModel);
    }

    private void navigateBack() {
        NavHostFragment
            .findNavController(this)
            .popBackStack();
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        binding = null;
        super.onDestroy();
    }
}