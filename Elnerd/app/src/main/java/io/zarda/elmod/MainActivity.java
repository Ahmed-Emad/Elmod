package io.zarda.elmod;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.cengalabs.flatui.FlatUI;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import io.zarda.elmod.model.Constants;
import io.zarda.elmod.model.Constants.SharedMemory;
import io.zarda.elmod.model.Question;
import io.zarda.elmod.src.SharedPreferencesManager;
import io.zarda.elmod.src.ViewManager;


public class MainActivity extends FragmentActivity {

    private final int APP_THEME = R.array.blood;
    ArrayList<String> choices;
    boolean isFirst;
    private SharedPreferencesManager sharedPreferencesManager;
    private CountDownTimer timer;
    private ViewManager vm;
    private int correctIndex;
    private int lastLongestPlayed;
    private int lastAllPlayed;
    private int currentLongestPlayed;
    private int currentAllPlayed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        FlatUI.initDefaultValues(this);

        // Default theme should be set before content view is added
        FlatUI.setDefaultTheme(APP_THEME);

        SharedPreferencesManager.initialize(this);

        sharedPreferencesManager = sharedPreferencesManager.getInstance();

        sharedPreferencesManager.setKey(SharedMemory.LAST_SYNC_TIMESTAMP, 0L);

        lastAllPlayed = (int) sharedPreferencesManager.getKey(SharedMemory.ALL_PLAYED, 0);
        lastLongestPlayed = (int) sharedPreferencesManager.getKey(SharedMemory.LONGEST_PLAYED, 0);

        currentLongestPlayed = 0;
        currentAllPlayed = 0;


        vm = new ViewManager(this);

        updatePreferences();

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
        ++currentAllPlayed;
        vm.setCurrentPlayed(currentAllPlayed);
        Random rand = new Random();
        int mod = rand.nextInt(5);
        mod += 4;
        int number;
        if (currentAllPlayed < 10) {
            number = rand.nextInt(60);
        } else if (currentAllPlayed < 40) {
            number = rand.nextInt(150);
        } else {
            number = rand.nextInt(300);
        }
        boolean[] arr = new boolean[10];
        for (int i = 0; i < 10 ; ++i) {
            arr[i] = false;
        }
        choices = new ArrayList<>();

        choices.add("" + number % mod);
        arr[number % mod] = true;
        int c1, c2, c3;

        c1 = rand.nextInt(mod);
        while (arr[c1]) {
            c1 = rand.nextInt(mod);
        }
        arr[c1] = true;

        c2 = rand.nextInt(mod);
        while (arr[c2]) {
            c2 = rand.nextInt(mod);
        }
        arr[c2] = true;

        c3 = rand.nextInt(mod);
        while (arr[c3]) {
            c3 = rand.nextInt(mod);
        }
        arr[c3] = true;

        choices.add("" + c1);
        choices.add("" + c2);
        choices.add("" + c3);
        Collections.shuffle(choices);
        for (int i = 0; i < 4; i++) {
            if (Integer.parseInt(choices.get(i)) == number % mod) {
                correctIndex = i;
                break;
            }
        }
        Question question = new Question(number + " % " + mod, choices, correctIndex);
        vm.showQuestion(question, isFirst);
        correctIndex = question.getCorrectIndex();

        if (!isFirst) {
            timer = new CountDownTimer(Constants.QuestionLimit, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    System.out.println("seconds remaining: " + (double) millisUntilFinished / 1000);
                }

                @Override
                public void onFinish() {
                    vm.showFailure(correctIndex);
                    timer = null;
                }
            };
            timer.start();
        }
    }

    public void answerClick(View v) {
        if (timer != null || isFirst) {
            if (timer != null) {
                timer.cancel();
            }
            isFirst = false;
            timer = null;
            int tag = Integer.parseInt((String) v.getTag());
            if (tag == correctIndex) {
                vm.showSuccess(correctIndex);
                ++currentLongestPlayed;
            } else {
                vm.showFailure(correctIndex, tag);
            }
        }
    }

    public void playClick(View v) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        currentLongestPlayed = 0;
        isFirst = true;
        vm.endHomeView();
    }

    public void goHome(View v) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        updatePreferences();
        vm.endGameView();
        vm.startHomeView();
    }

    public void endGame(View v) {
        finish();
    }

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
    }

}
