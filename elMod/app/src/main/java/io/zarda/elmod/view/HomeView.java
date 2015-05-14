package io.zarda.elmod.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.View;
import android.widget.Button;

import java.util.List;

import io.zarda.elmod.R;
import io.zarda.elmod.src.HomeViewNotifier;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class HomeView implements Viewable {

    HomeViewNotifier hvn;

    Button play;

    private Context context;

    int screenWidth;
    int screenHeight;

    public HomeView(HomeViewNotifier hvn) {
        this.hvn = hvn;
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
        ((Activity) context).setContentView(R.layout.activity_home);

//        View mainLayout = ((Activity) context).findViewById(R.id.homeMainLayout);
//        Animation goUp = new TranslateAnimation(0, 0, screenHeight, 0);
//        goUp.setDuration(500);
//        mainLayout.startAnimation(goUp);

        play = (Button) ((Activity) context).findViewById(R.id.play);
        play.setBackground(context.getResources().getDrawable(R.drawable.pb));
    }

    @Override
    public void endView() {
//        View mainLayout = ((Activity) context).findViewById(R.id.homeMainLayout);
//        Animation goDown = new TranslateAnimation(0, 0, 0, screenHeight);
//        goDown.setDuration(500);
//        mainLayout.startAnimation(goDown);
        hvn.notifyHomeAnimationFinished();
    }

}
