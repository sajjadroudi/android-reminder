package com.mobiliha.eventsbadesaba.ui.modify;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.databinding.ObservableArrayList;
import androidx.databinding.ObservableField;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.ReminderApp;
import com.mobiliha.eventsbadesaba.ui.core.Event;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Occasion;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.data.local.db.entity.TaskColor;
import com.mobiliha.eventsbadesaba.data.repository.TaskRepository;
import com.mobiliha.eventsbadesaba.util.Utils;

import java.util.Calendar;
import java.util.Objects;

import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class TaskModifyViewModel extends ViewModel {

    public static final int NOT_DEFINED = -1;
    public static final String TAG = "ModifyViewModel";

    public final ObservableField<Task> task = new ObservableField<>(new Task());
    // Visible fields are additional fields that user has been selected to input more information
    public final ObservableArrayList<AdditionalField> visibleFields = new ObservableArrayList<>();

    private final MutableLiveData<Event<String>> message = new MutableLiveData<>();
    private final MutableLiveData<Event<Task>> actionSetAlarm = new MutableLiveData<>();
    private final MutableLiveData<Event<Task>> actionUpdateAlarm = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> actionNavigateBack = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> actionShowDueDateDialog = new MutableLiveData<>();
    private final MutableLiveData<Event<Boolean>> actionShowOccasionDialog = new MutableLiveData<>();
    private final MutableLiveData<Boolean> showProgressBar = new MutableLiveData<>(false);

    private final TaskRepository repository;
    private final Mode mode;
    private final CompositeDisposable disposables = new CompositeDisposable();

    public TaskModifyViewModel(int taskId) {
        this.repository = new TaskRepository();
        mode = (taskId == NOT_DEFINED) ? Mode.INSERT : Mode.UPDATE;

        // Set current time as due date
        task.get().setDueDate(Calendar.getInstance());
        task.notifyChange();

        if(taskId != NOT_DEFINED) {
            repository.getTask(taskId)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new SingleObserver<Task>() {
                        @Override
                        public void onSubscribe(@NonNull Disposable d) {
                            disposables.add(d);
                        }

                        @Override
                        public void onSuccess(@NonNull Task task) {
                            if(task.getDetails() != null)
                                addToVisibleAdditionalFields(AdditionalField.DESC);

                            if(task.getLocation() != null)
                                addToVisibleAdditionalFields(AdditionalField.LOCATION);

                            if(task.getLink() != null)
                                addToVisibleAdditionalFields(AdditionalField.LINK);

                            TaskModifyViewModel.this.task.set(task);
                        }

                        @Override
                        public void onError(@NonNull Throwable e) {

                        }
                    });
        }
    }

    public TaskModifyViewModel(String token) {
        this.repository = new TaskRepository();
        mode = Mode.INSERT;

        showProgressBar.postValue(true);

        repository.getTaskFromServer(token)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnTerminate(() -> showProgressBar.postValue(false))
                .subscribe(new SingleObserver<Task>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Task task) {
                        setupAdditionalVisibleFields(task);

                        TaskModifyViewModel.this.task.set(task);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        showMessage(Utils.extractMessage(e));

                        navigateBack();
                    }
                });
    }

    public TaskModifyViewModel() {
        this(NOT_DEFINED);
    }

    public void addToVisibleAdditionalFields(AdditionalField field) {
        if(!visibleFields.contains(field))
            visibleFields.add(field);
    }

    /**
     * Set year, month and day.
     * @param value Calendar contains desired year, month and day.
     */
    public void setDateCalendar(Calendar value) {
        Calendar calendar = task.get().getDueDate();

        calendar.set(Calendar.YEAR, value.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, value.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, value.get(Calendar.DAY_OF_MONTH));

        task.get().setDueDate(calendar);
        task.notifyChange();
    }

    /**
     * Set hour and minute.
     * @param hour Hour.
     * @param minute Minute.
     */
    public void setTime(int hour, int minute) {
        Calendar calendar = task.get().getDueDate();

        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);

        task.get().setDueDate(calendar);
        task.notifyChange();
    }

    public void saveTask() {
        trimTitle();

        Task task = Objects.requireNonNull(this.task.get());

        try {
            validateTask(task);
        } catch (IllegalArgumentException e) {
            showMessage(e.getMessage());
            return;
        }

        task.invalidateToken();

        Single<Task> single = (mode == Mode.INSERT) ? repository.insert(task)
                : repository.update(task);

        single
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<Task>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    disposables.add(d);
                }

                @Override
                public void onSuccess(@NonNull Task task) {
                    showMessage(R.string.saved);

                    if(mode == Mode.INSERT) {
                        actionSetAlarm.postValue(new Event<>(task));
                    } else {
                        actionUpdateAlarm.postValue(new Event<>(task));
                    }

                    navigateBack();
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    showMessage(Utils.extractMessage(e));
                }
            });
    }

    public void navigateBack() {
        actionNavigateBack.postValue(new Event<>(true));
    }

    public void showDueDateDialog() {
        actionShowDueDateDialog.postValue(new Event<>(true));
    }

    public void showOccasionDialog() {
        actionShowOccasionDialog.postValue(new Event<>(true));
    }

    public void setOccasion(Occasion occasion) {
        task.get().setOccasion(occasion);
        task.notifyChange();
    }

    public Occasion getOccasion() {
        return task.get().getOccasion();
    }

    public Calendar getDueDate() {
        return task.get().getDueDate();
    }

    public void setTaskColor(TaskColor color) {
        task.get().setColor(color);
        task.notifyChange();
    }

    public LiveData<Event<String>> getMessage() {
        return message;
    }

    public LiveData<Event<Boolean>> getActionShowDueDateDialog() {
        return actionShowDueDateDialog;
    }

    public LiveData<Event<Boolean>> getActionShowOccasionDialog() {
        return actionShowOccasionDialog;
    }

    public LiveData<Event<Boolean>> getActionNavigateBack() {
        return actionNavigateBack;
    }

    public LiveData<Event<Task>> getActionSetAlarm() {
        return actionSetAlarm;
    }

    public LiveData<Event<Task>> getActionUpdateAlarm() {
        return actionUpdateAlarm;
    }

    public LiveData<Boolean> getShowProgressBar() {
        return showProgressBar;
    }

    private void validateTask(Task task) {
        String title = task.getTitle();

        if(title == null || title.length() < Task.MIN_TITLE_LENGTH) {
            String message = ReminderApp.getAppContext().getString(
                    R.string.short_title, Task.MIN_TITLE_LENGTH
            );
            throw new IllegalArgumentException(message);
        }

        if(title.length() > Task.MAX_TITLE_LENGTH) {
            String message = ReminderApp.getAppContext().getString(
                    R.string.long_title, Task.MAX_TITLE_LENGTH
            );
            throw new IllegalArgumentException(message);
        }

        if(task.getDueDate() == null) {
            String message = ReminderApp.getAppContext().getString(
                    R.string.empty_due_date
            );
            throw new IllegalArgumentException(message);
        }
    }

    private void trimTitle() {
        Task task = Objects.requireNonNull(this.task.get());
        String newTitle = Utils.tryTrim(task.getTitle());
        task.setTitle(newTitle);
        this.task.notifyChange();
    }

    private void showMessage(String message) {
        if(message != null)
            this.message.postValue(new Event<>(message));
    }

    private void showMessage(@StringRes int resId) {
        String message = ReminderApp.getAppContext().getString(resId);
        showMessage(message);
    }

    private void setupAdditionalVisibleFields(Task task) {
        if(task.getDetails() != null)
            addToVisibleAdditionalFields(AdditionalField.DESC);

        if(task.getLocation() != null)
            addToVisibleAdditionalFields(AdditionalField.LOCATION);

        if(task.getLink() != null)
            addToVisibleAdditionalFields(AdditionalField.LINK);
    }

    @Override
    protected void onCleared() {
        disposables.clear();
        super.onCleared();
    }

    public static class Factory implements ViewModelProvider.Factory {

        private int taskId = NOT_DEFINED;
        private String token;

        public Factory(int taskId) {
            this.taskId = taskId;
        }

        public Factory(String token) {
            this.token = token;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if(modelClass.isAssignableFrom(TaskModifyViewModel.class))
                if(token != null)
                    return (T) new TaskModifyViewModel(token);
                else
                    return (T) new TaskModifyViewModel(taskId);
            throw new IllegalArgumentException();
        }
    }

}
