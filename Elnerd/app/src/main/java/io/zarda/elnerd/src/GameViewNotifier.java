package io.zarda.elnerd.src;

import io.zarda.elnerd.MainActivity;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class GameViewNotifier {

    private ViewManager vm;

    public GameViewNotifier(ViewManager vm, MainActivity ma) {
        this.vm = vm;
    }

    public void notifyShowSuccessFinished() {
        vm.endGameView();
        vm.startGameView();
    }

    public void notifyShowFailureFinished() {
        vm.updateScore();
        vm.disableButtons();
    }

}
