package com.example.textrecognition;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class result extends AppCompatActivity {
    private Button backButton;
    private TextView resultTextView;
    private String resulttext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);
        resultTextView=findViewById(R.id.result_textview);
        backButton=findViewById(R.id.back_button);
        resulttext=getIntent().getStringExtra(textRecognition.RESULT_TEXT);
        resultTextView.setText("Detected text :\n "+resulttext);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}