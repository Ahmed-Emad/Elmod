package io.zarda.elnerd.view;

import android.widget.Button;

import com.beardedhen.androidbootstrap.BootstrapButton;

/**
 * Created by atef & emad on 5 May, 2015.
 */
public interface Game {
    void showSuccess(BootstrapButton correctButton);
    void showFailure(BootstrapButton correctButton, BootstrapButton wrongButton);
    void showNextQuestion();
    void setTime(int msTime);
}
