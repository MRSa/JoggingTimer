package net.osdn.gokigen.joggingtimer.stopwatch.laptime

interface ILapTimeHolder
{
    fun getLapTimeCount(): Int
    fun clearLapTime()
    fun addLapTime(item: LapTimeItems)
}
