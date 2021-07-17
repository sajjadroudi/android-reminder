package com.mobiliha.eventsbadesaba.ui.list;

import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;

import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.data.repository.TaskRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class TaskListViewModel extends ViewModel {

    private final TaskRepository repository;
    private final ObservableBoolean isListEmpty = new ObservableBoolean(false);

    public TaskListViewModel() {
        this.repository = new TaskRepository();
    }

    public Single<List<Task>> getTaskList() {
        return repository.getAllTasks()
                .doOnSuccess(tasks -> isListEmpty.set(tasks == null || tasks.isEmpty()));
    }

    public Completable deleteTask(Task task) {
        return repository.delete(task);
    }

    public ObservableBoolean getIsListEmpty() {
        return isListEmpty;
    }

}
