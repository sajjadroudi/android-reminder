package com.mobiliha.eventsbadesaba.ui.list;

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
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.databinding.FragmentTaskListBinding;

public class TaskListFragment extends Fragment {

    public static final String TAG = "ListFragment";

    private final ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT
    ) {
        @Override
        public boolean onMove(
            @NonNull RecyclerView recyclerView,
            @NonNull RecyclerView.ViewHolder viewHolder,
            @NonNull RecyclerView.ViewHolder target
        ) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Task task = adapter.getCurrentList().get(position);
            viewModel.deleteTask(task);
        }
    });

    private FragmentTaskListBinding binding;
    private TaskListViewModel viewModel;
    private TaskAdapter adapter = new TaskAdapter(task -> {
        viewModel.navigateToTaskDetails(task);
    });

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(TaskListViewModel.class);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        setupUi(inflater, container);

        setupObservers();

        viewModel.fetchTaskList();

        return binding.getRoot();
    }

    private void setupObservers() {
        viewModel.getTaskList().observe(getViewLifecycleOwner(), tasks -> {
            adapter.submitList(tasks);
        });

        viewModel.getMessage().observe(getViewLifecycleOwner(), message -> {
            message.handleIfNotNull(result -> {
                Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            });
        });

        viewModel.getActionNavigateToAddTask().observe(getViewLifecycleOwner(), doesNavigate -> {
            doesNavigate.handleIfNotNull(result -> {
                NavController navController = NavHostFragment.findNavController(this);
                String title = getString(R.string.add_task);
                navController.navigate(
                        TaskListFragmentDirections.actionListToModify()
                            .setTitle(title)
                );
            });
        });

        viewModel.getActionNavigateToTaskDetails().observe(getViewLifecycleOwner(), task -> {
            task.handleIfNotNull(result -> {
                NavController navController = NavHostFragment.findNavController(TaskListFragment.this);
                navController.navigate(
                        TaskListFragmentDirections.actionListToDetails(result.getTaskId())
                );
            });
        });
    }

    private void setupUi(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_task_list, container,false
        );
        binding.recyclerView.setAdapter(adapter);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(getViewLifecycleOwner());
    }

    @Override
    public void onDestroy() {
        binding = null;
        super.onDestroy();
    }
}