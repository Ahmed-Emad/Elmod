package io.zarda.elmod.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Handler;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;

import java.util.List;
import java.util.Random;

import io.zarda.elmod.R;
import io.zarda.elmod.src.GameViewNotifier;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class GameView implements Viewable, Game {

    Context context;
    GameViewNotifier gvn;

    FontAwesomeText currentPlayed;
    int time;

    int screenWidth;
    int screenHeight;

    TextView bar;

    public GameView(GameViewNotifier gvn) {
        this.gvn = gvn;
    }

    @Override
    public void initializeView(Context context, List<View> views) {
        this.context = context;

        Display screen = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screen.getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;
    }

    @Override
    public void startView() {
        ((Activity) context).setContentView(R.layout.activity_game);
        currentPlayed = (FontAwesomeText) ((Activity) context).findViewById(R.id.current_score);
        LinearLayout gameLayout = (LinearLayout) ((Activity) context).findViewById(R.id.gameLayout);
        String[] colors = {"#2ecc71", "#3498db", "#9b59b6", "#e67e22", "#e74c3c"};
        int color = new Random().nextInt(5);
        gameLayout.setBackgroundColor(Color.parseColor(colors[color]));
    }

    @Override
    public void setTime(int time) {
        this.time = time;
        Animation timeAnimation = new TranslateAnimation(0, -screenWidth, 0, 0);
        timeAnimation.setDuration(time);
        bar = (TextView) ((Activity) context).findViewById(R.id.bar);
        bar.startAnimation(timeAnimation);

    }

    @Override
    public void setCurrentPlay(int played) {
        currentPlayed.setText("  " + played + "  ");
    }

    @Override
    public void endView() {

    }

    @Override
    public void showSuccess(FlatButton correctButton) {
        correctButton.getAttributes().setTheme(FlatUI.GRASS, context.getResources());

        bar.getAnimation().cancel();

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                gvn.notifyShowSuccessFinished();
            }
        }, 200);
    }

    @Override
    public void showFailure(final FlatButton correctButton, FlatButton wrongButton) {
        correctButton.getAttributes().setTheme(FlatUI.GRASS, context.getResources());

        if (wrongButton != null) {
            wrongButton.getAttributes().setTheme(FlatUI.BLOOD, context.getResources());
        }

        if (bar != null) {
            bar.getAnimation().cancel();
        }

        final FrameLayout layout = (FrameLayout) ((Activity) context).findViewById(R.id.frame_layout);
        final LayoutInflater inflater = ((Activity) context).getLayoutInflater();

        final Handler handler = new Handler();
        final View[] scoreView = new View[1];
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (scoreView[0] == null && layout != null) {
                    scoreView[0] = inflater.inflate(R.layout.activity_score, layout, false);
                    layout.addView(scoreView[0]);
                    FlatButton repeat = (FlatButton) ((Activity) context).findViewById(R.id.repeat);
                    FlatButton home = (FlatButton) ((Activity) context).findViewById(R.id.home);
                    repeat.getAttributes().setTheme(FlatUI.DARK, context.getResources());
                    home.getAttributes().setTheme(FlatUI.DARK, context.getResources());
                    gvn.notifyShowFailureFinished();
                }
            }
        }, 200);
    }

}
