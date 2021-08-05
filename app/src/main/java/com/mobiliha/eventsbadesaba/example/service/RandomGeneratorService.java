package com.mobiliha.eventsbadesaba.example.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import java.util.Random;

/**
 * An example of a bound service.
 */
public class RandomGeneratorService extends Service {

    public static class LocalBinder extends Binder {

        private final RandomGeneratorService service;

        public LocalBinder(RandomGeneratorService service) {
            this.service = service;
        }

        public RandomGeneratorService getService() {
            return service;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new LocalBinder(this);
    }

    public int getRandom() {
        return new Random().nextInt(1000);
    }

}