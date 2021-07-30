package com.mobiliha.eventsbadesaba.data.local.db.entity;

import androidx.annotation.NonNull;

import com.mobiliha.eventsbadesaba.data.local.db.Converter;
import com.mobiliha.eventsbadesaba.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Task {

    public static final int MAX_TITLE_LENGTH = 40;
    public static final int MIN_TITLE_LENGTH = 3;
    public static final int NOT_INITIALIZED_TASK_ID = -1;

    private final int taskId;
    private String title;
    private Calendar dueDate;
    private Occasion occasion;
    private String details;
    private String location;
    private String link;
    private TaskColor color = TaskColor.BLUE;

    private String token;
    private String shareId;
    private String shareLink;

    public Task(
            int taskId,
            @NonNull String title,
            long dueDate,
            String occasion,
            String details,
            String location,
            String link,
            String color,
            String token,
            String shareId,
            String shareLink
    ) {
        this.taskId = taskId;
        setTitle(title);
        setDueDate(dueDate);
        setOccasion(occasion);
        setDetails(details);
        setLocation(location);
        setLink(link);
        setColor(color);
        setToken(token);
        setShareId(shareId);
        setShareLink(shareLink);
    }

    public Task(int taskId, Task task) {
        this(
                taskId,
                task.getTitle(),
                task.getDueDate().getTimeInMillis(),
                task.getOccasion() == null ? null : task.getOccasion().toString(),
                task.getDetails(),
                task.getLocation(),
                task.getLink(),
                task.getColor() == null ? null : task.getColor().toString(),
                task.getToken(),
                task.getShareId(),
                task.getShareLink()
        );
    }

    public Task() {
        this.taskId = NOT_INITIALIZED_TASK_ID;
    }

    public void invalidateToken() {
        setToken(null);
    }

    public boolean isTokenValid() {
        return token != null;
    }

    public int getTaskId() {
        return taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
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
        this.details = Utils.tryTrim(details);
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
        this.location = Utils.tryTrim(location);
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = Utils.tryTrim(link);
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

    public String getShareId() {
        return shareId;
    }

    public void setShareId(String shareId) {
        this.shareId = shareId;
    }

    public String getShareLink() {
        return shareLink;
    }

    public void setShareLink(String shareLink) {
        this.shareLink = shareLink;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @NonNull
    @Override
    public String toString() {
        return "Task{" +
                "taskId=" + taskId +
                ", title='" + title + '\'' +
                ", dueDate=" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dueDate.getTime()) +
                ", occasion=" + occasion +
                ", details='" + details + '\'' +
                ", location='" + location + '\'' +
                ", link='" + link + '\'' +
                ", color=" + color +
                ", token='" + token + '\'' +
                ", shareId='" + shareId + '\'' +
                ", shareLink='" + shareLink + '\'' +
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
                occasion == task.occasion &&
                Utils.equals(details, task.details) &&
                Utils.equals(location, task.location) &&
                Utils.equals(link, task.link) &&
                color == task.color;
    }

}
