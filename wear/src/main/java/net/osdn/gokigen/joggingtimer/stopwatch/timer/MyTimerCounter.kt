package net.osdn.gokigen.joggingtimer.stopwatch.timer

import android.util.Log
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.stopwatch.IDatabaseReloadCallback
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase.DEFAULT_RECORD_TYPE
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase.PASSAGE_RECORD_TYPE
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    private var lapTime: MutableList<Long>
    private var referenceTimeA: List<Long>? = null
    private var referenceTimeB: List<Long>? = null
    private var referenceTimeC: List<Long>? = null
    private var callback: ICounterStatusNotify? = null
    private var counterMode = mutableStateOf(false)
    private val myTimer = MyTimerTrigger(this, 100)
    private var counterStatus = mutableStateOf(ICounterStatus.STOP)

    init
    {
        lapTime = ArrayList()
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
            lapTime.clear()
            lapTime.add(startTime)
            currentTimer.longValue = startTime
            currentLapCount.intValue = 1
            executeUserFeedback(ICounterStatus.START)
            counterStatus.value = ICounterStatus.START
            createRecordDatabase()
            myTimer.startTimer()
            Log.v(TAG, "start() startTime : $startTime")
        }
    }

    private fun createRecordDatabase()
    {
        try
        {
            val date = Date()
            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
            val title = simpleDateFormat.format(date)
            AppSingleton.controller.createIndex(title, startTime)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun timeStamp(isPass: Boolean): Long
    {
        var timeToSet: Long = 0
        if (counterStatus.value == ICounterStatus.START)
        {
            timeToSet = System.currentTimeMillis()
            lapTime.add(timeToSet)
            ++(currentLapCount.intValue)

            val recordType = if (isPass) { PASSAGE_RECORD_TYPE } else { DEFAULT_RECORD_TYPE }
            AppSingleton.controller.appendTimeData(timeToSet, recordType)

            executeUserFeedback(ICounterStatus.LAPTIME)

        }
        return timeToSet
    }

    override fun stop()
    {
        if (counterStatus.value == ICounterStatus.START)
        {
            stopTime = System.currentTimeMillis()
            lapTime.add(stopTime)
            ++(currentLapCount.intValue)
            executeUserFeedback(ICounterStatus.FINISHED)
            counterStatus.value = ICounterStatus.FINISHED
            AppSingleton.controller.finishTimeData(startTime = startTime, endTime = stopTime)
        }
        myTimer.forceStop()
    }

    private fun executeUserFeedback(counterStatus: ICounterStatus)
    {
        when (counterStatus) {
            ICounterStatus.FINISHED -> {
                AppSingleton.controller.timerStarted(false)
                AppSingleton.controller.vibrate(120)
                AppSingleton.controller.launchNotify(false)  // onGoing Activity (STOP)
            }
            ICounterStatus.START -> {
                AppSingleton.controller.timerStarted(true)
                AppSingleton.controller.vibrate(120)
                AppSingleton.controller.launchNotify(true)  // onGoing Activity (START)
            }
            ICounterStatus.LAPTIME -> {
                AppSingleton.controller.vibrate(50)
            }
            ICounterStatus.STOP -> {
                AppSingleton.controller.vibrate(50)
            }
        }
    }

    override fun reset()
    {
        if (counterStatus.value == ICounterStatus.FINISHED)
        {
            startTime = 0L
            stopTime = 0L
            currentTimer.longValue = 0L
            currentLapCount.intValue = 0
            lapTime.clear()
            counterStatus.value = ICounterStatus.STOP
        }
        myTimer.forceStop()
    }

    override fun getLapTimeCount(): Int
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
            ICounterStatus.LAPTIME -> {
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

    override fun getLapTime(lapCount: Int): Long
    {
        try
        {
            return if (lapCount < 0) 0 else lapTime[lapCount] - startTime
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return getLastLapTime()
    }

    override fun getLastLapTime(): Long
    {
        try
        {
            if (lapTime.size > 0)
            {
                return (lapTime[lapTime.size - 1] - startTime)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return 0L
    }

    override fun getCurrentLapTime(): Long
    {
        val currentTime = System.currentTimeMillis()
        try
        {
            if (lapTime.size > 0)
            {
                return currentTime - lapTime[lapTime.size - 1]
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

    override fun getReferenceLapTimeList(refId: Int): List<Long>?
    {
        //Log.v(TAG, "getReferenceLapTimeList() : $refId")
        return (when (refId)
        {
            0 -> referenceTimeA
            1 -> referenceTimeB
            else -> referenceTimeC
        })
    }

    override fun getLapTimeList(): List<Long>
    {
        return lapTime
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
            lapTime = ArrayList(timelist)
            stopTime = 0L
            currentLapCount.intValue = lapTime.size
            counterStatus.value = ICounterStatus.START
            AppSingleton.controller.launchNotify(true) // onGoing Activity (START)
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
            //selectReferenceLapTime(id)
            when (id) {
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
            Log.v(TAG, "[$id] reference lap time : $size")
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun getReferenceLapTime(refId: Int, position: Int): Long
    {
        try
        {
            val referenceTime: List<Long>? = when (refId)
            {
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
            if (referenceTime == null || location < 1 || referenceTime.size < location)
            {
                return 0
            }
            return if (location == 1)
            {
                referenceTime[0]
            } else referenceTime[location - 1] - referenceTime[location - 2]
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return 0
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
