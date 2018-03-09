package net.osdn.gokigen.joggingtimer.stopwatch;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import net.osdn.gokigen.joggingtimer.R;

/**
 *
 *
 */
public class ButtonClickListener implements View.OnClickListener, View.OnLongClickListener, Parcelable
{
    private final String TAG = toString();
    private IClickCallback callback = null;

    ButtonClickListener()
    {

    }

    void setCallback(IClickCallback callback)
    {
        this.callback = callback;
    }

    @Override
    public void onClick(View v)
    {
        int id = v.getId();
        if (callback != null)
        {
            if (id == R.id.btn1)
            {
                callback.clickedBtn1();
            }
            else if (id == R.id.btn2)
            {
                callback.clickedBtn2();
            }
            else if (id == R.id.btn3)
            {
                callback.clickedBtn3();
            }
            else if (id == R.id.main_counter)
            {
                callback.clickedCounter();
            }
        }
    }

    @Override
    public boolean onLongClick(View v)
    {
        boolean ret = false;
        int id = v.getId();
        if (callback != null)
        {
            if (id == R.id.btn1)
            {
                ret = callback.pushedBtn1();
            }
            else if (id == R.id.btn2)
            {
                ret = callback.pushedBtn2();
            }
            else if (id == R.id.btn3)
            {
                ret = callback.pushedBtn3();
            }
        }
        return (ret);
    }





    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        Log.v(TAG, "writeToParcel() ");
    }
    public static final Parcelable.Creator<ButtonClickListener> CREATOR = new Parcelable.Creator<ButtonClickListener>()
    {
        public ButtonClickListener createFromParcel(Parcel in)
        {
            return (new ButtonClickListener(in));
        }

        public ButtonClickListener[] newArray(int size)
        {
            return (new ButtonClickListener[size]);
        }
    };

    private ButtonClickListener(Parcel in)
    {
        try
        {
            //
            Log.v(TAG, " in : " + in.toString());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
