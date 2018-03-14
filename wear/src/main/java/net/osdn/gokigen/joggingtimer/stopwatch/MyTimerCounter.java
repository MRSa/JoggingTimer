package net.osdn.gokigen.joggingtimer.stopwatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.SystemClock;

/**
 *    My Timer counter
 *
 */
public class MyTimerCounter implements Parcelable, ITimerCounter
{
    private boolean isTimerStopped = true;
    private long startTime = 0;
    private long stopTime = 0;
    private List<Long> elapsedTime = null;

    MyTimerCounter()
    {
        elapsedTime = new ArrayList<>();
    }


    /**
     *   Is my timer running?
     *
     * @return  true : running, false : stopped
     */
    @Override
    public boolean isStarted()
    {
        return (!isTimerStopped);
    }

    @Override
    public boolean isReset()
    {
        return ((isTimerStopped)&&(startTime == 0));
    }

    /**
     *   Start Timer
     *
     */
    @Override
    public void start()
    {
        if (isTimerStopped)
        {
            startTime = SystemClock.elapsedRealtime();
            stopTime = 0;
            elapsedTime.clear();
            elapsedTime.add(startTime);
            isTimerStopped = false;
        }
    }

    @Override
    public long timeStamp()
    {
        long timeToSet = 0;
        if (!isTimerStopped)
        {
            timeToSet = SystemClock.elapsedRealtime();
            elapsedTime.add(timeToSet);
        }
        return (timeToSet);
    }

    @Override
    public void stop()
    {
        if (!isTimerStopped)
        {
            stopTime = SystemClock.elapsedRealtime();
            elapsedTime.add(stopTime);
            isTimerStopped = true;
        }
    }

    @Override
    public void reset()
    {
        if (isTimerStopped)
        {
            startTime = 0;
            stopTime = 0;
            elapsedTime.clear();
        }
    }

    @Override
    public List<Long> getTimerList()
    {
        return (elapsedTime);
    }

    @Override
    public int getElapsedCount()
    {
        return (elapsedTime.size());
    }

    @Override
    public long getPastTime()
    {
        long currentTime = SystemClock.elapsedRealtime();
        if (isTimerStopped)
        {
            if (elapsedTime.size() == 0)
            {
                // カウンタクリア状態なので...
                return (0);
            }
            return (getLastElapsedTime());
        }
        return (currentTime - startTime);
    }

    @Override
    public long getLastElapsedTime()
    {
        try
        {
            return ((elapsedTime.get(elapsedTime.size() - 1)) - startTime);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (0);
    }

    @Override
    public long getCurrentElapsedTime()
    {
        long currentTime = SystemClock.elapsedRealtime();
        try
        {
            return (currentTime - (elapsedTime.get(elapsedTime.size() - 1)));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (currentTime - startTime);
    }

    @Override
    public long getStartTime()
    {
        return (startTime);
    }

    @Override
    public long getStopTime()
    {
        return (stopTime);
    }

    @Override
    public int describeContents()
    {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags)
    {
        dest.writeInt(isTimerStopped ? 1 : 0);
        dest.writeLong(startTime);
        dest.writeLong(stopTime);
        dest.writeList(elapsedTime);
    }

    public static final Parcelable.Creator<MyTimerCounter> CREATOR = new Parcelable.Creator<MyTimerCounter>()
    {
        public MyTimerCounter createFromParcel(Parcel in)
        {
            return (new MyTimerCounter(in));
        }

        public MyTimerCounter[] newArray(int size)
        {
            return (new MyTimerCounter[size]);
        }
    };

    private MyTimerCounter(Parcel in)
    {
        try
        {
            isTimerStopped = (in.readInt() == 1);
            startTime = in.readLong();
            stopTime = in.readLong();
            in.readList(elapsedTime, Long.class.getClassLoader());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     */
    public void reloadTimerCounter(long startTime, ArrayList<Long> timelist)
    {
        this.startTime = startTime;
        elapsedTime = null;
        elapsedTime = new ArrayList<>(timelist);
        stopTime = 0;
        isTimerStopped = false;
    }
}
