package com.example.assignment;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Finish extends AppCompatActivity {

    TextView tvCong, tvResult;
    String playerName;
    int result;
    double duration;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_finish);

        Intent intent = getIntent();
        playerName = intent.getStringExtra("playerName");
        result = intent.getIntExtra("result", 0);
        duration = intent.getDoubleExtra("duration", 0);

        tvCong = findViewById(R.id.tvConG);
        tvResult = findViewById(R.id.tvResult);

        tvCong.setText("Congratulation!\n\n"+playerName);
        tvResult.setText("Your Score: " +(int)(result/5.0*100.0) +"% \n\n" +
                "Time spended："+(int)duration+"s");

    }

    public void goBack(View v){
        Intent intent = new Intent(Finish.this, MainActivity.class);
        startActivity(intent);
    }
}
