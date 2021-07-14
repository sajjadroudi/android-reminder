package com.mobiliha.eventsbadesaba.ui.modify;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.data.local.db.DbHelper;
import com.mobiliha.eventsbadesaba.data.local.db.dao.TaskDao;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Occasion;
import com.mobiliha.eventsbadesaba.data.repository.TaskRepository;
import com.mobiliha.eventsbadesaba.databinding.FragmentModifyBinding;
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
public class ModifyFragment extends Fragment {

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

    private FragmentModifyBinding binding;
    private ModifyViewModel viewModel;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public ModifyFragment() {
        super(R.layout.fragment_modify);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
    }

    @Override
    public void onViewCreated(
            @NonNull View view,
            @Nullable Bundle savedInstanceState
    ) {
        setupBinding(view);

        binding.btnSave.setOnClickListener((v) -> {
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
        });

        binding.btnCancel.setOnClickListener((v) -> navigateBack());

        binding.txtDate.setOnClickListener(v -> {
            showDatePickerDialog(selectedCalendar -> {
                viewModel.setDateCalendar(selectedCalendar);
            });
        });

        binding.txtTime.setOnClickListener(v -> {
            showTimePickerDialog((hour, minute) -> {
                viewModel.setTime(hour, minute);
            });
        });

        binding.txtOccasion.setOnClickListener(v -> {
            showOccasionDialog(occasion -> {
                viewModel.setOccasion(occasion);
            });
        });

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
                .setPositiveButtonResource(android.R.string.ok)
                .setNegativeButtonResource(android.R.string.cancel)
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
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.occasion)
                .setItems(R.array.occasions, (dialog, which) -> {
                    Occasion occasion = Occasion.values()[which];
                    callback.onSelect(occasion);
                })
                .create()
                .show();
    }

    private void setupBinding(View view) {
        binding = FragmentModifyBinding.bind(view);
        binding.setViewModel(viewModel);
    }

    private void setupViewModel() {
        TaskDao dao = DbHelper.getInstance(getContext()).getTaskDao();
        TaskRepository repository = new TaskRepository(dao);
        viewModel = new ViewModelProvider(
                this,
                new ModifyViewModel.Factory(repository)
        ).get(ModifyViewModel.class);
    }

    private void navigateBack() {
        NavHostFragment
            .findNavController(this)
            .popBackStack();
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }
}