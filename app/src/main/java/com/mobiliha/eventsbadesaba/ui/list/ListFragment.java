package com.mobiliha.eventsbadesaba.ui.list;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.mobiliha.eventsbadesaba.data.local.db.DbHelper;
import com.mobiliha.eventsbadesaba.data.local.db.dao.TaskDao;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.data.repository.TaskRepository;
import com.mobiliha.eventsbadesaba.databinding.FragmentListBinding;

import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class ListFragment extends Fragment {

    public static final String TAG = "ListFragment";

    private FragmentListBinding binding;
    private ListViewModel viewModel;
    private TaskAdapter adapter;
    private final CompositeDisposable disposables = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupViewModel();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            ViewGroup container,
            Bundle savedInstanceState
    ) {
        setupBinding(inflater, container);
        setupObservables();
        return binding.getRoot();
    }

    private void setupObservables() {
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
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }

    private void setupBinding(LayoutInflater inflater, ViewGroup container) {
        binding = FragmentListBinding.inflate(inflater, container, false);
        adapter = new TaskAdapter();
        binding.recyclerView.setAdapter(adapter);
        binding.setViewModel(viewModel);
    }

    private void setupViewModel() {
        TaskDao taskDao = DbHelper.getInstance(getContext()).getTaskDao();
        TaskRepository repository = new TaskRepository(taskDao);
        viewModel = new ViewModelProvider(
                this,
                new ListViewModel.Factory(repository)
        ).get(ListViewModel.class);
    }

    @Override
    public void onDestroy() {
        disposables.clear();
        super.onDestroy();
    }
}