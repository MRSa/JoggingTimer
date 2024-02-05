package net.osdn.gokigen.joggingtimer

import android.app.Application
import android.util.Log
import net.osdn.gokigen.joggingtimer.stopwatch.WearableActivityController
import net.osdn.gokigen.joggingtimer.stopwatch.timer.MyTimerCounter

class AppSingleton : Application()
{

    override fun onCreate()
    {
        super.onCreate()
        Log.v(TAG, "AppSingleton::create()")

        controller = WearableActivityController()
        timerCounter = MyTimerCounter()
    }

    companion object
    {
        private val TAG = AppSingleton::class.java.simpleName
        lateinit var controller: WearableActivityController
        lateinit var timerCounter: MyTimerCounter
    }
}
