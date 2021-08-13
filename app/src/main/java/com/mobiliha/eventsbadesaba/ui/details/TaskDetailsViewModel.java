package com.mobiliha.eventsbadesaba.ui.details;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.ReminderApp;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Occasion;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.data.repository.TaskRepository;
import com.mobiliha.eventsbadesaba.ui.core.Event;
import com.mobiliha.eventsbadesaba.util.Utils;

import io.reactivex.CompletableObserver;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class TaskDetailsViewModel extends ViewModel {

    public static final String TAG = "TaskDetailsViewModel";

    private final TaskRepository repository;
    private final CompositeDisposable disposables = new CompositeDisposable();

    private final MutableLiveData<Event<String>> message = new MutableLiveData<>();
    private final MutableLiveData<Event<Task>> actionCancelAlarm = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> actionNavigateBack = new MutableLiveData<>();
    private final MutableLiveData<Event<Integer>> actionNavigateToModify = new MutableLiveData<>();
    private final MutableLiveData<Event<Task>> actionConfirmTaskDeletion = new MutableLiveData<>();
    private final MutableLiveData<Event<Task>> actionShareTask = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>(false);

    private final MutableLiveData<Task> task = new MutableLiveData<>();
    public final LiveData<String> taskOccasion = Transformations.map(task, task -> {
        Occasion occasion = task.getOccasion();
        if(occasion == null)
            return null;

        int index = occasion.ordinal();
        String occasionStr = ReminderApp.getAppContext()
                .getResources().getStringArray(R.array.occasions)[index];
        return occasionStr;
    });

    private final int taskId;

    public TaskDetailsViewModel(int taskId) {
        repository = new TaskRepository();
        this.taskId = taskId;
    }

    public void confirmTaskDeletion() {
        Task task = this.task.getValue();
        if(task == null) return;

        actionConfirmTaskDeletion.postValue(new Event<>(task));
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

                        actionCancelAlarm.postValue(new Event<>(task));

                        actionNavigateBack.postValue(new Event<>(true));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        showMessage(R.string.something_went_wrong);
                    }
                });
    }

    public void navigateToModify() {
        actionNavigateToModify.postValue(new Event<>(taskId));
    }

    public void shareTask() {
        Task task = this.task.getValue();
        if(task == null)
            return;

        if(task.isTokenValid()) {
            actionShareTask.postValue(new Event<>(task));
            return; // Token is still valid so we don't need to send a request to the server.
        }

        showProgressBar.postValue(true);

        repository.saveTaskInServer(task)
                .map(info -> {
                    task.setShareLink(info.getLink());
                    task.setShareId(info.getBaseId());
                    task.setToken(info.getToken());

                    return task;
                })
                .flatMap(info -> repository.update(task))
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> showProgressBar.postValue(false))
                .subscribe(new SingleObserver<Task>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Task task) {
                        TaskDetailsViewModel.this.task.postValue(task);

                        actionShareTask.postValue(new Event<>(task));
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        int resId = Utils.extractMessage(e);
                        showMessage(resId);
                    }
                });
    }

    public void fetchTask() {
        repository.getTask(taskId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Task>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Task task) {
                        TaskDetailsViewModel.this.task.setValue(task);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e(TAG, "onError: ", e);
                    }
                });
    }

    public LiveData<Event<Task>> getActionCancelAlarm() {
        return actionCancelAlarm;
    }

    public LiveData<Event<Boolean>> getActionNavigateBack() {
        return actionNavigateBack;
    }

    public LiveData<Event<String>> getMessage() {
        return message;
    }

    public LiveData<Task> getTask() {
        return task;
    }

    public LiveData<Event<Integer>> getActionNavigateToModify() {
        return actionNavigateToModify;
    }

    public LiveData<Event<Task>> getActionShareTask() {
        return actionShareTask;
    }

    public LiveData<Event<Task>> getActionConfirmTaskDeletion() {
        return actionConfirmTaskDeletion;
    }

    public LiveData<Boolean> getShowProgressBar() {
        return showProgressBar;
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
