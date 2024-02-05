package net.osdn.gokigen.joggingtimer.stopwatch

import net.osdn.gokigen.joggingtimer.stopwatch.timer.LapTimeInfo

/**
 *
 *
 */
interface IDatabaseReloadCallback
{
    fun dataIsReloaded(timelist: ArrayList<LapTimeInfo>)
    fun referenceDataIsReloaded(id: Int, timelist: ArrayList<Long>)
}
