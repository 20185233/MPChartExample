package com.zhuanghongji.mpchartexample.notimportant;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.zhuanghongji.mpchartexample.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsActivity extends AppCompatActivity {

    @BindView(R.id.textViewTitle)
    TextView textViewTitle;
    @BindView(R.id.textViewMessage)
    TextView textViewMessage;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);
        setIntentValues();
    }
    
    private void setIntentValues() {
        if (getIntent() != null) {
            textViewTitle.setText(getIntent().getStringExtra("title"));
            textViewMessage.setText(getIntent().getStringExtra("message"));
        }
    }
}