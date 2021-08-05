package com.mobiliha.eventsbadesaba.example.asynctask;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import com.mobiliha.eventsbadesaba.R;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ExampleAsyncTaskActivity extends AppCompatActivity {

    private class AsyncTaskDownload extends AsyncTask<URL, Void, Bitmap> {

        private ProgressDialog progress;

        @Override
        protected void onPreExecute() {
            progress = new ProgressDialog(ExampleAsyncTaskActivity.this);
            progress.setMessage("Downloading...");
            progress.setCancelable(false);
            progress.show();
        }

        @Override
        protected Bitmap doInBackground(URL... urls) {
            Bitmap result = null;
            try {
                URL url = urls[0];
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                result = BitmapFactory.decodeStream(inputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return result;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            progress.hide();
            ((ImageView) findViewById(R.id.img_result)).setImageBitmap(bitmap);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.example_activity_async_task);

        findViewById(R.id.btn_download).setOnClickListener(v -> {
            try {
                new AsyncTaskDownload().execute(new URL("http://badesaba.ir/assets/img/back-logo.png"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
        });

    }
}