package com.mobiliha.eventsbadesaba.ui.details;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.ReminderApp;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Occasion;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.data.repository.TaskRepository;

import io.reactivex.Completable;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class TaskDetailsViewModel extends ViewModel {

    public static final String TAG = "TaskDetailsViewModel";

    private final TaskRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();
    public final ObservableField<Task> task = new ObservableField<>();
    public final ObservableField<String> taskOccasion = new ObservableField<>();

    public TaskDetailsViewModel(int taskId) {
        repository = new TaskRepository();

        repository.getTask(taskId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Task>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Task task) {
                        TaskDetailsViewModel.this.task.set(task);
                        updateTaskOccasion(task);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }

    public Completable deleteTask(Task task) {
        return repository.delete(task)
                .observeOn(AndroidSchedulers.mainThread());
    }

    private void updateTaskOccasion(Task task) {
        Occasion occasion = task.getOccasion();
        if(occasion != null) {
            int index = occasion.ordinal();
            String occasionStr = ReminderApp.getAppContext()
                    .getResources().getStringArray(R.array.occasions)[index];
            taskOccasion.set(occasionStr);
        }
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final int taskId;

        public Factory(int taskId) {
            this.taskId = taskId;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if(modelClass.isAssignableFrom(TaskDetailsViewModel.class)) {
                return (T) new TaskDetailsViewModel(taskId);
            }
            throw new IllegalArgumentException();
        }
    }
}
