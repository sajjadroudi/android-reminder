package com.mobiliha.eventsbadesaba.example.exercise.num_3;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mobiliha.eventsbadesaba.R;

public class ExampleExercise3Fragment extends Fragment {

    interface OnPassData {
        void firstMethod();
        void secondMethod();
    }

    private final OnPassData callback;

    public ExampleExercise3Fragment(OnPassData callback) {
        super(R.layout.example_fragment_exercise3);
        this.callback = callback;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        callback.firstMethod();
        callback.secondMethod();

    }

}