package com.mobiliha.eventsbadesaba.example.exercise.num_1;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobiliha.eventsbadesaba.R;

public class ExampleExercise1Fragment extends Fragment {

    public ExampleExercise1Fragment() {
        super(R.layout.example_fragment_exercise1);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        new Thread(() -> {
            for(int i = 0; i < 20; i++) {

                String name = "Name " + i;

                new Handler(Looper.getMainLooper()).post(() -> {
                    sendData(name);
                });

                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    private void sendData(String data) {
        Bundle bundle = new Bundle();
        bundle.putString(ExampleExercise1Activity.EXTRA_NAME, data);

        getParentFragmentManager()
                .setFragmentResult(ExampleExercise1Activity.REQUEST_KEY, bundle);
    }
    
}