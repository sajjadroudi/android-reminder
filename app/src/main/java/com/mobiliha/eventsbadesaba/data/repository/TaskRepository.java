package com.mobiliha.eventsbadesaba.data.repository;

import com.mobiliha.eventsbadesaba.data.local.db.dao.TaskDao;
import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class TaskRepository {

    private final TaskDao dao;

    public TaskRepository(TaskDao dao) {
        this.dao = dao;
    }

    public Single<List<Task>> getAllTasks() {
        return applyRequirements(dao.getAllTasks());
    }

    public Single<Task> getTask(int taskId) {
        return applyRequirements(dao.getTask(taskId));
    }

    public Completable insert(Task task) {
        return applyRequirements(dao.insert(task));
    }

    public Completable update(Task task) {
        return applyRequirements(dao.update(task));
    }

    public Completable delete(Task task) {
        return applyRequirements(dao.delete(task));
    }

    public Completable deleteAll() {
        return applyRequirements(dao.deleteAll());
    }

    private <T> Single<T> applyRequirements(Single<T> value) {
        return value.subscribeOn(Schedulers.io());
    }

    private Completable applyRequirements(Completable value) {
        return value.subscribeOn(Schedulers.io());
    }

}