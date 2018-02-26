package net.osdn.gokigen.joggingtimer;

import android.support.wearable.activity.WearableActivity;

/**
 *
 *
 *
 */
public interface IWearableActivityControl
{
    void setup(WearableActivity activity, IClickCallback callback);
    void exitApplication(WearableActivity activity);

    void vibrate(int duration);
}
