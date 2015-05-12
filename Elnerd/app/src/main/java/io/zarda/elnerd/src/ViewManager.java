package io.zarda.elnerd.src;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapThumbnail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.zarda.elnerd.MainActivity;
import io.zarda.elnerd.R;
import io.zarda.elnerd.model.Question;
import io.zarda.elnerd.view.GameView;
import io.zarda.elnerd.view.HomeView;
import io.zarda.elnerd.view.Viewable;

/**
 * Created by atef & emad on 4 May, 2015.
 */

public class ViewManager {

    private List<View> gameViewsList;
    private List<View> homeViewsList;

    GameViewNotifier gvn;
    HomeViewNotifier hvn;

    HomeView homeView;
    GameView gameView;

    Viewable currentView;

    int bestPlayed;
    int allPlayed;

    private Context context;

    public ViewManager(final Context context) {
        this.context = context;

//        // homeView
//        ArrayList<View> homeViewsArray = new ArrayList<>();
//        Button playButton = (Button) ((Activity) context).findViewById(R.id.play);
//        homeViewsArray.add(playButton);
//
//        BootstrapButton bestButton = (BootstrapButton) ((Activity) context).findViewById(R.id.best);
//        BootstrapButton allButton = (BootstrapButton) ((Activity) context).findViewById(R.id.all);
//        homeViewsArray.add(bestButton);
//        homeViewsArray.add(allButton);
//
//        homeViewsList = Collections.unmodifiableList(homeViewsArray);
//
//
//        // game View
//        ArrayList<View> gameViewsArray = new ArrayList<>();
//        BootstrapButton questiontButton = (BootstrapButton) ((Activity) context).findViewById(
//                R.id.question);
//        BootstrapButton choice1Button = (BootstrapButton) ((Activity) context).findViewById(
//                R.id.choice_one);
//        BootstrapButton choice2Button = (BootstrapButton) ((Activity) context).findViewById(
//                R.id.choice_two);
//        BootstrapButton choice3Button = (BootstrapButton) ((Activity) context).findViewById(
//                R.id.choice_three);
//        BootstrapButton choice4Button = (BootstrapButton) ((Activity) context).findViewById(
//                R.id.choice_four);
//
//        gameViewsArray.add(questiontButton);
//        gameViewsArray.add(choice1Button);
//        gameViewsArray.add(choice2Button);
//        gameViewsArray.add(choice3Button);
//        gameViewsArray.add(choice4Button);
//
//        gameViewsList = Collections.unmodifiableList(gameViewsArray);

        gvn = new GameViewNotifier(this, (MainActivity) context);
        hvn = new HomeViewNotifier(this);

        homeView = new HomeView(hvn);
        homeView.initializeView(context, homeViewsList);

        gameView = new GameView(gvn);
        gameView.initializeView(context, gameViewsList);
    }

    public void startHomeView() {
        ((MainActivity) context).updatePreferences();
        homeView.startView();
        currentView = homeView;
        ArrayList<View> homeViewsArray = new ArrayList<>();

        BootstrapButton bestButton = (BootstrapButton) ((Activity) context).findViewById(R.id.best);
        BootstrapButton allButton = (BootstrapButton) ((Activity) context).findViewById(R.id.all);
        homeViewsArray.add(bestButton);
        homeViewsArray.add(allButton);

        homeViewsList = Collections.unmodifiableList(homeViewsArray);

        ((BootstrapButton) homeViewsList.get(0)).setText("" + bestPlayed);
        ((BootstrapButton) homeViewsList.get(1)).setText("" + allPlayed);
    }

    public void endHomeView() {
        homeView.endView();
        currentView = null;
    }

    public void setScores(int best, int all) {
        bestPlayed = best;
        allPlayed = all;
    }

    public void startGameView() {
        gameView.startView();
        currentView = gameView;
        ArrayList<View> gameViewsArray = new ArrayList<>();
        BootstrapButton questiontButton = (BootstrapButton) ((Activity) context).findViewById(
                R.id.question);
        BootstrapButton choice1Button = (BootstrapButton) ((Activity) context).findViewById(
                R.id.choice_one);
        BootstrapButton choice2Button = (BootstrapButton) ((Activity) context).findViewById(
                R.id.choice_two);
        BootstrapButton choice3Button = (BootstrapButton) ((Activity) context).findViewById(
                R.id.choice_three);
        BootstrapButton choice4Button = (BootstrapButton) ((Activity) context).findViewById(
                R.id.choice_four);

        gameViewsArray.add(questiontButton);
        gameViewsArray.add(choice1Button);
        gameViewsArray.add(choice2Button);
        gameViewsArray.add(choice3Button);
        gameViewsArray.add(choice4Button);
        gameViewsList = Collections.unmodifiableList(gameViewsArray);

        ((MainActivity) context).setNewQuestion();
    }

    public void endGameView() {
        gameView.endView();
        currentView = null;
    }

    public void showQuestion(Question question) {
        ((BootstrapButton) gameViewsList.get(0)).setText(question.getHeader());
        for (int i = 1; i < gameViewsList.size(); ++i) {
            ((BootstrapButton) gameViewsList.get(i)).setText(
                    " answer = " + question.getChoices().get(i - 1));
        }

        gameView.setTime(6000);

//        gameView.showNextQuestion();
    }

    public void showSuccess(int correctButtonIndex) {
        gameView.showSuccess((BootstrapButton) gameViewsList.get(correctButtonIndex + 1));
    }

    public void showFailure(int correctButtonIndex, int clickedButtonIndex) {
        gameView.showFailure((BootstrapButton) gameViewsList.get(correctButtonIndex + 1),
                (BootstrapButton) gameViewsList.get(clickedButtonIndex + 1));
    }

    public boolean inHome() {
        return currentView == homeView;
    }

}

