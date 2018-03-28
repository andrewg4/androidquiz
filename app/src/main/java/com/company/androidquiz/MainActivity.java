package com.company.androidquiz;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextViewBuildVariant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextViewBuildVariant = findViewById(R.id.tv_build_variant);

        String buildVariant = BuildConfig.FLAVOR;
        mTextViewBuildVariant.setText(buildVariant);
    }
}
