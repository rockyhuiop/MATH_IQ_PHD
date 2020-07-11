package com.example.assignment;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Create extends AppCompatActivity implements OnDownloadFinishListener {
    private final String serverURL = "https://ajtdbwbzhh.execute-api.us-east-1.amazonaws.com/default/201920ITP4501Assignment";
    DataBase db;
    String result;
    String playerName;
    MyAsyncTask task = null;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.splash);
        Intent intent = getIntent();
        playerName = intent.getStringExtra("playerName");
        db = new DataBase();
        getTest();
    }

    public void getTest(){
        if(task == null || task.getStatus().equals(AsyncTask.Status.FINISHED)) {
            task = new MyAsyncTask();
            new MyAsyncTask(this).execute();
        }
    }

    @Override
    public void updateDownloadResult(String result) {
        // do in background by Asynctask
        this.result = result;
        try{
            JSONObject jsonObject = new JSONObject(result);
            JSONArray questions = jsonObject.getJSONArray("questions");
            String[] question = new String[10];
            int[] answers = new int[10];
            for(int i = 0; i < 10; i++){
                question[i] = questions.getJSONObject(i).getString("question");
                answers[i] = questions.getJSONObject(i).getInt("answer");
            }

            // initial Data Base
            db.createQL();
            db.createTLTD();
            for(int i = 0; i < question.length; i++){
                db.insertQuestion(i+1, question[i], answers[i]);
            }
            Intent myIntent = new Intent(this, IQTest.class);
            myIntent.putExtra("playerName", this.playerName);
            startActivity(myIntent);
            finish();
        }catch (Exception e){
            result = e.getMessage();
        }
    }
}
