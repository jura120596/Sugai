package ru.sugai.village;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class TextEditActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_edit);

        // Hide ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        EditText text = (EditText) findViewById(R.id.textEditor);
        String text1 = getIntent().getStringExtra("text");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            text.setText(Html.fromHtml(text1, Html.FROM_HTML_MODE_COMPACT));
        } else {
            text.setText(Html.fromHtml(text1));
        }
        findViewById(R.id.fab_save).setOnClickListener((v) -> {
            Intent intent1 = new Intent();
            intent1.putExtra("text", text.getText().toString().replaceAll("\\n", "<br />"));
            setResult(Activity.RESULT_OK, intent1);
            finish();
        });
    }
}