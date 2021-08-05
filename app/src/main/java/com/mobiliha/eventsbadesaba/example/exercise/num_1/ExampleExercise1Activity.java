package com.mobiliha.eventsbadesaba.example.exercise.num_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentResultListener;

import android.os.Bundle;
import android.widget.TextView;

import com.mobiliha.eventsbadesaba.R;

public class ExampleExercise1Activity extends AppCompatActivity {

    public static final String REQUEST_KEY = "request_key";
    public static final String EXTRA_NAME = "name";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_activity_exercise1);

        TextView textView = findViewById(R.id.text_view);

//        getSupportFragmentManager()
//                .beginTransaction()
//                .add(R.id.fragment_container, Exercise1Fragment.class, null)
//                .commit();

        getSupportFragmentManager()
                .setFragmentResultListener(REQUEST_KEY, this, new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        String name = result.getString(EXTRA_NAME);
                        textView.setText(name);
                    }
                });
    }
}