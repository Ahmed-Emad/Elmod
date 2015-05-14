package io.zarda.elmod.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

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

    public HomeView(HomeViewNotifier hvn) {
        this.hvn = hvn;
    }

    @Override
    public void initializeView(Context context, List<View> views) {
        this.context = context;
    }

    @Override
    public void startView() {
        ((Activity) context).setContentView(R.layout.activity_home);

        play = (Button) ((Activity) context).findViewById(R.id.play);
        play.setBackground(context.getResources().getDrawable(R.drawable.pb));
    }

    @Override
    public void endView() {
        hvn.notifyHomeAnimationFinished();
    }

}
