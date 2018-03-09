package net.osdn.gokigen.joggingtimer.stopwatch;

/**
 *
 *
 */
class MyTimerTrigger
{
    private final ITimeoutReceiver triggerReceiver;
    private final long duration;
    private final ITimerCounter timerCounter;

    MyTimerTrigger(ITimeoutReceiver triggerReceiver, long duration, ITimerCounter timerCounter)
    {
        this.triggerReceiver = triggerReceiver;
        this.duration = duration;
        this.timerCounter = timerCounter;
    }

    void startTimer()
    {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
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
                    } while (timerCounter.isStarted());
                }
            }
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

    public interface ITimeoutReceiver
    {
        void timeout();
    }
}
