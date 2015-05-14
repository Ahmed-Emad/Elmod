package io.zarda.elmod.view;

import android.app.Activity;
import android.content.Context;
import android.view.View;

import java.util.List;

import io.zarda.elmod.R;
import io.zarda.elmod.src.HomeViewNotifier;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class HomeView implements Viewable {

    HomeViewNotifier hvn;
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
    }

    @Override
    public void endView() {
        hvn.notifyHomeAnimationFinished();
    }

}
