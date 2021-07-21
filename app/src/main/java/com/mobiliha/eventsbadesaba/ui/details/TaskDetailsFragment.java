package com.mobiliha.eventsbadesaba.ui.details;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.databinding.FragmentTaskDetailsBinding;
import com.mobiliha.eventsbadesaba.ui.modify.TaskModifyFragment;
import com.mobiliha.eventsbadesaba.ui.modify.TaskModifyFragmentArgs;

import io.reactivex.CompletableObserver;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class TaskDetailsFragment extends Fragment implements View.OnClickListener {

    public static final String TAG = "TaskDetailsFragment";

    private FragmentTaskDetailsBinding binding;
    private TaskDetailsViewModel viewModel;
    private CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        setupUi(inflater, container);

        setupClickListener();

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        Task task = viewModel.task.get();

        if(task == null)
            return;

        switch (v.getId()) {
            case R.id.btn_delete:
                deleteTask(task);
                break;
            case R.id.btn_share:
                // TODO
                Toast.makeText(getContext(), "Not implemented yet!", Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_edit:
                navigateToModify(task.getTaskId());
                break;
        }
    }

    private void deleteTask(Task task) {
        // TODO : incomplete
        viewModel.deleteTask(task)
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onComplete() {
                        // TODO: Show a snack bar and let the user undo the deletion
                        Toast.makeText(getContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
                        navigateBack();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }

    private void navigateToModify(int taskId) {
        NavController navController = NavHostFragment.findNavController(this);
        String title = getString(R.string.edit_task);
        navController.navigate(
                TaskDetailsFragmentDirections.actionDetailsToModify(title)
                    .setTaskId(taskId)
        );
    }

    private void navigateBack() {
        NavHostFragment.findNavController(this)
                .popBackStack();
    }

    private void setupUi(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_task_details, container, false
        );
        binding.setViewModel(viewModel);
    }

    private void setupViewModel() {
        int taskId = TaskDetailsFragmentArgs.fromBundle(getArguments()).getTaskId();
        viewModel = new ViewModelProvider(
                this, new TaskDetailsViewModel.Factory(taskId)
        ).get(TaskDetailsViewModel.class);
    }

    private void setupClickListener() {
        View[] views = {
                binding.btnShare,
                binding.btnEdit,
                binding.btnDelete
        };

        for(View view : views)
            view.setOnClickListener(this);
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        binding = null;
        super.onDestroy();
    }

}