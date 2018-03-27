package net.osdn.gokigen.joggingtimer.utilities;

import android.util.Log;

public class CreateModelData implements CreateModelDataDialog.Callback
{
    private final String TAG = toString();


    public CreateModelData()
    {

    }

    @Override
    public void dataCrated(int lap, int hour, int minute, int second)
    {
        Log.v(TAG, "dataCrated()");

    }

    @Override
    public void dataCreateCancelled()
    {

    }
}
