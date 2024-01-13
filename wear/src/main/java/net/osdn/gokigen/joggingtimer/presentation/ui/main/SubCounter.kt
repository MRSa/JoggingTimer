package net.osdn.gokigen.joggingtimer.presentation.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ICounterStatus
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert

@Composable
fun SubCounter(counterManager: ITimerCounter)
{
    val totalTimeValue = counterManager.getPastTime() // - counterManager.getStartTime()
    val lapTimeValue = counterManager.getPastTime() - if (counterManager.getLastLapTime() <= 0 ) { 0 } else { counterManager.getLastLapTime() }
    val finishTimeValue = counterManager.getStopTime() - counterManager.getStartTime()
    val timeString = if (counterManager.getCounterMode()) {
            // サブカウンタは、ラップタイムを表示する
            when (counterManager.getCurrentCountStatus()) {
                ICounterStatus.START -> {
                    // 実行中
                    "[${counterManager.getLapTimeCount()}] ${TimeStringConvert.getTimeString(lapTimeValue)}"
                }
                ICounterStatus.STOP -> {
                    // 開始前
                    TimeStringConvert.getTimeString(0)
                }
                ICounterStatus.FINISHED -> {
                    // カウント終了
                    "[${counterManager.getLapTimeCount()}] ${TimeStringConvert.getTimeString(lapTimeValue)}"
                }
            }
        }
        else
        {
            // サブカウンタはトータル時間を表示する
            when (counterManager.getCurrentCountStatus()) {
                ICounterStatus.START -> {
                    // 実行中
                    TimeStringConvert.getTimeString(totalTimeValue)
                }
                ICounterStatus.STOP -> {
                    // 開始前
                    TimeStringConvert.getTimeString(0)
                }
                ICounterStatus.FINISHED -> {
                    // カウント終了(結果表示)
                    TimeStringConvert.getTimeString(finishTimeValue)
                }
            }
        }
    val diffTimeString = timeString

    Row(
        horizontalArrangement = Arrangement.SpaceBetween
    )
    {
        // 左側のサブカウンタ (diffTimeStringを表示)
        Text(
            modifier = Modifier
                .padding(start = 6.dp)
                .weight(1.0f),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.secondary,
            text = "$diffTimeString",
            fontSize = 12.sp,
        )
        // 右側のサブカウンタ (timeStringを表示)
        Text(
            modifier = Modifier
                .padding(end = 6.dp)
                .weight(1.0f),
            textAlign = TextAlign.End,
            color = MaterialTheme.colors.secondary,
            text = "$timeString",
            fontSize = 12.sp,
        )
    }
}
