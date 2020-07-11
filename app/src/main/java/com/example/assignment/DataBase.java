package com.example.assignment;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;

public class DataBase extends AppCompatActivity {

    // varible dictionary
    private final String TABLE_NAME_QL = "QuestionsLog";
    private final String TABLE_NAME_TL = "TestsLog";
    private final String TABLE_NAME_TD = "TestsDetails";
    private final String[] QL_COLUMN = {"questionNo", "question", "answer"};
    private final String[] TL_COLUMN = {"testNo","playerName","testDate", "testTime", "duration", "correctCount"};
    private final String[] TD_COLUMN = {"testNo","questionNo","question","answer","yourAnswer","isCorrect"};

    private SQLiteDatabase db;
    private String sql;
    private Cursor cursor = null;


    public DataBase(){
        // define db
        db = SQLiteDatabase.openDatabase("/data/data/com.example.assignment/IQTest", null, SQLiteDatabase.CREATE_IF_NECESSARY);
    }

    public void insertQuestion(int questionNo, String question, int answer){
        // insert into QuestionLog
        sql = "INSERT INTO "+TABLE_NAME_QL+"(questionNo, question, answer) VALUES" +
                "("+questionNo+","+"'"+question+"',"+answer+");";
        db.execSQL(sql);
    }
    public void insertTestDetails(int testNo, int questionNo, String question, int answer, int yourAnswer, int isCorrect){
        // insert into TestDetails
        sql = "INSERT INTO "+TABLE_NAME_TD+" VALUES" +
                "("+testNo+","+questionNo+","+"'"+question+"',"+answer+","+yourAnswer+","+isCorrect+");";
        db.execSQL(sql);
    }
    public void updateTestLog(int testNo, double duration, int correctCount){
        sql = "UPDATE "+TABLE_NAME_TL+" SET duration ="+duration+", correctCount ="+correctCount+" WHERE testNo ="+testNo+";";
        db.execSQL(sql);
    }

    public void insertTestLog(int testNo, String playerName, Date testDate, String testTime){
        // insert into TestLog
        sql = "INSERT INTO "+TABLE_NAME_TL+"(testNo, playerName, testDate, testTime) VALUES" +
                "("+testNo+",'"+playerName+"','"+testDate+"','"+testTime+"');";
        db.execSQL(sql);
    }

    public void createQL(){
        // create temporary table QuestionLog
        sql = "DROP TABLE IF EXISTS "+ TABLE_NAME_QL +";";
        db.execSQL(sql);

        sql = "CREATE TABLE "+TABLE_NAME_QL+"(" +
                "questionNo int PRIMARY KEY," +
                "question text," +
                "answer int);";
        db.execSQL(sql);
    }
    public void createTLTD(){
        // create table TestLog and TestDetails
        sql = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_TL+"(" +
                "testNo int PRIMARY KEY," +
                "playerName text," +
                "testDate date," +
                "testTime time," +
                "duration double," +
                "correctCount int);";
        db.execSQL(sql);

        sql = "CREATE TABLE IF NOT EXISTS "+TABLE_NAME_TD+"(" +
                "testNo int," +
                "questionNo int," +
                "question text," +
                "answer int," +
                "yourAnswer int," +
                "isCorrect bool," +
                "PRIMARY KEY(testNo, questionNo)," +
                "CONSTRAINT testNo_fk FOREIGN KEY (testNo) REFERENCES "+TABLE_NAME_TL+"(testNo));";
        db.execSQL(sql);
    }
    public String[][] getIQQuestion(){
        // get iq question from database
        cursor = db.rawQuery("SELECT * FROM "+TABLE_NAME_QL+";",null);
        String[][] dataStr = new String[10][2];

        while (cursor.moveToNext()){
            int questionNo = cursor.getInt(cursor.getColumnIndex(QL_COLUMN[0]));
            String question = cursor.getString(cursor.getColumnIndex(QL_COLUMN[1]));
            int answer = cursor.getInt(cursor.getColumnIndex(QL_COLUMN[2]));
            dataStr[questionNo-1][0] = question;
            dataStr[questionNo-1][1] = ""+answer;
        }
        return dataStr;
    }

    public int getMaximumTestNo(){
        sql = "SELECT MAX(testNo) FROM "+TABLE_NAME_TL+";";
        cursor = db.rawQuery(sql, null);
        int maxNumber = 0;
        if ((cursor.moveToFirst()) || !(cursor.getCount() ==0)){
            maxNumber = cursor.getInt(0);
        }
        return maxNumber;
    }

    public String[][] getTestRecord(){
        sql = "SELECT testNo, playerName, testDate, duration, correctCount FROM "+TABLE_NAME_TL+" ORDER BY correctCount DESC, duration ASC;";
        cursor = db.rawQuery(sql, null);

        String[][] testRecord = new String[20][4];

        int i = 0;
        while (cursor.moveToNext()){
            int testNo = cursor.getInt(cursor.getColumnIndex(TL_COLUMN[0]));
            String playerName = cursor.getString(cursor.getColumnIndex(TL_COLUMN[1]));
            String date = cursor.getString(cursor.getColumnIndex(TL_COLUMN[2]));
            double duration = cursor.getDouble(cursor.getColumnIndex(TL_COLUMN[4]));
            int correctCount = cursor.getInt(cursor.getColumnIndex(TL_COLUMN[5]));




            testRecord[i][0] = playerName;
            testRecord[i][1] = date.substring(0,10);
            testRecord[i][2] = (int)duration+"s";
            testRecord[i][3] = (int)(correctCount / 5.00 * 100)+"";

            if(i >= 19){
                break;
            }else{
                i++;
            }
        }
        return testRecord;
    }

    public void close(){
        db.close();
    }

    public float[] getPercentage(){
        int[] correctCount = new int[10];
        int[] totalCount = new int[10];
        float[] percentage = new float[10];

        sql = "SELECT SUM(isCorrect), questionNo FROM TestsDetails GROUP BY questionNo ORDER BY questionNo ASC;";
        Cursor cursor = db.rawQuery(sql, null);
        while (cursor.moveToNext()){
            int i = cursor.getInt(1);
            if(i != 0){
                correctCount[i-1] = cursor.getInt(0);
            }
        }

        sql = "SELECT COUNT(isCorrect), questionNo FROM TestsDetails GROUP BY questionNo ORDER BY questionNo ASC;";
        cursor = db.rawQuery(sql, null);
        while(cursor.moveToNext()){
            int i = cursor.getInt(1);
            if(cursor.getInt(1) != 0){
                totalCount[i-1] = cursor.getInt(0);
            }
        }

        for(int i = 0; i < 10; i++){
            percentage[i] = (float)(correctCount[i] * 1.00 / totalCount[i])*100;
        }
        return percentage;

    }

}
