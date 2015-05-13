package io.zarda.elnerd.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.beardedhen.androidbootstrap.BootstrapButton;

import java.util.List;

import io.zarda.elnerd.R;
import io.zarda.elnerd.src.GameViewNotifier;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class GameView implements Viewable, Game {

    Context context;
    GameViewNotifier gvn;

    BootstrapButton currentPlayed;
    int time;

    public GameView(GameViewNotifier gvn) {
        this.gvn = gvn;
    }

    @Override
    public void initializeView(Context context, List<View> views) {
        this.context = context;
    }

    @Override
    public void startView() {
        ((Activity) context).setContentView(R.layout.activity_game);
        currentPlayed = (BootstrapButton) ((Activity) context).findViewById(R.id.current_score);
    }

    @Override
    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public void setCurrentPlay(int played) {
        currentPlayed.setText("  " + played + "  ");
    }

    @Override
    public void endView() {

    }

    @Override
    public void showSuccess(BootstrapButton correctButton) {
        correctButton.setBootstrapType("success");
        correctButton.setRightIcon("fa-check");
        correctButton.setLeftIcon("fa-check");

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gvn.notifyShowSuccessFinished();
            }
        }, 800);
    }

    @Override
    public void showFailure(BootstrapButton correctButton, BootstrapButton wrongButton) {
        correctButton.setBootstrapType("success");
        correctButton.setRightIcon("fa-check");
        correctButton.setLeftIcon("fa-check");

        if (wrongButton != null) {
            wrongButton.setBootstrapType("danger");
            wrongButton.setRightIcon("fa-times");
            wrongButton.setLeftIcon("fa-times");
        }

        final FrameLayout layout = (FrameLayout) ((Activity) context).findViewById(R.id.frame_layout);
        final LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                View scoreView = inflater.inflate(R.layout.activity_score, layout, false);
                layout.addView(scoreView);
                gvn.notifyShowFailureFinished();
            }
        }, 800);
    }

    @Override
    public void showNextQuestion() {

    }

}
