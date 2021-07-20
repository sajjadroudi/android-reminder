package com.mobiliha.eventsbadesaba.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.mobiliha.eventsbadesaba.data.local.db.entity.Task;
import com.mobiliha.eventsbadesaba.data.repository.TaskRepository;
import com.mobiliha.eventsbadesaba.util.AlarmHelper;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

public class BootReceiver extends BroadcastReceiver {

    public static final String TAG = "BootReceiver";

    // TODO: It's better to use a service to execute the background task.
    @Override
    public void onReceive(Context context, Intent intent) {

        if(!Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()))
            return;

        PendingResult pendingResult = goAsync();
        AlarmHelper alarmHelper = new AlarmHelper(context);

        final CompositeDisposable disposables = new CompositeDisposable();
        new TaskRepository()
                .getAllTasks()
                .toObservable()
                .flatMap(Observable::fromIterable)
                .subscribe(new Observer<Task>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposables.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Task task) {
                        alarmHelper.setAlarm(task);
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        pendingResult.finish();
                        disposables.clear();
                    }

                    @Override
                    public void onComplete() {
                        pendingResult.finish();
                        disposables.clear();
                    }
                });
    }

}