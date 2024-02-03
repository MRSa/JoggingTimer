package net.osdn.gokigen.joggingtimer.presentation.ui.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert

@Composable
fun LapTimeList(counterManager: ITimerCounter)
{
    //val referenceId = remember { mutableIntStateOf(AppSingleton.controller.getReferenceTimerSelection()) }
    LapTimeListPastTime(counterManager)
}

@Composable
fun LapTimeListPastTime(counterManager: ITimerCounter)
{
    // （通常の）ラップタイム表示
    val lapTimeCount = counterManager.getLapTimeCount()
    val stopLapIndex = 1
    val referenceLapId = AppSingleton.controller.getReferenceTimerSelection()
    //val stopLapIndex = if (lapTimeCount > (5 + 1)) { lapTimeCount - 5 } else { 1 }  // 表示するラップ数を最大5に制限する場合...
    if (lapTimeCount > 0)
    {
        // 最新ラップタイムが上に表示されるようにしてみる
        for (lapTimeIndex in (lapTimeCount - 1) downTo stopLapIndex)
        {
            val lapTime = counterManager.getLapTime(lapTimeIndex)
            val previousTime = counterManager.getLapTime(lapTimeIndex - 1)
            val isPassLapTime = counterManager.isPassLapTime(lapTimeIndex + 1)
            val diffTime = lapTime - previousTime
            val timeString = TimeStringConvert.getTimeString(diffTime)
            val referenceLapTime = AppSingleton.timerCounter.getReferenceLapTime(referenceLapId, lapTimeIndex)
            val textDiffTime = if (referenceLapTime == 0L) { "" } else { " (${TimeStringConvert.getDiffTimeString(diffTime - referenceLapTime)})" }
            val textDecoration = if (isPassLapTime) { TextDecoration.LineThrough } else { TextDecoration.None }
            Text(
                modifier = Modifier.fillMaxWidth()
                    .padding(start = 25.dp),
                textAlign = TextAlign.Start,  // 左寄せ
                color = MaterialTheme.colors.primary,
                text = "[${lapTimeIndex}] $timeString $textDiffTime",
                fontSize = 12.sp,
                textDecoration = textDecoration,
            )
        }
    }
    else
    {
        // ラップタイムがない（リセットされている）場合...
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            color = MaterialTheme.colors.primary,
            text = " - - - - - - -",
            fontSize = 12.sp,
        )
    }
}
