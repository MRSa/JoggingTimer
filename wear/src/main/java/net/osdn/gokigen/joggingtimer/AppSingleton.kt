package net.osdn.gokigen.joggingtimer

import android.app.Application
import net.osdn.gokigen.joggingtimer.stopwatch.WearableActivityController
import net.osdn.gokigen.joggingtimer.stopwatch.timer.MyTimerCounter

class AppSingleton : Application()
{

    override fun onCreate()
    {
        super.onCreate()

        controller = WearableActivityController()
        timerCounter = MyTimerCounter()

    }

    companion object
    {
        lateinit var controller: WearableActivityController
        lateinit var timerCounter: MyTimerCounter
    }
}
