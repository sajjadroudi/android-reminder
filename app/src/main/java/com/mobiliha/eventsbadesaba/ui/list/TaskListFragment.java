package com.mobiliha.eventsbadesaba.ui.list;

import android.os.Bundle;
import android.util.Log;
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

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

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

            viewModel.deleteTask(task)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onComplete() {
                        // TODO: show a snack bar and let the user undo the deletion
                        Toast.makeText(getContext(), R.string.deleted, Toast.LENGTH_SHORT).show();
                        fetchTasks();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: ", e);
                    }
                });
        }
    });

    private FragmentTaskListBinding binding;
    private TaskListViewModel viewModel;
    private TaskAdapter adapter;
    private final CompositeDisposable disposables = new CompositeDisposable();

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

        fetchTasks();

        binding.fab.setOnClickListener(v -> navigateToAddFragment());
        
        return binding.getRoot();
    }

    private void fetchTasks() {
        viewModel.getTaskList()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Task>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<Task> tasks) {
                        adapter.submitList(tasks);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Toast.makeText(getContext(), R.string.something_went_wrong, Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }

    private void setupUi(LayoutInflater inflater, ViewGroup container) {
        binding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_task_list, container,false
        );
        adapter = new TaskAdapter();
        binding.recyclerView.setAdapter(adapter);
        itemTouchHelper.attachToRecyclerView(binding.recyclerView);
        binding.setViewModel(viewModel);
    }

    private void navigateToAddFragment() {
        NavController navController = NavHostFragment.findNavController(this);
        String title = getString(R.string.add_task);
        navController.navigate(
                TaskListFragmentDirections.actionListToModify(title)
        );
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        binding = null;
        super.onDestroy();
    }
}