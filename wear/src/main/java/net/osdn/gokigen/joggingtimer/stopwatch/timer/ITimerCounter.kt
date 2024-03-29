package net.osdn.gokigen.joggingtimer.stopwatch.timer

data class LapTimeInfo(val lapTime: Long, val isPass: Boolean)

interface ITimerCounter
{
    fun isStarted(): Boolean
    fun isReset(): Boolean
    fun toggleCounterMode()
    fun getCounterMode() : Boolean
    fun getCurrentCountStatus() : ICounterStatus
    fun start()
    fun stop()
    fun timeStamp(isPass: Boolean): Long
    fun reset()
    fun getLapTimeCount(): Int
    fun getPastTime(): Long
    fun getLapTime(lapCount: Int): Long
    fun isPassLapTime(lapCount: Int): Boolean
    fun getLastLapTime(): Long
    fun getCurrentLapTime(): Long
    fun getStartTime(): Long
    fun getStopTime(): Long
    fun getLapTimeList(): List<LapTimeInfo>
    fun getReferenceLapTimeList(refId: Int): List<Long>?
    fun getReferenceLapTime(refId: Int, position: Int): Long
    fun setCallback(callback: ICounterStatusNotify)
}
