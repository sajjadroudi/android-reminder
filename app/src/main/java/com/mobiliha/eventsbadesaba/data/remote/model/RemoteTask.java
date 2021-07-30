package com.mobiliha.eventsbadesaba.data.remote.model;

import com.google.gson.annotations.SerializedName;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;

public class RemoteTask {

    @SerializedName("baseId")
    private final String shareId;

    @SerializedName("startTime")
    private final long dueDate;

    private final String title;
    private final Integer occasion;
    private final int color;
    private final String description;
    private final String link;
    private final String location;

    // These fields need to be set otherwise the server returns `internal server` error.
    private final boolean hasRemind = false;
    private final boolean[] remind = new boolean[0];
    private final long endTime;

    public RemoteTask(
            String shareId,
            long dueDate,
            String title,
            Integer occasion,
            int color,
            String description,
            String link,
            String location
    ) {
        this.shareId = shareId;
        this.dueDate = dueDate;
        this.title = title;
        this.occasion = occasion;
        this.color = color;
        this.description = description;
        this.link = link;
        this.location = location;

        this.endTime = dueDate;
    }

    public RemoteTask(Task task) {
        this(
                task.getShareId(),
                task.getDueDate().getTimeInMillis(),
                task.getTitle(),
                task.getOccasion() == null ? null : task.getOccasion().ordinal() + 1,
                task.getColor().ordinal(),
                task.getDetails(),
                task.getLink(),
                task.getLocation()
        );
    }

    public String getTitle() {
        return title;
    }

    public String getShareId() {
        return shareId;
    }

    public long getDueDate() {
        return dueDate;
    }

    public int getOccasion() {
        return occasion;
    }

    public int getColor() {
        return color;
    }

    public String getDescription() {
        return description;
    }

    public String getLink() {
        return link;
    }

    public String getLocation() {
        return location;
    }

}
