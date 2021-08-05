package com.mobiliha.eventsbadesaba.example.intent_service;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import com.mobiliha.eventsbadesaba.R;

public class ExampleIntentServiceActivity extends AppCompatActivity {

    private static final int REQ_CODE = 0;

    private TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_activity_intent_service);

        txtResult = findViewById(R.id.txt_result);

        ((EditText) findViewById(R.id.edt_input)).addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                process(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });
    }

    private void process(String info) {
        PendingIntent pendingResult = createPendingResult(REQ_CODE, new Intent(), 0);

        Intent intent = new Intent(this, InfoProcessorIntentService.class);
        intent.putExtra(InfoProcessorIntentService.EXTRA_INFO, info);
        intent.putExtra(InfoProcessorIntentService.EXTRA_PENDING_RESULT, pendingResult);

        startService(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == REQ_CODE && resultCode == InfoProcessorIntentService.RESULT_CODE) {
            int result = data.getIntExtra(InfoProcessorIntentService.EXTRA_RESULT, -1);

            if(result != -1) {
                txtResult.setText("Result: " + result);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {

        Intent intent = new Intent(this, InfoProcessorIntentService.class);
        stopService(intent);

        super.onDestroy();
    }
}