package com.mobiliha.eventsbadesaba.ui.details;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.databinding.FragmentTaskDetailsBinding;
import com.mobiliha.eventsbadesaba.util.AlarmHelper;

public class TaskDetailsFragment extends Fragment {

    private interface OnConfirmListener {
        void onConfirm();
    }

    public static final String TAG = "TaskDetailsFragment";

    private FragmentTaskDetailsBinding binding;
    private TaskDetailsViewModel viewModel;
    private AlarmHelper alarmHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();

        alarmHelper = new AlarmHelper(requireContext());
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        setupUi(inflater, container);

        setupObservers();

        viewModel.fetchTask();

        return binding.getRoot();
    }

    private void setupObservers() {
        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            message.handleIfNotNull(result -> {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            });
        });

        viewModel.getActionCancelAlarm().observe(getViewLifecycleOwner(), task -> {
            task.handleIfNotNull(result -> {
                alarmHelper.cancelAlarm(result);
            });
        });

        viewModel.getActionNavigateBack().observe(getViewLifecycleOwner(), doesNavigate -> {
            doesNavigate.handleIfNotNull(result -> {
                if(result) {
                    NavHostFragment.findNavController(this)
                            .popBackStack();
                }
            });
        });

        viewModel.getActionNavigateToModify().observe(getViewLifecycleOwner(), taskId -> {
            taskId.handleIfNotNull(result -> {
                NavController navController = NavHostFragment.findNavController(this);
                String title = getString(R.string.edit_task);
                navController.navigate(
                        TaskDetailsFragmentDirections.actionDetailsToModify(title)
                                .setTaskId(result)
                );
            });
        });

        viewModel.getActionShareTask().observe(getViewLifecycleOwner(), taskToShare -> {
            taskToShare.handleIfNotNull(result -> {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                String text = getString(R.string.extra_share_text, result.getTitle(), result.getShareLink());
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent = Intent.createChooser(intent, null);
                startActivity(intent);
            });
        });

        viewModel.getActionConfirmTaskDeletion().observe(getViewLifecycleOwner(), task -> {
            task.handleIfNotNull(result -> {
                showConfirmationDialog(() -> viewModel.deleteTask(result));
            });
        });
    }

    private void showConfirmationDialog(OnConfirmListener callback) {
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.delete_task)
                .setMessage(R.string.are_you_sure)
                .setPositiveButton(R.string.yes, (dialog, which) -> {
                    callback.onConfirm();
                })
                .setNegativeButton(R.string.no, null)
                .create()
                .show();
    }

    private void setupUi(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_task_details, container, false
        );
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    private void setupViewModel() {
        int taskId = TaskDetailsFragmentArgs.fromBundle(getArguments()).getTaskId();
        viewModel = new ViewModelProvider(
                this, new TaskDetailsViewModel.Factory(taskId)
        ).get(TaskDetailsViewModel.class);
    }

    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }

}