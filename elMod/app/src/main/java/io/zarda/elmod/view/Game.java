package io.zarda.elmod.view;

import com.cengalabs.flatui.views.FlatButton;

/**
 * Created by atef & emad on 5 May, 2015.
 */
public interface Game {
    void showSuccess(FlatButton correctButton);

    void showFailure(FlatButton correctButton, FlatButton wrongButton);

    void setTime(int msTime);

    void setCurrentPlay(int currentPlayed);
}
