package net.osdn.gokigen.joggingtimer.stopwatch.timer

interface ICounterStatusNotify
{
    fun counterStatusChanged(forceStartTimer: Boolean)
}