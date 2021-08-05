package com.mobiliha.eventsbadesaba.example.exercise.num_3;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.mobiliha.eventsbadesaba.R;

public class ExampleExercise3Activity extends AppCompatActivity implements ExampleExercise3Fragment.OnPassData {

    public static final String TAG = "Exercise3Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_activity_exercise3);

        ExampleExercise3Fragment fragment = new ExampleExercise3Fragment(this);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container, fragment)
                .commit();

    }

    @Override
    public void firstMethod() {
        Toast.makeText(this, "firstMethod called", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "firstMethod called");
    }

    @Override
    public void secondMethod() {
        Toast.makeText(this, "secondMethod called", Toast.LENGTH_SHORT).show();
        Log.i(TAG, "secondMethod called");
    }
}