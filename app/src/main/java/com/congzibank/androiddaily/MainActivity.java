package com.congzibank.androiddaily;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.congzibank.androiddaily.view.DYLoadingView;

public class MainActivity extends AppCompatActivity {

    private DYLoadingView loadingView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadingView = findViewById(R.id.loading);
        loadingView.startAnimation();
    }
}
