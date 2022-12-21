package com.example.mathgame;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.Random;

public class Game extends AppCompatActivity {

    TextView score;
    TextView life;
    TextView time;

    TextView question;
    TextView answer;

    TextView ok;
    TextView next;
    Random random = new Random();
    int number1;
    int number2;
    int userAnswer;
    int realAnswer;
    int userScore = 0;
    int userLife = 3;

    CountDownTimer timer;
    private static  final long START_TIMER_IN_MILS = 60000;
    boolean timerRunning;
    long timeLeftInMils = START_TIMER_IN_MILS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        score = findViewById(R.id.textViewScore);
        life = findViewById(R.id.textViewLife);
        time = findViewById(R.id.textViewTime);
        question = findViewById(R.id.textViewQuestion);
        answer = findViewById(R.id.editTextTextAnswer);
        ok = findViewById(R.id.buttonOk);
        next = findViewById(R.id.buttonNext);

        gameContinue();
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userAnswer = Integer.valueOf(answer.getText().toString());
                pauseTimer();
                if (userAnswer == realAnswer) {
                    userScore = userAnswer + 10;
                    score.setText(""+userScore);
                    question.setText("Congratulation, Your answer is true.");
                } else {
                    userLife = userLife - 1;
                    life.setText(""+userLife);
                    question.setText("Sorry, Your answer is wrong.");
                }

            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                answer.setText("");
                gameContinue();
                resetTimer();

                if (userLife <= 0) {
                    Toast.makeText(getApplicationContext(), "Game Over", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Game.this, Result.class);
                    intent.putExtra("score", userScore);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void gameContinue() {
        number1 = random.nextInt(100);
        number2 = random.nextInt(100);

        realAnswer = number1 + number2;
        question.setText(number1 + " + " + number2);

        startTimer();
    }

    public void  startTimer() {
        timer = new CountDownTimer(timeLeftInMils, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMils = millisUntilFinished;
                updateText();
            }

            @Override
            public void onFinish() {
                timerRunning = false;
                pauseTimer();
                resetTimer();
                updateText();
                userLife = userLife - 1;
                life.setText(""+userLife);
                question.setText("Sorry! Time is up!");
            }
        }.start();

        timerRunning = true;
    }

    public void updateText() {
        int second = (int)(timeLeftInMils / 100) % 60;
        String timeLeft = String.format(Locale.getDefault(), "%02d", second);
        time.setText(timeLeft);
    }

    public void pauseTimer(){
        timer.cancel();
        timerRunning = false;
    }

    public void resetTimer(){
        timeLeftInMils = START_TIMER_IN_MILS;
        updateText();
    }
}