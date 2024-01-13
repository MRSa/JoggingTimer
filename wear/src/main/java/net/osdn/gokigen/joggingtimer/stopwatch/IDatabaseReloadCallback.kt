package net.osdn.gokigen.joggingtimer.stopwatch

/**
 *
 *
 */
interface IDatabaseReloadCallback
{
    fun dataIsReloaded(timelist: ArrayList<Long>)
    fun referenceDataIsReloaded(id: Int, timelist: ArrayList<Long>)
}
