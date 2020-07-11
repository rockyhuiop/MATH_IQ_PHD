package com.example.assignment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;



import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class IQTest extends AppCompatActivity {

    DataBase db;
    String startTime;
    String playerName;
    int testNo;
    int questionNo;
    int correctCount;
    int[] questionIndex;
    String[] question;
    String yourAnswer;
    TextView tvQuestion, tvPlayer, tvQuestionNo, tvCorrect;
    Button A1,A2,A3,A4;
    Intent local;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN); //enable full screen
        setContentView(R.layout.activity_iqtest);

        local = getIntent();
        playerName = local.getStringExtra("playerName");

        // View initialize
        tvPlayer = findViewById(R.id.tvPlayer);
        tvQuestion = findViewById(R.id.question);
        tvQuestionNo = findViewById(R.id.tvQuestionNo);
        tvCorrect = findViewById(R.id.tvCorrect);
        A1 = findViewById(R.id.A1);
        A2 = findViewById(R.id.A2);
        A3 = findViewById(R.id.A3);
        A4 = findViewById(R.id.A4);

        db = new DataBase();

        tvPlayer.setText("Fighting ! "+playerName);
        questionNo = local.getIntExtra("questionNo", 0);
        correctCount = local.getIntExtra("correctCount", 0);

        if(questionNo <= 5) {
            if(questionNo < 1){
                questionNo = 1;
                questionIndex = genRandom();
                testNo = db.getMaximumTestNo()+1;
                startTime = LocalTime.now().toString();
                Date startDate = new Date();
                db.insertTestLog(testNo, this.playerName, startDate, startTime);
            }else {
                questionIndex = local.getIntArrayExtra("questionIndex");
                testNo = local.getIntExtra("testNo", 0);
                startTime = local.getStringExtra("startTime");
            }

            // get question
//            question = db.getIQQuestion()[questionNo-1];
            String[][] temp = db.getIQQuestion();
            question = db.getIQQuestion()[questionIndex[questionNo-1]];

            tvQuestion.setText(question[0]);
            tvQuestionNo.setText(questionNo + " / 5");
            tvCorrect.setText(""+correctCount);
            setAnswers(question[1]);
        }else {
            startTime = local.getStringExtra("startTime");
            testNo = local.getIntExtra("testNo", 0);
            double duration = LocalTime.parse(startTime).until(LocalTime.now(), ChronoUnit.SECONDS);
            db.updateTestLog(testNo, duration, correctCount);
            Intent intent = new Intent(IQTest.this, Finish.class);
            intent.putExtra("playerName", this.playerName);
            intent.putExtra("result",this.correctCount);
            intent.putExtra("duration", duration);
            startActivity(intent);
            db.close();
            finish();
        }
    }

    // onclick method
    public void btnA1(View view){
        yourAnswer = A1.getText().toString();
        nextQuestion(yourAnswer);
    }
    public void btnA2(View view){
        yourAnswer = A2.getText().toString();
        nextQuestion(yourAnswer);
    }
    public void btnA3(View view){
        yourAnswer = A3.getText().toString();
        nextQuestion(yourAnswer);
    }
    public void btnA4(View view){
        yourAnswer = A4.getText().toString();
        nextQuestion(yourAnswer);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void setAnswers(String answer){
        // random assign correctanswer on buttons
        int correctAnsIndex = ThreadLocalRandom.current().nextInt(0,3);
        Button[] buttons = {A1,A2,A3,A4};
        String[] answers = new String[4];
        int[] fakeAns = genRandomFakeAns((Integer.parseInt(answer)-5), (Integer.parseInt(answer)+5), Integer.parseInt(answer));

        int k = 0;
        for (int i = 0; i < answers.length; i++){
            if(i != correctAnsIndex){
                buttons[i].setText(""+fakeAns[k++]);
            }else {
                buttons[i].setText(answer);
            }
        }
    }

    public int isCorrect(String answer){
        if(answer.equals(question[1])){
            return 1;
        }else {
            return 0;
        }
    }

    public void nextQuestion(String yourAnswer){
        db.insertTestDetails(testNo, questionIndex[questionNo-1]+1, this.question[0],
                Integer.parseInt(this.question[1]), Integer.parseInt(yourAnswer), isCorrect(yourAnswer));
        TextView indicator = findViewById(R.id.indicator);
        if(isCorrect(yourAnswer) == 1){
            correctCount++;
            indicator.setText("+1");
            indicator.setAlpha(1.0f);
            indicator.setTextColor(ContextCompat.getColor(this, R.color.right));
        }else {
            indicator.setText("+0");
            indicator.setAlpha(1.0f);
            indicator.setTextColor(ContextCompat.getColor(this, R.color.wrong));
        }






        questionNo++;

        local.putExtra("questionIndex",questionIndex);
        local.putExtra("startTime", startTime);
        local.putExtra("testNo", testNo);
        local.putExtra("correctCount",correctCount);
        local.putExtra("questionNo",questionNo);
        startActivity(local);
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Date convertToDateViaInstant(LocalDate dateToConvert) {
        return java.util.Date.from(dateToConvert.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant());
    }

    public int[] genRandom(){
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        Collections.shuffle(list);
        Object[] arr = list.toArray();
        int[] intarr = new int[5];
        for(int i = 0; i < 5; i++){
            intarr[i] = (int) arr[i];
        }
        return intarr;
    }

    public int[] genRandomFakeAns(int min, int max, int correctAns){
        List<Integer> list = new ArrayList<>();
        for (int i = min; i <= max; i++) {
            if(i != correctAns){
                list.add(i);
            }
        }
        Collections.shuffle(list);
        Object[] arr = list.toArray();
        int[] intarr = new int[3];
        for(int i = 0; i < 3; i++){
            intarr[i] = (int) arr[i];
        }
        return intarr;
    }
}
