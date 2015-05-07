package io.zarda.elnerd.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import io.zarda.elnerd.R;
import io.zarda.elnerd.src.GameViewNotifier;

/**
 * Created by atef & emad on 4 May, 2015.
 * Implementing by magdy.
 */
public class GameView implements Viewable , Game{
    Context context;

    int screenWidth;
    int screenHeight;

    FrameLayout mainFrame;
    TableLayout layout;
    Bitmap correctBitmap;
    Bitmap wrongBitmap;
    ImageView correctImage;
    ImageView wrongImage;

    AnimationSet responseAnimation;

    int [] colors = {R.drawable.display , R.drawable.display1 , R.drawable.display2 ,
            R.drawable.display3};

    RelativeLayout displayLayout;
    TextView cardMain;

    Button firstChoice;
    Button secondChoice;
    Button thirdChoice;
    Button forthChoice;

    ArrayList <TextView> cards = new ArrayList<TextView>();

    private float degree = 0;
    private int randomIndex = 0;

    GameViewNotifier gvn;

    public GameView(GameViewNotifier gvn){
        this.gvn = gvn;
    }

    @Override
    public void initializeView(Context context , List<View> views) {
        this.context = context;
        cardMain = (TextView) views.get(0);
        firstChoice = (Button) views.get(1);
        secondChoice = (Button) views.get(2);
        thirdChoice = (Button) views.get(3);
        forthChoice = (Button) views.get(4);

        setDimension();
    }

    @Override
    public void startView() {
//        mainFrame.addView(layout);

        setLayout();
        setDisplayLayout();
        setButtons();

        setBitmapsAndAnimation();
        ((Activity) context).setContentView(mainFrame);
    }

    @Override
    public void endView() {
        layout.removeAllViews();
        mainFrame.removeAllViews();
    }

    @Override
    public void showSuccess(final Button correctButton) {
        correctButton.setBackground(context.getResources().getDrawable(R.drawable.correctbtn));

        // mainFrame.
        if (!correctImage.isShown() && !wrongImage.isShown()) {
            mainFrame.addView(correctImage);
            correctImage.startAnimation(responseAnimation);

            responseAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ((ViewGroup) (correctImage.getParent())).removeView(correctImage);
                    gvn.notifyShowSuccessFinished();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    @Override
    public void showFailure(Button correctButton , final Button wrongButton) {

        wrongButton.setBackground(context.getResources().getDrawable(R.drawable.wrongbtn));

        if (!wrongImage.isShown() && !correctImage.isShown()) {
            mainFrame.addView(wrongImage);
            wrongImage.startAnimation(responseAnimation);

            responseAnimation.setAnimationListener(new Animation.AnimationListener() {
                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    ((ViewGroup) (wrongImage.getParent())).removeView(wrongImage);
                    gvn.notifyShowFailureFinished();
                }

                @Override
                public void onAnimationRepeat(Animation animation) {

                }
            });
        }
    }

    @Override
    public void showNextQuestion() {
        newQuestion();
    }

    private void setBitmapsAndAnimation(){
        correctBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.correct);
        wrongBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.wrong);

        correctImage = new ImageView(context);
        wrongImage = new ImageView(context);

