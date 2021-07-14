package com.mobiliha.eventsbadesaba.ui.modify;

import android.app.TimePickerDialog;
import android.graphics.Color;
import android.icu.number.Scale;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.data.local.db.DbHelper;
import com.mobiliha.eventsbadesaba.data.local.db.dao.TaskDao;
import com.mobiliha.eventsbadesaba.data.repository.TaskRepository;
import com.mobiliha.eventsbadesaba.databinding.FragmentModifyBinding;
import com.mobiliha.eventsbadesaba.util.PersianCalendar;
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

    private interface OnDateSelected {
        void onSelect(Calendar calendar);
    }

    private interface OnTimeSelected {
        void onSelect(int hour, int minute);
    }

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
                viewModel.setDate(selectedCalendar);
            });
        });

        binding.txtTime.setOnClickListener(v -> {
            showTimePickerDialog((hour, minute) -> {
                viewModel.setTime(hour, minute);
            });
        });

    }

    private void showDatePickerDialog(OnDateSelected callback) {
        PersianCalendar now = new PersianCalendar();
        final int maxYear = now.get(Calendar.YEAR) + 100;

        new PersianDatePickerDialog(getContext())
                .setPositiveButtonResource(android.R.string.ok)
                .setNegativeButtonResource(android.R.string.cancel)
                .setTodayButton(getString(R.string.today))
                .setTodayButtonVisible(true)
                .setMinYear(now.get(Calendar.YEAR))
                .setMaxYear(maxYear)
                .setInitDate(now.getTime())
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

    private void showTimePickerDialog(OnTimeSelected callback) {
        TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
            callback.onSelect(hourOfDay, minute);
        };

        new TimePickerDialog(
                getContext(),
                listener,
                0,
                0,
                true
        ).show();
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