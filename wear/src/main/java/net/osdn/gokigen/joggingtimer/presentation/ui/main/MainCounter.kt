package net.osdn.gokigen.joggingtimer.presentation.ui.main

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Text
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ICounterStatus
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert

@Composable
fun MainCounter(counterManager: ITimerCounter)
{
    val totalTimeValue = counterManager.getPastTime() //- counterManager.getStartTime()
    val lapTimeValue = counterManager.getPastTime() - if (counterManager.getLastElapsedTime() <= 0 ) { 0 } else { counterManager.getLastElapsedTime() }
    val finishTimeValue = counterManager.getStopTime() - counterManager.getStartTime()
    val timeString = if (counterManager.getCounterMode()) {
        // メインカウンタはトータル時間を表示する
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
    else
    {
        // メインカウンタは、ラップタイムを表示する
        when (counterManager.getCurrentCountStatus()) {
            ICounterStatus.START -> {
                // 実行中
                "[${counterManager.getElapsedCount()}] ${TimeStringConvert.getTimeString(lapTimeValue)}"
            }
            ICounterStatus.STOP -> {
                // 開始前
                TimeStringConvert.getTimeString(0)
            }
            ICounterStatus.FINISHED -> {
                // カウント終了
                "[${counterManager.getElapsedCount()}] ${TimeStringConvert.getTimeString(lapTimeValue)}"
            }
        }
    }

    Text(
        modifier = Modifier.fillMaxWidth(),
        textAlign = TextAlign.Center,
        color = Color.White, // MaterialTheme.colors.primary,
        fontSize = 18.sp,
        text = timeString.toString()
    )
}
