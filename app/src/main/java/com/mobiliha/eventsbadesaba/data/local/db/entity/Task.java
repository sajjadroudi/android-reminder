package com.mobiliha.eventsbadesaba.data.local.db.entity;

import androidx.annotation.NonNull;

import com.mobiliha.eventsbadesaba.data.local.db.Converter;
import com.mobiliha.eventsbadesaba.util.Utils;

import java.util.Calendar;

public class Task {

    private static final int DEF_TASK_ID = 0;

    private final int taskId;
    private String title;
    private Calendar dueDate;
    private String occasion;
    private String details;
    private String location;
    private String link;

    public Task(
            int taskId,
            @NonNull String title,
            Calendar dueDate,
            String occasion,
            String details,
            String location,
            String link
    ) {
        this.taskId = taskId;
        setTitle(title);
        setDueDate(dueDate);
        setOccasion(occasion);
        setDetails(details);
        setLocation(location);
        setLink(link);
    }

    public Task(
            @NonNull String title,
            Calendar dueDate,
            String occasion,
            String details,
            String location,
            String link
    ) {
        this(DEF_TASK_ID, title, dueDate, occasion, details, location, link);
    }

    public Task(
            int taskId,
            @NonNull String title,
            long dueDate,
            String occasion,
            String details,
            String location,
            String link
    ) {
        this(taskId, title, Converter.timestampToCalendar(dueDate), occasion, details, location, link);
    }

    public Task(
            @NonNull String title,
            long dueDate,
            String occasion,
            String details,
            String location,
            String link
    ) {
        this(DEF_TASK_ID, title, dueDate, occasion, details, location, link);
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

    public void setOccasion(String occasion) {
        this.occasion = occasion;
    }

    public String getOccasion() {
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

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", dueDate=" + dueDate +
                ", details='" + details + '\'' +
                ", location='" + location + '\'' +
                ", link='" + link + '\'' +
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
