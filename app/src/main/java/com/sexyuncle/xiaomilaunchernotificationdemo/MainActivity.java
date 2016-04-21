package com.sexyuncle.xiaomilaunchernotificationdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Utils.getFiledOfClass(MainActivity.this, 10000);
        final EditText editText = (EditText) findViewById(R.id.number);
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = editText.getText().toString();
                if (!TextUtils.isEmpty(text)) {
                    Utils.getFiledOfClass(MainActivity.this, Integer.parseInt(text));
                }
            }
        });
    }
}
