package net.osdn.gokigen.joggingtimer.presentation.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ICounterStatus
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert

@Composable
fun SubCounter(counterManager: ITimerCounter)
{
    val totalTimeValue = counterManager.getPastTime()
    val lapTimeValue = counterManager.getPastTime() - if (counterManager.getLastLapTime() <= 0 ) { 0 } else { counterManager.getLastLapTime() }

    // ----- サブカウンタ右側の表示
    val timeString = if (counterManager.getCounterMode()) {
            // ----- サブカウンタは、ラップタイムを表示する
            when (counterManager.getCurrentCountStatus()) {
                ICounterStatus.START -> {
                    // 実行中
                    "[${counterManager.getLapTimeCount()}] ${TimeStringConvert.getTimeString(lapTimeValue)}"
                }
                ICounterStatus.LAPTIME -> {
                    // 実行中(その２)
                    "[${counterManager.getLapTimeCount()}] ${TimeStringConvert.getTimeString(lapTimeValue)}"
                }
                ICounterStatus.STOP -> {
                    // 開始前は、カウンターを表示しない
                    ""
                }
                ICounterStatus.FINISHED -> {
                    // カウント終了時は、カウンターを表示しない
                    ""
                }
            }
        }
        else
        {
            // ----- サブカウンタはトータル時間を表示する
            when (counterManager.getCurrentCountStatus()) {
                ICounterStatus.START -> {
                    // 実行中
                    TimeStringConvert.getTimeString(totalTimeValue)
                }
                ICounterStatus.LAPTIME -> {
                    // 実行中(その２)
                    TimeStringConvert.getTimeString(totalTimeValue)
                }
                ICounterStatus.STOP -> {
                    // 開始前は、カウンターを表示しない
                    ""
                }
                ICounterStatus.FINISHED -> {
                    // カウント終了(結果表示)時は、カウンターを表示しない
                    ""
                }
            }
        }

    ////////////////////////////////////////////////////////////////////////////////////////

    val lapCount = counterManager.getLapTimeCount()  // 現在の進捗
    counterManager.getPastTime()                     // 現在の進捗時間
    counterManager.getLastLapTime()                  // 最新のラップタイム
    val refId = AppSingleton.controller.getReferenceTimerSelection()    // 基準値ID
    val refLapTimeList = counterManager.getReferenceLapTimeList(refId)  // 基準ラップタイムリスト
    val refLapTimeCount = refLapTimeList?.size ?: 0                     // 基準ラップタイム数

    val baseRefLapTime = if ((refLapTimeList != null)&&(refLapTimeCount > 0)) { refLapTimeList[0] } else { 0 }
    val refLapTime = if ((refLapTimeList != null)&&(refLapTimeCount > 0)&&(lapCount > 1)&&(lapCount < refLapTimeCount)) { refLapTimeList[lapCount - 1] - baseRefLapTime } else { 0L }

    val lastLapTime = counterManager.getLastLapTime()
    val totalRefTime = if ((refLapTimeList != null)&&(refLapTimeCount > 1)) { (refLapTimeList[refLapTimeCount - 1] - baseRefLapTime) } else { 0L }

    // 最終ラップタイム時のトータル時間との差分時間を計算
    val totalDiffTime = if (lapCount < refLapTimeCount) { if (lapCount > 1) { lastLapTime - refLapTime} else { 0L } } else { if (lapCount > 1) { lastLapTime - totalRefTime } else { 0L } } // 該当のラップタイム内の時間

    //Log.v("SubCount", " ----- lapCount: $lapCount refLapCount: $refLapTimeCount refLapTime: $refLapTime lastLapTime: ${counterManager.getLastLapTime()} totalRefTime: $totalRefTime diffTime: $totalDiffTime")

    // サブカウンタ左側の表示 (全体進捗を表示)
    val diffTimeString =
        // サブカウンタはトータル時間を表示する
        when (counterManager.getCurrentCountStatus()) {
            ICounterStatus.START -> {
                // 実行中
                TimeStringConvert.getDiffTimeString(totalDiffTime)
            }
            ICounterStatus.LAPTIME -> {
                // 実行中(その２)
                TimeStringConvert.getDiffTimeString(totalDiffTime)
            }
            ICounterStatus.STOP -> {
                // 開始前
                ""
            }
            ICounterStatus.FINISHED -> {
                //// カウント終了(結果表示)
                TimeStringConvert.getDiffTimeString(totalDiffTime)
            }
        }

    Row(horizontalArrangement = Arrangement.SpaceBetween)
    {
        // 左側のサブカウンタ (diffTimeStringを表示)
        Text(
            modifier = Modifier
                .padding(start = 15.dp)
                //.padding(start = 6.dp)
                .weight(1.0f),
            textAlign = TextAlign.Left,
            color = MaterialTheme.colors.secondary,
            text = "$diffTimeString",
            fontSize = 12.sp,
        )
        // 右側のサブカウンタ (timeStringを表示)
        Text(
            modifier = Modifier
                .padding(end = 15.dp)
                //.padding(end = 6.dp)
                .weight(1.0f),
            textAlign = TextAlign.End,
            color = MaterialTheme.colors.secondary,
            text = "$timeString",
            fontSize = 12.sp,
        )
    }
}
