package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.*;
import android.view.View;

public class ScoreBoard extends AppCompatActivity{
    private boolean stateChanged;
    TextView selection;
    RecyclerView recycler_view;
    MyAdapter adapter;
    String[][] dataArray;
    DataBase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_scoreboard);
        db = new DataBase();

        dataArray = db.getTestRecord();

        recycler_view = findViewById(R.id.rV);
        selection = findViewById(R.id.selection);
        TextView[] podium = new TextView[3];
        podium[0] = (TextView) findViewById(R.id.first);
        podium[1] = (TextView) findViewById(R.id.second);
        podium[2] = (TextView) findViewById(R.id.third);

        for (int i = 0; i < podium.length; i++){
            podium[i].setText(dataArray[i][0]);
        }

        adapter = new MyAdapter(this, dataArray);
        recycler_view.setHasFixedSize(true);
        recycler_view.setAdapter(adapter);
        recycler_view.setLayoutManager(new LinearLayoutManager(this));

//        adapter.setItemClickListener(new OnRecyclerViewClickListener() {
//            @Override
//            public void onItemClickListener(View view) {
//                int position = recycler_view.getChildAdapterPosition(view);
//                selection.setText("Planet - " + dataArray[position]);
//            }
//        });
    }

    public void getChart(View v){
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imagebtn));
        Intent intent = new Intent(ScoreBoard.this, Chart.class);
        startActivity(intent);
    }
}
