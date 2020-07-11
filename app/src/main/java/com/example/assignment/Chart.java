package com.example.assignment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


public class Chart extends AppCompatActivity {
    float[] percentage = new float[10];
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_chart);
        DataBase db = new DataBase();
        percentage = db.getPercentage();

        BarChart barChart = (BarChart) findViewById(R.id.barchart);

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(percentage[0], 0));
        entries.add(new BarEntry(percentage[1], 1));
        entries.add(new BarEntry(percentage[2], 2));
        entries.add(new BarEntry(percentage[3], 3));
        entries.add(new BarEntry(percentage[4], 4));
        entries.add(new BarEntry(percentage[5], 5));
        entries.add(new BarEntry(percentage[6], 6));
        entries.add(new BarEntry(percentage[7], 7));
        entries.add(new BarEntry(percentage[8], 8));
        entries.add(new BarEntry(percentage[9], 9));

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
}
