package io.zarda.elmod.src;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.FontAwesomeText;
import com.cengalabs.flatui.FlatUI;
import com.cengalabs.flatui.views.FlatButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.zarda.elmod.MainActivity;
import io.zarda.elmod.R;
import io.zarda.elmod.model.Constants;
import io.zarda.elmod.model.Question;
import io.zarda.elmod.view.GameView;
import io.zarda.elmod.view.HomeView;
import io.zarda.elmod.view.Viewable;

/**
 * Created by atef & emad on 4 May, 2015.
 */

public class ViewManager {

    GameViewNotifier gvn;
    HomeViewNotifier hvn;
    HomeView homeView;
    GameView gameView;
    Viewable currentView;
    int bestPlayed;
    int allPlayed;
    private List<View> gameViewsList;
    private List<View> homeViewsList;
    private List<FlatButton> buttonsViewList;
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

        gvn = new GameViewNotifier(this, context);
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
        ArrayList<FlatButton> buttonsViewsArray = new ArrayList<>();

        FontAwesomeText questiontButton = (FontAwesomeText) ((Activity) context).findViewById(
                R.id.question);
        FlatButton choice1Button = (FlatButton) ((Activity) context).findViewById(
                R.id.choice_one);
        FlatButton choice2Button = (FlatButton) ((Activity) context).findViewById(
                R.id.choice_two);
        FlatButton choice3Button = (FlatButton) ((Activity) context).findViewById(
                R.id.choice_three);
        FlatButton choice4Button = (FlatButton) ((Activity) context).findViewById(
                R.id.choice_four);

        gameViewsArray.add(questiontButton);
        buttonsViewsArray.add(choice1Button);
        buttonsViewsArray.add(choice2Button);
        buttonsViewsArray.add(choice3Button);
        buttonsViewsArray.add(choice4Button);

        gameViewsList = Collections.unmodifiableList(gameViewsArray);
        buttonsViewList = Collections.unmodifiableList(buttonsViewsArray);

        ((MainActivity) context).setNewQuestion();
    }

    public void endGameView() {
        gameView.endView();
        currentView = null;
    }

    public void disableButtons() {
        ((FlatButton) (buttonsViewList.get(0))).setEnabled(false);
        ((FlatButton) (buttonsViewList.get(1))).setEnabled(false);
        ((FlatButton) (buttonsViewList.get(2))).setEnabled(false);
        ((FlatButton) (buttonsViewList.get(3))).setEnabled(false);
    }

    public void updateScore() {
        ((MainActivity) context).updatePreferences();
        BootstrapButton best = (BootstrapButton) ((Activity) context).findViewById(R.id.best_score);
        BootstrapButton all = (BootstrapButton) ((Activity) context).findViewById(R.id.all_score);

        best.setText("" + bestPlayed);
        all.setText("" + allPlayed);
    }

    public void showQuestion(Question question, boolean isFirst) {
        Typeface type = Typeface.createFromAsset(context.getAssets(),"fonts/theboldfont.ttf");
        ((FontAwesomeText) gameViewsList.get(0)).setText(question.getHeader());
        ((FontAwesomeText) gameViewsList.get(0)).setTypeface(type);

        for (int i = 0; i < buttonsViewList.size(); ++i) {
            (buttonsViewList.get(i)).setText("" + question.getChoices().get(i));
            (buttonsViewList.get(i)).setTypeface(type);
            (buttonsViewList.get(i)).getAttributes().setTheme(FlatUI.SNOW, context.getResources());
            (buttonsViewList.get(i)).setTextColor(R.color.black);
        }

        if (!isFirst) {
            gameView.setTime(Constants.QuestionLimit);
        }
    }

    public void setCurrentPlayed(int played) {
        gameView.setCurrentPlay(played - 1);
    }

    public void showSuccess(int correctButtonIndex) {
        gameView.setTime(Constants.QuestionLimit);
        gameView.showSuccess(buttonsViewList.get(correctButtonIndex));
    }

    public void showFailure(int correctButtonIndex, int clickedButtonIndex) {
        gameView.showFailure(buttonsViewList.get(correctButtonIndex),
                buttonsViewList.get(clickedButtonIndex));
    }

    public void showFailure(int correctButtonIndex) {
        gameView.setTime(Constants.QuestionLimit);
        gameView.showFailure(buttonsViewList.get(correctButtonIndex), null);
    }

    public boolean inHome() {
        return currentView == homeView;
    }

}

