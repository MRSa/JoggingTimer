package net.osdn.gokigen.joggingtimer.stopwatch.timer

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
    fun getLastLapTime(): Long
    fun getCurrentLapTime(): Long
    fun getStartTime(): Long
    fun getStopTime(): Long
    fun getLapTimeList(): List<Long>
    fun getReferenceLapTimeList(): List<Long>?
    fun getReferenceLapTimeList(refId: Int): List<Long>?
    fun getReferenceLapTime(position: Int): Long
    fun getReferenceLapTime(refId: Int, position: Int): Long
    fun selectReferenceLapTime(id: Int)
    fun setCallback(callback: ICounterStatusNotify)
}
