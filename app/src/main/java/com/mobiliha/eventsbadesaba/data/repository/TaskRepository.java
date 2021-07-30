package com.mobiliha.eventsbadesaba.data.repository;

import com.mobiliha.eventsbadesaba.data.local.db.DbHelper;
import com.mobiliha.eventsbadesaba.data.local.db.dao.TaskDao;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.data.remote.BadeSabaApi;
import com.mobiliha.eventsbadesaba.data.remote.RetrofitHelper;
import com.mobiliha.eventsbadesaba.data.remote.model.RemoteTask;
import com.mobiliha.eventsbadesaba.data.remote.model.ShareInfo;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class TaskRepository {

    private final TaskDao dao;
    private final BadeSabaApi api;

    public TaskRepository() {
        this.dao = DbHelper.getInstance().getTaskDao();
        api = RetrofitHelper.createBadeSabaApi();
    }

    public Single<List<Task>> getAllTasks() {
        return applyRequirements(dao.getAllTasks());
    }

    public Single<Task> getTask(int taskId) {
        return applyRequirements(dao.getTask(taskId));
    }

    public Single<Task> insert(Task task) {
        return applyRequirements(dao.insert(task));
    }

    public Single<Task> update(Task task) {
        return applyRequirements(dao.update(task));
    }

    public Completable delete(Task task) {
        return applyRequirements(dao.delete(task));
    }

    public Completable deleteAll() {
        return applyRequirements(dao.deleteAll());
    }

    public Single<ShareInfo> saveTaskInServer(Task task) {
        RemoteTask remoteTask = new RemoteTask(task);
        return applyRequirements(api.saveTask(remoteTask));
    }

    private <T> Single<T> applyRequirements(Single<T> value) {
        return value.subscribeOn(Schedulers.io());
    }

    private Completable applyRequirements(Completable value) {
        return value.subscribeOn(Schedulers.io());
    }

}
