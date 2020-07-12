package com.example.assignment;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class Chart extends AppCompatActivity {
    float[] datas = new float[10];
    DataBase db;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_chart);
        db = new DataBase();
        String type = "";

        if(getIntent().getExtras() != null) {
            Intent intent = getIntent();
            type = intent.getStringExtra("type");
        }

        if(type.equals("percentage")){
            datas = db.getPercentage();
        }else {
            datas = db.getNumber();
        }

        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(datas[0], 0));
        entries.add(new BarEntry(datas[1], 1));
        entries.add(new BarEntry(datas[2], 2));
        entries.add(new BarEntry(datas[3], 3));
        entries.add(new BarEntry(datas[4], 4));
        entries.add(new BarEntry(datas[5], 5));
        entries.add(new BarEntry(datas[6], 6));
        entries.add(new BarEntry(datas[7], 7));
        entries.add(new BarEntry(datas[8], 8));
        entries.add(new BarEntry(datas[9], 9));

        BarDataSet bardataset = new BarDataSet(entries, "Cells");

        ArrayList<String> labels = new ArrayList<String>();
        labels.add("Q1");
        labels.add("Q2");
        labels.add("Q3");
        labels.add("Q4");
        labels.add("Q5");
        labels.add("Q6");
        labels.add("Q7");
        labels.add("Q8");
        labels.add("Q9");
        labels.add("Q10");

        BarData data = new BarData(labels, bardataset);
        barChart.setData(data); // set the data and list of labels into chart
        barChart.setDescription("Accuracy of each IQ question");  // set the description
        bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
        barChart.animateY(4000);
        db.close();
    }


    public void btnPercentage(View v){
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imagebtn));
        Intent intent = new Intent(Chart.this, Chart.class);
        intent.putExtra("type", "percentage");
        startActivity(intent);
        finish();
    }

    public void btnNumber(View v){
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imagebtn));
        Intent intent = new Intent(Chart.this, Chart.class);
        intent.putExtra("type","number");
        startActivity(intent);
        finish();
    }

    public void btnClean(View v){
        v.startAnimation(AnimationUtils.loadAnimation(this, R.anim.imagebtn));
        AlertDialog.Builder al = new AlertDialog.Builder(this);
        al.setTitle("Warning!");
        al.setMessage("All your datas will be deleted !");
        al.setIcon(R.drawable.ic_baseline_warning_24);
        al.setCancelable(false);

        al.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clean();
            }
        });

        al.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        al.create();
        al.show();
    }

    public void clean(){
        db = new DataBase();
        db.dropTable();
        db.createTLTD();
        db.close();
        Toast.makeText(this,"All test record have been cleaned!", Toast.LENGTH_LONG).show();
        Intent myIntent = new Intent(this, ScoreBoard.class);
        setResult(RESULT_OK, myIntent);
        super.finish();
    }
}
