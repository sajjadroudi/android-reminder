package com.mobiliha.eventsbadesaba.ui.modify;

import androidx.databinding.ObservableField;
import androidx.lifecycle.ViewModel;

import com.mobiliha.eventsbadesaba.R;
import com.mobiliha.eventsbadesaba.ReminderApp;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Occasion;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.data.local.db.entity.TaskColor;
import com.mobiliha.eventsbadesaba.data.repository.TaskRepository;
import com.mobiliha.eventsbadesaba.util.UserInputException;
import com.mobiliha.eventsbadesaba.util.Utils;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import io.reactivex.Single;

public class TaskModifyViewModel extends ViewModel {

    public static final String TAG = "ModifyViewModel";

    public final ObservableField<String> title = new ObservableField<>();
    public final ObservableField<Occasion> occasion = new ObservableField<>();
    public final ObservableField<Calendar> date = new ObservableField<>(Calendar.getInstance());
    public final ObservableField<Calendar> time = new ObservableField<>(Calendar.getInstance());
    public final ObservableField<String> details = new ObservableField<>();
    public final ObservableField<String> location = new ObservableField<>();
    public final ObservableField<String> desc = new ObservableField<>();
    public final ObservableField<String> link = new ObservableField<>();
    public final ObservableField<TaskColor> color = new ObservableField<>(TaskColor.BLUE);

    private final TaskRepository repository;

    // Visible additional fields are those that user has been selected to input data
    private final Set<AdditionalField> visibleAdditionalFields;

    public TaskModifyViewModel() {
        this.repository = new TaskRepository();
        visibleAdditionalFields = new HashSet<>();
    }

    public void addToVisibleAdditionalFields(AdditionalField field) {
        visibleAdditionalFields.add(field);
    }

    public AdditionalField[] getVisibleAdditionalFields() {
        return visibleAdditionalFields.toArray(new AdditionalField[0]);
    }

    /**
     * Set year, month and day.
     * @param value Calendar contains desired year, month and day.
     */
    public void setDateCalendar(Calendar value) {
        Calendar calendar = Calendar.getInstance();
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
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        time.set(calendar);
    }

    public Single<Task> saveTask() {
        String title = Utils.tryTrim(this.title.get());

        if(title == null || title.length() < Task.MIN_TITLE_LENGTH) {
            return Single.create(emitter -> {
                String message = ReminderApp.getAppContext().getString(
                        R.string.short_title, Task.MIN_TITLE_LENGTH
                );
                emitter.onError(new UserInputException(message));
            });
        }

        if(title.length() > Task.MAX_TITLE_LENGTH) {
            return Single.create(emitter -> {
                String message = ReminderApp.getAppContext().getString(
                        R.string.long_title, Task.MAX_TITLE_LENGTH
                );
                emitter.onError(new UserInputException(message));
            });
        }

        Task task = new Task(
                title,
                buildDueDate(date.get(), time.get()),
                occasion.get(),
                Utils.tryTrim(details.get()),
                Utils.tryTrim(location.get()),
                Utils.tryTrim(link.get()),
                color.get()
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

    public void setOccasion(Occasion occasion) {
        this.occasion.set(occasion);
    }

    public Calendar getTimeCalendar() {
        return time.get();
    }

    public Calendar getDateCalendar() {
        return date.get();
    }

    public void setTaskColor(TaskColor color) {
        this.color.set(color);
    }

}
