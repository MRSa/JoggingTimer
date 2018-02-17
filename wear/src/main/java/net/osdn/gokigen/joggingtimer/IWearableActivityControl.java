package net.osdn.gokigen.joggingtimer;

import android.support.wearable.activity.WearableActivity;

/**
 *
 *
 *
 */
public interface IWearableActivityControl
{
    void setup(WearableActivity activity);
    void exitApplication(WearableActivity activity);
}
