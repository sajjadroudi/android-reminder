package com.mobiliha.eventsbadesaba.example.exercise.num_2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.mobiliha.eventsbadesaba.R;

public class ExampleExercise2Activity extends AppCompatActivity {

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_activity_exercise2);

        textView = findViewById(R.id.text_view);

    }

    public void setName(String name) {
        textView.setText(name);
    }

}