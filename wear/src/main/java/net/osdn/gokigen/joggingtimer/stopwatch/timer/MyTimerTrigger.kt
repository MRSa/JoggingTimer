package net.osdn.gokigen.joggingtimer.stopwatch.timer

import android.util.Log

/**
 *   タイマーカウンターの実装
 *
 */
class MyTimerTrigger(private val triggerReceiver: ITimeoutReceiver, private val duration: Long) : ITimerStopTrigger
{
    private var isStarted = false

    fun startTimer()
    {
        val thread = Thread {
            if (!isStarted)
            {
                isStarted = true
                Log.v(TAG, "MyTimerTrigger::startTimer() START")
                do
                {
                    try
                    {
                        Thread.sleep(duration)
                    }
                    catch (e: Exception)
                    {
                        // timeout! (But, do nothing!)
                    }
                    triggerReceiver.timeout()
                } while (isStarted)
            }
            else
            {
                Log.v(TAG, "MyTimerTrigger::startTimer() already started...")
            }
        }
        try
        {
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    // ITimerStopTrigger
    override fun forceStop()
    {
        isStarted = false
        Log.v(TAG, "MyTimerTrigger::forceStop()")
    }

    companion object
    {
        private val TAG = MyTimerTrigger::class.java.simpleName
    }

}
