package com.example.quizapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ResultActivity extends AppCompatActivity {

    private TextView resultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result2);

        resultTextView = findViewById(R.id.resultTextView);

        // Get the Intent that started this activity and extract the data
        Intent intent = getIntent();
        String result = intent.getStringExtra("RESULT");

        // Display the result
        resultTextView.setText(result);

        // Set up the exit button
        findViewById(R.id.exitButton).setOnClickListener(v -> finish());
    }
}
