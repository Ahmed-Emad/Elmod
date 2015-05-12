package io.zarda.elnerd;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import io.zarda.elnerd.model.Constants.SharedMemory;
import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.src.SharedPreferencesManager;
import io.zarda.elnerd.src.ViewManager;


public class MainActivity extends FragmentActivity {

    private SharedPreferencesManager sharedPreferencesManager;
    private CountDownTimer timer;
    private ViewManager vm;
    private int correctIndex;
    private int lastLongestPlayed;
    private int lastAllPlayed;
    private int currentLongestPlayed;
    private int currentAllPlayed;

    ArrayList<String> choices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        SharedPreferencesManager.initialize(this);

        sharedPreferencesManager = sharedPreferencesManager.getInstance();

        choices = new ArrayList<>();
        choices.add("   0   ");
        choices.add("   1   ");
        choices.add("   2   ");
        choices.add("   3   ");

        sharedPreferencesManager.setKey(SharedMemory.LAST_SYNC_TIMESTAMP, 0L);


        lastAllPlayed = (int) sharedPreferencesManager.getKey(SharedMemory.ALL_PLAYED, 0);
        lastLongestPlayed = (int) sharedPreferencesManager.getKey(SharedMemory.LONGEST_PLAYED, 0);

        currentLongestPlayed = 0;
        currentAllPlayed = 0;

        vm = new ViewManager(this);
        vm.startHomeView();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        updatePreferences();
    }

    @Override
    public void onPause() {
        super.onPause();
        updatePreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        backPressed();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            backPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void backPressed() {
        System.out.println("onBackPressed Called");
        if (!vm.inHome()) {
            vm.endGameView();
            vm.startHomeView();
        } else {
//            Toast.makeText(this, "You are currently in home :)", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    public void setNewQuestion() {
        Random rand = new Random();
        int number = rand.nextInt(100);
//        Collections.shuffle(choices);
        Question question = new Question(number + "  %  4  =  ", choices, number % 4);
        vm.showQuestion(question);
        correctIndex = question.getCorrectIndex();

        timer = new CountDownTimer(6000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {
                System.out.println("seconds remaining: " + (double) millisUntilFinished / 1000);
            }

            @Override
            public void onFinish() {
                vm.endGameView();
                vm.startHomeView();
            }
        };
        timer.start();
    }

    public void answerClick(View v) {
        timer.cancel();
        if ((int) v.getTag() == correctIndex) {
            vm.showSuccess(correctIndex);
            ++currentLongestPlayed;
        } else {
            vm.showFailure(correctIndex, (int) v.getTag());
        }
    }

    public void playClick(View v) {
        currentLongestPlayed = 0;
        vm.endHomeView();
//        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
//        startActivity(browserIntent);
//        Intent browserIntent = new Intent(this, WebViewActivity.class);
//        startActivity(browserIntent);
    }

//    public void loginClick(View v) {
//        FragmentTransaction transaction = mFragmentManager.beginTransaction();
//        transaction.replace(android.R.id.content, new FragmentSimpleLoginButton());
//        transaction.commit();
//    }

    public void updatePreferences() {
        if (currentLongestPlayed > lastLongestPlayed) {
            sharedPreferencesManager.setKey(SharedMemory.LONGEST_PLAYED, currentLongestPlayed);
        }
        sharedPreferencesManager.setKey(SharedMemory.ALL_PLAYED, lastAllPlayed + currentAllPlayed);

        currentLongestPlayed = 0;
        System.out.println("Best: " + (int) sharedPreferencesManager.getKey(
                SharedMemory.LONGEST_PLAYED, 0));
        System.out.println("All: " + (int) sharedPreferencesManager.getKey(
                SharedMemory.LONGEST_PLAYED, 0));
        vm.setScores(
                (int) sharedPreferencesManager.getKey(SharedMemory.LONGEST_PLAYED, 0),
                (int) sharedPreferencesManager.getKey(SharedMemory.ALL_PLAYED, 0));
        lastAllPlayed += currentAllPlayed;
        currentAllPlayed = 0;
        currentLongestPlayed = 0;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