        correctImage.setImageBitmap(correctBitmap);
        wrongImage.setImageBitmap(wrongBitmap)
        ;
        FrameLayout.LayoutParams correctParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT , FrameLayout.LayoutParams.WRAP_CONTENT);

        FrameLayout.LayoutParams wrongParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT , FrameLayout.LayoutParams.WRAP_CONTENT);

        correctParams.height = screenWidth;
        correctParams.width = screenWidth;

        wrongParams.height = screenWidth;
        wrongParams.width = screenWidth;

        correctImage.setLayoutParams(correctParams);
        wrongImage.setLayoutParams(wrongParams);

        Animation fadeAnimation = new AlphaAnimation(0.9f , 0.0f);
        fadeAnimation.setDuration(1000);

        Animation scaleAnimation = new ScaleAnimation(0.25f , 1f , 0.25f , 1f ,
                screenWidth/2 , screenHeight/2);
        scaleAnimation.setDuration(1000);

        responseAnimation = new AnimationSet(false);
        responseAnimation.addAnimation(fadeAnimation);
        responseAnimation.addAnimation(scaleAnimation);
        responseAnimation.setDuration(1000);
        responseAnimation.setFillAfter(true);
    }

    private void setLayout(){
        layout = new TableLayout(context);
        layout.setGravity(Gravity.CENTER);

        mainFrame = new FrameLayout(context);
        mainFrame.addView(layout);
    }

    private void setDisplayLayout(){
        displayLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams displayParams = new RelativeLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        displayParams.width = screenWidth;
        displayParams.height = (int)(screenHeight * 0.5);
        displayLayout.setLayoutParams(displayParams);
        displayLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        layout.addView(displayLayout);
    }

    private void setDimension(){
        Display screen = ((Activity) context).getWindowManager().getDefaultDisplay();
        Point size = new Point();
        screen.getSize(size);
        this.screenWidth = size.x;
        this.screenHeight = size.y;
    }

    private void setButtons(){
        TableLayout.LayoutParams params = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);

        params.setMargins(20 ,20 ,20 ,20);

        firstChoice.setLayoutParams(params);
        secondChoice.setLayoutParams(params);
        thirdChoice.setLayoutParams(params);
        forthChoice.setLayoutParams(params);

        setButtonsDefaultColor();

        layout.addView(firstChoice);
        layout.addView(secondChoice);
        layout.addView(thirdChoice);
        layout.addView(forthChoice);

    }

    private void setButtonsDefaultColor(){
        firstChoice.setBackground(context.getResources().getDrawable(R.drawable.btn));
        secondChoice.setBackground(context.getResources().getDrawable(R.drawable.btn));
        thirdChoice.setBackground(context.getResources().getDrawable(R.drawable.btn));
        forthChoice.setBackground(context.getResources().getDrawable(R.drawable.btn));
    }


    private void getRandomIndex(int x){
        Random random = new Random();
        int randomValue = random.nextInt(x);
        while (randomValue == randomIndex){
            randomValue = random.nextInt(x);
        }
        randomIndex =  randomValue;
    }

    private void getRandomDegree(float x){
        Random random = new Random();
        float randomDegree = random.nextFloat() * x - (x / 2);
        while(randomDegree == degree){
            randomDegree = random.nextFloat() * x - (x / 2);
        }
        degree = randomDegree;
    }

    private void addCard(){
        if(cards.size() == 10){
            cards.remove(0);
        }
        TextView card = new Button(context);
        cards.add(card);
        card.setText(cardMain.getText());
        getRandomIndex(3);
        getRandomDegree(10);

        card.setWidth(screenWidth);
        card.setBackground(context.getResources().getDrawable(colors[randomIndex]));
        card.setGravity(Gravity.CENTER);
        card.setWidth(screenWidth);
        card.setHeight((int) (screenHeight * 0.5));
        card.setTranslationX(new Random().nextFloat() * 20 - 10);
        card.setTranslationY(new Random().nextFloat() * 20 - 10);

        AnimationSet dropAnimation = new AnimationSet(false);

        Animation rotateAnimation = new RotateAnimation(0.0f , degree , screenWidth/2 ,
                screenHeight/4);
        rotateAnimation.setDuration(1000);

        Animation scaleAnimation = new ScaleAnimation(1.0f , 0.85f , 1f , 0.7f , screenWidth / 2 ,
                screenHeight / 4);
        scaleAnimation.setDuration(1000);

        dropAnimation.addAnimation(rotateAnimation);
        dropAnimation.addAnimation(scaleAnimation);
        dropAnimation.setFillAfter(true);

        displayLayout.addView(card);
        card.startAnimation(dropAnimation);
    }

    private void newQuestion(){
        addCard();
        setButtonsDefaultColor();
    }
}
