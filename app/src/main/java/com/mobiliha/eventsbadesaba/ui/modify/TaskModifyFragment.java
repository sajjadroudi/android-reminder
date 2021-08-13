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
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Occasion;
import com.mobiliha.eventsbadesaba.data.local.db.entity.TaskColor;
import com.mobiliha.eventsbadesaba.databinding.FragmentTaskModifyBinding;
import com.mobiliha.eventsbadesaba.util.AlarmHelper;

import java.util.Calendar;

/**
 * A fragment to add and edit task.
 */
public class TaskModifyFragment extends Fragment {

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
    private TaskModifyFragmentArgs args;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        args = TaskModifyFragmentArgs.fromBundle(getArguments());

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

        setupObservers();

        // A task from outside has been shared
        if(args.getToken() != null) {
            ((AppCompatActivity) requireActivity()).getSupportActionBar().setTitle(R.string.add_task);
        }

        binding.coloredRadioButtonGroup.setOnCheckedChangeListener((group, checkedId) -> {
            TaskColor taskColor = (TaskColor) group.findViewById(checkedId).getTag();
            viewModel.setTaskColor(taskColor);
        });

        return binding.getRoot();
    }

    private void setupObservers() {
        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            message.handleIfNotNull(result -> {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            });
        });

        viewModel.getActionShowDueDateDialog().observe(getViewLifecycleOwner(), show -> {
            show.handleIfNotNull(result -> {
                if(result) {
                    showDatePickerDialog(selectedCalendar -> {
                        viewModel.setDateCalendar(selectedCalendar);

                        showTimePickerDialog((hour, minute) -> {
                            viewModel.setTime(hour, minute);
                        });
                    });
                }
            });
        });

        viewModel.getActionShowOccasionDialog().observe(getViewLifecycleOwner(), callback -> {
            callback.handleIfNotNull(result -> {
                if(result) {
                    showOccasionDialog(selectedOccasion -> {
                        viewModel.setOccasion(selectedOccasion);
                    });
                }
            });
        });

        viewModel.getActionSetAlarm().observe(getViewLifecycleOwner(), task -> {
            task.handleIfNotNull(result -> {
                alarmHelper.setAlarm(result);
            });
        });

        viewModel.getActionUpdateAlarm().observe(getViewLifecycleOwner(), task -> {
            task.handleIfNotNull(result -> {
                alarmHelper.updateAlarm(result);
            });
        });

        viewModel.getActionNavigateBack().observe(getViewLifecycleOwner(), doesNavigate -> {
            doesNavigate.handleIfNotNull(result -> {
                if(result) {
                    NavHostFragment
                            .findNavController(this)
                            .popBackStack();
                }
            });
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
        TaskModifyViewModel.Factory factory;
        String token = args.getToken();
        if(token != null) {
            factory = new TaskModifyViewModel.Factory(token);
        } else {
            int taskId = args.getTaskId();
            factory = new TaskModifyViewModel.Factory(taskId);
        }

        viewModel = new ViewModelProvider(
                this,
                factory
        ).get(TaskModifyViewModel.class);
    }

    private void setupUi(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_task_modify, container, false
        );
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}