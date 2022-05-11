package net.osdn.gokigen.joggingtimer.stopwatch;

/**
 *
 *
 */
class MyTimerTrigger implements ITimerStopTrigger
{
    private final ITimeoutReceiver triggerReceiver;
    private final long duration;
    private final ITimerCounter timerCounter;
    private boolean forceStop = false;

    MyTimerTrigger(ITimeoutReceiver triggerReceiver, long duration, ITimerCounter timerCounter)
    {
        this.triggerReceiver = triggerReceiver;
        this.duration = duration;
        this.timerCounter = timerCounter;
    }

    void startTimer()
    {
        Thread thread = new Thread(() -> {
            if (timerCounter != null)
            {
                do {
                    try {
                        Thread.sleep(duration);
                    } catch (Exception e) {
                        // timeout! (But, do nothing!)
                    }
                    if (triggerReceiver != null)
                    {
                        triggerReceiver.timeout();
                    }
                } while ((timerCounter.isStarted())&&(!forceStop));
            }
            forceStop = false;
        });
        try
        {
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void forceStop()
    {
        forceStop = true;
    }

    public interface ITimeoutReceiver
    {
        void timeout();
    }
}
