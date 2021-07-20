package com.mobiliha.eventsbadesaba.data.local.db.entity;

import androidx.annotation.NonNull;

import com.mobiliha.eventsbadesaba.data.local.db.Converter;
import com.mobiliha.eventsbadesaba.util.Utils;

import java.util.Calendar;

public class Task {

    public static final int MAX_TITLE_LENGTH = 40;
    public static final int MIN_TITLE_LENGTH = 3;
    private static final int DEF_TASK_ID = 0;

    private final int taskId;
    private String title;
    private Calendar dueDate;
    private Occasion occasion;
    private String details;
    private String location;
    private String link;
    private TaskColor color = TaskColor.BLUE;

    public Task(
            int taskId,
            @NonNull String title,
            long dueDate,
            String occasion,
            String details,
            String location,
            String link,
            String color
    ) {
        this.taskId = taskId;
        setTitle(title);
        setDueDate(dueDate);
        setOccasion(occasion);
        setDetails(details);
        setLocation(location);
        setLink(link);
        setColor(color);
    }

    public Task(
            @NonNull String title,
            Calendar dueDate,
            Occasion occasion,
            String details,
            String location,
            String link,
            TaskColor color
    ) {
        this(DEF_TASK_ID, title,
                dueDate == null ? 0 : dueDate.getTimeInMillis(),
                occasion == null ? null : occasion.toString(),
                details, location, link,
                color == null ? null : color.toString());
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public void setDueDate(long dueDate) {
        setDueDate(Converter.timestampToCalendar(dueDate));
    }

    public void setDueDate(Calendar value) {
        this.dueDate = value;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setOccasion(Occasion occasion) {
        this.occasion = occasion;
    }

    public void setOccasion(String value) {
        Occasion occasion = value == null ? null : Occasion.valueOf(value);
        setOccasion(occasion);
    }

    public Occasion getOccasion() {
        return occasion;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public TaskColor getColor() {
        return color;
    }

    public void setColor(TaskColor color) {
        if (color != null) {
            this.color = color;
        }
    }

    public void setColor(String value) {
        TaskColor color = value == null ? null : TaskColor.valueOf(value);
        setColor(color);
    }

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", dueDate=" + dueDate +
                ", occasion=" + occasion +
                ", details='" + details + '\'' +
                ", location='" + location + '\'' +
                ", link='" + link + '\'' +
                ", color=" + color +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId &&
                Utils.equals(title, task.title) &&
                Utils.equals(dueDate, task.dueDate) &&
                Utils.equals(occasion, task.occasion) &&
                Utils.equals(details, task.details) &&
                Utils.equals(location, task.location) &&
                Utils.equals(link, task.link);
    }

}
