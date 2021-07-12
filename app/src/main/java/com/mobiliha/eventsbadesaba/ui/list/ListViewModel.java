package com.mobiliha.eventsbadesaba.ui.list;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableBoolean;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.data.repository.TaskRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;

public class ListViewModel extends ViewModel {

    private final TaskRepository repository;
    private final ObservableBoolean isListEmpty = new ObservableBoolean(false);

    public ListViewModel(TaskRepository repository) {
        this.repository = repository;
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

    public static class Factory implements ViewModelProvider.Factory {

        private final TaskRepository repository;

        public Factory(TaskRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if(modelClass.isAssignableFrom(ListViewModel.class)) {
                return (T) new ListViewModel(repository);
            }
            throw new IllegalArgumentException();
        }
    }

}
