package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TestsLog extends AppCompatActivity {

    TextView tvResult;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_db);

        tvResult = findViewById(R.id.tvResult);

        DataBase db = new DataBase();
        tvResult.setText(db.showTL());
        db.close();
    }

    public void btnQL(View v){
        Intent intent = new Intent(this, QuestionsLog.class);
        startActivity(intent);
        finish();
    }

    public void btnTL(View v){
        DataBase db = new DataBase();
        tvResult.setText(db.showTL());
        db.close();
    }
}
