package com.mobiliha.eventsbadesaba.ui.list;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.ReminderApp;
import com.mobiliha.eventsbadesaba.ui.core.Event;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.data.repository.TaskRepository;

import java.util.List;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class TaskListViewModel extends ViewModel {

    private final TaskRepository repository;

    private final MutableLiveData<List<Task>> taskList = new MutableLiveData<>();
    public final LiveData<Boolean> isListEmpty = Transformations.map(
            taskList, tasks -> tasks == null || tasks.isEmpty()
    );
    private final MutableLiveData<Event<String>> message = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> actionNavigateToAddTask = new MutableLiveData<>();
    private final MutableLiveData<Event<Task>> actionNavigateToTaskDetails = new MutableLiveData<>();

    private final CompositeDisposable disposables = new CompositeDisposable();

    public TaskListViewModel() {
        this.repository = new TaskRepository();
    }

    public void fetchTaskList() {
        repository.getAllTasks()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<List<Task>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull List<Task> tasks) {
                        taskList.postValue(tasks);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        showMessage(R.string.something_went_wrong);
                    }
                });
    }

    public void deleteTask(Task task) {
        repository.delete(task)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onComplete() {
                        showMessage(R.string.deleted);
                        fetchTaskList();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        showMessage(R.string.something_went_wrong);
                    }
                });
    }

    public void navigateToAddTask() {
        actionNavigateToAddTask.postValue(new Event<>(true));
    }

    public void navigateToTaskDetails(Task task) {
        actionNavigateToTaskDetails.postValue(new Event<>(task));
    }

    public LiveData<List<Task>> getTaskList() {
        return taskList;
    }

    public LiveData<Event<String>> getMessage() {
        return message;
    }

    public LiveData<Event<Boolean>> getActionNavigateToAddTask() {
        return actionNavigateToAddTask;
    }

    public LiveData<Event<Task>> getActionNavigateToTaskDetails() {
        return actionNavigateToTaskDetails;
    }

    private void showMessage(@StringRes int resId) {
        String message = ReminderApp.getAppContext().getString(resId);
        this.message.postValue(new Event<>(message));
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }

}
