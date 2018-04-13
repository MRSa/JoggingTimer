package net.osdn.gokigen.joggingtimer.stopwatch;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.NonNull;
import android.util.Log;

/**
 *    My Timer counter
 *
 */
public class MyTimerCounter implements ITimerCounter, IDatabaseReloadCallback
{
    private final String TAG = toString();
    //private static final long COUNTER_UPPERLIMIT = (80 / (1000 * 60 * 60));  // 80h
    private boolean isTimerStopped = true;
    private ICounterStatusNotify callback = null;
    private long startTime = 0;
    private long stopTime = 0;
    private List<Long> elapsedTime;
    private List<Long> referenceTime = null;

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
            startTime = System.currentTimeMillis();
            stopTime = 0;
            elapsedTime.clear();
            elapsedTime.add(startTime);
            isTimerStopped = false;
            Log.v(TAG, "start() startTime : " + startTime);
        }
    }

    @Override
    public long timeStamp()
    {
        long timeToSet = 0;
        if (!isTimerStopped)
        {
            timeToSet = System.currentTimeMillis();
            elapsedTime.add(timeToSet);
        }
        return (timeToSet);
    }

    @Override
    public void stop()
    {
        if (!isTimerStopped)
        {
            stopTime = System.currentTimeMillis();
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
    public int getElapsedCount()
    {
        return (elapsedTime.size());
    }

    @Override
    public long getPastTime()
    {
        long currentTime = System.currentTimeMillis();
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
    public long getElapsedTime(int lapCount)
    {
        try
        {
            return ((lapCount < 0) ? 0 : (elapsedTime.get(lapCount) - startTime));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (getLastElapsedTime());
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
        long currentTime = System.currentTimeMillis();
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
    public List<Long> getReferenceLapTimeList()
    {
        return (referenceTime);
    }

    @Override
    public List<Long> getLapTimeList()
    {
        return (elapsedTime);
    }

    @Override
    public void setCallback(ICounterStatusNotify callback)
    {
        this.callback = callback;
    }

    @Override
    public void dataIsReloaded(@NonNull ArrayList<Long> timelist)
    {
        try
        {
            long startTime = timelist.get(0);
            long pastTime = System.currentTimeMillis() - startTime;
            Log.v(TAG, "pastTime : " + pastTime);
            this.startTime = startTime;
            elapsedTime = null;
            elapsedTime = new ArrayList<>(timelist);
            stopTime = 0;
            isTimerStopped = false;
            if (callback != null)
            {
                callback.counterStatusChanged(true);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void referenceDataIsReloaded(ArrayList<Long> timelist)
    {
        try
        {
            referenceTime = null;
            if (timelist != null)
            {
                referenceTime = new ArrayList<>(timelist);
                if (callback != null)
                {
                    callback.counterStatusChanged(false);
                }
                Log.v(TAG, "reference lap time : " + referenceTime.size());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public long getReferenceLapTime(int position)
    {
        try
        {
            int location = position + 1;
            if ((referenceTime == null)||(location < 1)||(referenceTime.size() < location))
            {
                return (0);
            }
            if (location == 1)
            {
                return (referenceTime.get(0));
            }
            return (referenceTime.get(location - 1) - referenceTime.get(location - 2));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (0);
    }

    public interface ICounterStatusNotify
    {
        void counterStatusChanged(boolean forceStartTimer);
    }
}
