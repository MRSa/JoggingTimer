package net.osdn.gokigen.joggingtimer.presentation.ui.detail

data class LapTimeDataItem(val absoluteTime: Long, val lapTime: Long, val diffTime: Long, val shiftTime: Long, val useReference: Boolean = false)
