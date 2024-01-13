package net.osdn.gokigen.joggingtimer.stopwatch

interface IDataEntry
{
    fun createIndex(title: String, startTime: Long)
    fun appendTimeData(elapsedTime: Long)
    fun finishTimeData(startTime: Long, endTime: Long)
}
