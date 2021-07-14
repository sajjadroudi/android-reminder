package com.mobiliha.eventsbadesaba.ui.modify;

import androidx.annotation.NonNull;
import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.ReminderApp;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.data.repository.TaskRepository;
import com.mobiliha.eventsbadesaba.util.UserInputException;
import com.mobiliha.eventsbadesaba.util.Utils;

import java.util.Calendar;

import io.reactivex.Completable;

public class ModifyViewModel extends ViewModel {

    public static final String TAG = "ModifyViewModel";

    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<String> occasion = new ObservableField<>();
    public final ObservableField<Calendar> date = new ObservableField<>();
    public final ObservableField<Calendar> time = new ObservableField<>();
    public final ObservableField<String> details = new ObservableField<>();
    public final ObservableField<String> location = new ObservableField<>();
    public final ObservableField<String> link = new ObservableField<>();
    public final ObservableField<String> desc = new ObservableField<>();

    private final TaskRepository repository;

    public ModifyViewModel(TaskRepository repository) {
        this.repository = repository;
    }

    /**
     * Set year, month and day.
     * @param value Calendar contains desired year, month and day.
     */
    public void setDate(Calendar value) {
        Calendar calendar = date.get();
        if(calendar == null)
            calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, value.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, value.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, value.get(Calendar.DAY_OF_MONTH));
        date.set(calendar);
    }

    /**
     * Set hour and minute.
     * @param hour Hour.
     * @param minute Minute.
     */
    public void setTime(int hour, int minute) {
        Calendar calendar = time.get();
        if(calendar == null)
            calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        time.set(calendar);
    }

    public Completable saveTask() {
        String title = Utils.tryTrim(this.title.get());

        if(title == null) {
            return Completable.create(emitter -> {
                String message = ReminderApp.getAppContext().getString(R.string.invalid_title);
                emitter.onError(new UserInputException(message));
            });
        }


        Task task = new Task(
                title,
                buildDueDate(date.get(), time.get()),
                Utils.tryTrim(occasion.get()),
                Utils.tryTrim(details.get()),
                Utils.tryTrim(location.get()),
                Utils.tryTrim(link.get())
        );

        return repository.insert(task);
    }

    private Calendar buildDueDate(Calendar date, Calendar time) {
        if(date == null || time == null)
            return null;

        Calendar dueDate = Calendar.getInstance();

        dueDate.set(Calendar.YEAR, date.get(Calendar.YEAR));
        dueDate.set(Calendar.MONTH, date.get(Calendar.MONTH));
        dueDate.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH));

        dueDate.set(Calendar.HOUR_OF_DAY, time.get(Calendar.HOUR_OF_DAY));
        dueDate.set(Calendar.MINUTE, time.get(Calendar.MINUTE));

        return dueDate;
    }

    public static class Factory implements ViewModelProvider.Factory {

        private final TaskRepository repository;

        public Factory(TaskRepository repository) {
            this.repository = repository;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            if(modelClass.isAssignableFrom(ModifyViewModel.class)) {
                return (T) new ModifyViewModel(repository);
            }
            throw new IllegalArgumentException();
        }
    }

}