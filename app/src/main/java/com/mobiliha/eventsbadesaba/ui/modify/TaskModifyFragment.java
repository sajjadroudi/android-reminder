package com.mobiliha.eventsbadesaba.ui.modify;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Occasion;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.data.local.db.entity.TaskColor;
import com.mobiliha.eventsbadesaba.databinding.FragmentTaskModifyBinding;
import com.mobiliha.eventsbadesaba.util.AlarmHelper;
import com.mobiliha.eventsbadesaba.util.UserInputException;

import java.util.Calendar;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * A fragment to add and edit task.
 */
public class TaskModifyFragment extends Fragment implements View.OnClickListener {

    private interface OnTimeSelectListener {
        void onSelect(int hour, int minute);
    }

    private interface OnOccasionSelectListener {
        void onSelect(Occasion occasion);
    }

    public static final String TAG = "ModifyFragment";

    private FragmentTaskModifyBinding binding;
    private TaskModifyViewModel viewModel;
    private AlarmHelper alarmHelper;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();

        alarmHelper = new AlarmHelper(requireContext());
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

        binding.coloredCirclesGroup.setOnCheckedChangeListener((group, checkedId) -> {
            TaskColor taskColor = (TaskColor) group.findViewById(checkedId).getTag();
            viewModel.setTaskColor(taskColor);
        });

        return binding.getRoot();
    }

    private void setupClickListener() {
        View[] views = {
                binding.btnSave,
                binding.btnCancel,
                binding.txtDateTime,
                binding.txtOccasion,
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
                showOccasionDialog(selectedOccasion -> {
                    viewModel.setOccasion(selectedOccasion);
                });
                break;

            case R.id.txt_location:
            case R.id.txt_desc:
            case R.id.txt_link:
                AdditionalField field = (AdditionalField) v.getTag();
                viewModel.addToVisibleAdditionalFields(field);
                break;

        }
    }

    private void saveTask() {
        viewModel.saveTask()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Task>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Task task) {
                        Toast.makeText(getContext(), R.string.saved, Toast.LENGTH_SHORT).show();

                        if(viewModel.getMode() == Mode.INSERT) {
                            alarmHelper.setAlarm(task);
                        } else {
                            alarmHelper.updateAlarm(task);
                        }

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

    private void showDatePickerDialog(DatePickerDialog.OnDateSelectListener callback) {
        new DatePickerDialog(getContext(), viewModel.getDueDate(), callback)
                .show();
    }

    private void showTimePickerDialog(OnTimeSelectListener callback) {
        TimePickerDialog.OnTimeSetListener listener = (view, hourOfDay, minute) -> {
            callback.onSelect(hourOfDay, minute);
        };

        Calendar initTime = viewModel.getDueDate();
        int hour = 0, minute = 0;
        if(initTime != null) {
            hour = initTime.get(Calendar.HOUR_OF_DAY);
            minute = initTime.get(Calendar.MINUTE);
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
        Occasion currentOccasion = viewModel.getOccasion();
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

    private void setupViewModel() {
        int taskId = TaskModifyFragmentArgs.fromBundle(getArguments()).getTaskId();
        viewModel = new ViewModelProvider(
                this,
                new TaskModifyViewModel.Factory(taskId)
        ).get(TaskModifyViewModel.class);
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