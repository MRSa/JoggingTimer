package net.osdn.gokigen.joggingtimer.stopwatch.timer

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import net.osdn.gokigen.joggingtimer.stopwatch.IDatabaseReloadCallback

/**
 * My Timer counter
 *
 */
class MyTimerCounter internal constructor() : ITimerCounter, ITimeoutReceiver, IDatabaseReloadCallback
{
    private var startTime = 0L
    private var stopTime = 0L
    private var currentTimer = mutableLongStateOf(0)
    private var currentLapCount = mutableIntStateOf(0)
    private var referenceTimeId = 0
    private var elapsedTime: MutableList<Long>
    private var referenceTimeA: List<Long>? = null
    private var referenceTimeB: List<Long>? = null
    private var referenceTimeC: List<Long>? = null
    private var callback: ICounterStatusNotify? = null
    private var counterMode = mutableStateOf(false)
    private val myTimer = MyTimerTrigger(this, 100)
    private var counterStatus = mutableStateOf(ICounterStatus.STOP)

    init
    {
        elapsedTime = ArrayList()
        currentLapCount.intValue = 0
        currentTimer.longValue = 0L
    }

    /**
     * Is my timer running?
     *
     * @return  true : running, false : stopped
     */
    override fun isStarted(): Boolean
    {
        return (counterStatus.value == ICounterStatus.START)
    }

    override fun isReset(): Boolean
    {
        return (counterStatus.value == ICounterStatus.STOP)
    }

    override fun toggleCounterMode()
    {
        counterMode.value = !(counterMode.value)
        Log.v(TAG, "toggleCounterMode : ${counterMode.value}")
    }

    override fun getCounterMode() : Boolean
    {
        return (counterMode.value)
    }

    override fun getCurrentCountStatus(): ICounterStatus
    {
        return (counterStatus.value)
    }

    /**
     * Start Timer
     *
     */
    override fun start()
    {
        if (counterStatus.value != ICounterStatus.START)
        {
            startTime = System.currentTimeMillis()
            stopTime = 0L
            elapsedTime.clear()
            elapsedTime.add(startTime)
            currentTimer.longValue = startTime
            currentLapCount.intValue = 1
            counterStatus.value = ICounterStatus.START
            myTimer.startTimer()
            Log.v(TAG, "start() startTime : ${startTime}")
        }
    }

    override fun timeStamp(): Long
    {
        var timeToSet: Long = 0
        if (counterStatus.value == ICounterStatus.START)
        {
            timeToSet = System.currentTimeMillis()
            elapsedTime.add(timeToSet)
            ++(currentLapCount.intValue)
        }
        return timeToSet
    }

    override fun stop()
    {
        if (counterStatus.value == ICounterStatus.START)
        {
            stopTime = System.currentTimeMillis()
            elapsedTime.add(stopTime)
            ++(currentLapCount.intValue)
            counterStatus.value = ICounterStatus.FINISHED
        }
        myTimer.forceStop()
    }

    override fun reset()
    {
        if (counterStatus.value == ICounterStatus.FINISHED)
        {
            startTime = 0L
            stopTime = 0L
            currentTimer.longValue = 0L
            currentLapCount.intValue = 0
            elapsedTime.clear()
            counterStatus.value = ICounterStatus.STOP
        }
        myTimer.forceStop()
    }

    override fun getElapsedCount(): Int
    {
        return (currentLapCount.intValue)
    }

    override fun getPastTime(): Long
    {
        return when (getCurrentCountStatus()) {
            ICounterStatus.START -> {
                // 実行中...タイマースタートからの時間を返す
                (currentTimer.longValue - startTime)
            }
            ICounterStatus.STOP -> {
                // カウンタリセット時
                0L
            }
            ICounterStatus.FINISHED -> {
                // カウンタ終了時
                (stopTime - startTime)
            }
        }
    }

    override fun getElapsedTime(lapCount: Int): Long
    {
        try
        {
            return if (lapCount < 0) 0 else elapsedTime[lapCount] - startTime
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return getLastElapsedTime()
    }

    override fun getLastElapsedTime(): Long
    {
        try
        {
            if (elapsedTime.size > 0)
            {
                return (elapsedTime[elapsedTime.size - 1] - startTime)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return 0L
    }

    override fun getCurrentElapsedTime(): Long
    {
        val currentTime = System.currentTimeMillis()
        try
        {
            if (elapsedTime.size > 0)
            {
                return currentTime - elapsedTime[elapsedTime.size - 1]
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return currentTime - startTime
    }

    override fun getStartTime(): Long
    {
        return startTime
    }

    override fun getStopTime(): Long
    {
        return stopTime
    }

    override fun getReferenceLapTimeList(): List<Long>?
    {
        if (referenceTimeId == 0) {
            return referenceTimeA
        } else if (referenceTimeId == 1) {
            return referenceTimeB
        }
        return referenceTimeC
    }
    override fun getLapTimeList(): List<Long>
    {
        return elapsedTime
    }

    override fun setCallback(callback: ICounterStatusNotify)
    {
        this.callback = callback
    }

    override fun dataIsReloaded(timelist: ArrayList<Long>)
    {
        Log.v(TAG, "dataIsReloaded() : lap ${timelist.size}")
        try
        {
            val startTime = timelist[0]
            val pastTime = System.currentTimeMillis() - startTime
            myTimer.startTimer()
            Log.v(TAG, "pastTime : $pastTime")
            this.startTime = startTime
            elapsedTime = ArrayList(timelist)
            stopTime = 0L
            currentLapCount.intValue = elapsedTime.size
            counterStatus.value = ICounterStatus.START
            callback?.counterStatusChanged(true)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun referenceDataIsReloaded(id: Int, timelist: ArrayList<Long>)
    {
        try
        {
            val size: Int
            selectReferenceLapTime(id)
            when (referenceTimeId) {
                0 -> {
                    referenceTimeA = null
                    referenceTimeA = ArrayList(timelist)
                    size = referenceTimeA?.size ?: 0
                }
                1 -> {
                    referenceTimeB = null
                    referenceTimeB = ArrayList(timelist)
                    size = referenceTimeB?.size ?: 0
                }
                else -> {
                    referenceTimeC = null
                    referenceTimeC = ArrayList(timelist)
                    size = referenceTimeC?.size ?: 0
                }
            }
            callback?.counterStatusChanged(false)
            Log.v(TAG, "reference lap time : $size")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun getReferenceLapTime(position: Int): Long {
        try
        {
            val referenceTime: List<Long>? = when (referenceTimeId) {
                0 -> {
                    referenceTimeA
                }
                1 -> {
                    referenceTimeB
                }
                else -> {
                    referenceTimeC
                }
            }
            val location = position + 1
            if (referenceTime == null || location < 1 || referenceTime.size < location) {
                return 0
            }
            return if (location == 1) {
                referenceTime[0]
            } else referenceTime[location - 1] - referenceTime[location - 2]
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return 0
    }

    override fun selectReferenceLapTime(id: Int)
    {
        referenceTimeId = id
    }

    override fun timeout()
    {
        try
        {
            currentTimer.longValue = System.currentTimeMillis()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = MyTimerCounter::class.java.simpleName
    }
}
