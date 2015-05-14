package io.zarda.elmod.src;

import android.content.Context;
import android.os.Vibrator;

/**
 * Created by atef & emad on 4 May, 2015.
 */
public class GameViewNotifier {

    private ViewManager vm;
    private Context context;

    public GameViewNotifier(ViewManager vm, Context context) {
        this.vm = vm;
        this.context = context;
    }

    public void notifyShowSuccessFinished() {
        vm.endGameView();
        vm.startGameView();
    }

    public void notifyShowFailureFinished() {
        Vibrator vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
        vm.updateScore();
        vm.disableButtons();
    }

}
