package net.osdn.gokigen.joggingtimer.presentation.ui.edit

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.horologist.composables.TimePicker
import kotlinx.coroutines.launch
import net.osdn.gokigen.joggingtimer.AppSingleton
import java.time.LocalTime

@Composable
fun LapTimeEditScreen(navController: NavHostController, indexId: Int, lapIndex: Int, recordIndexId: Long)
{
    val coroutineScope = rememberCoroutineScope()
    val lapTimeList = AppSingleton.controller.getLapTimeList(indexId)

    // 変更対象のラップタイム現在設定値を取得する
    val targetLapTime = if ((lapTimeList.size >= lapIndex)&&(lapIndex >= 1))
    {
        lapTimeList[lapIndex].recordTime - lapTimeList[lapIndex - 1].recordTime
    }
    else
    {
        0L
    }

    // ミリ秒をナノ秒に変換して、LocalTimeの初期値として与える
    val initialTime = LocalTime.ofNanoOfDay(targetLapTime * 1000000L)
    Log.v("EDIT", "lapTime: $targetLapTime $initialTime" )

    // 時分秒を入力してもらい、データベースに記録する
    TimePicker(
        onTimeConfirm = {
            val newTimeDataMs = (it.hour * 60L * 60L + it.minute * 60L + it.second) * 1000L  // ミリ秒に変更
            val diffTimeMs = newTimeDataMs - targetLapTime   // 新旧データの差分

            // 各ラップタイムへ 新旧データの差分値をばらまく
            val eachLapTimeOffset = if (lapTimeList.size > 2) { ((newTimeDataMs - targetLapTime) / (lapTimeList.size - 2)) * (-1) } else { 0L }

            Log.v("EDIT", "TIME CHANGE: from $targetLapTime to $newTimeDataMs (diff: $diffTimeMs, each lap: $eachLapTimeOffset)" )
            coroutineScope.launch {
                var previousTime = 0L
                var previousDiffTime = 0L
                lapTimeList.forEachIndexed { index, it ->
                    Log.v("EDIT", " ${it.recordIndexId} : ${it.recordTime} [$eachLapTimeOffset] $previousDiffTime $previousTime")
                    if (index > 0)
                    {
                        // ラップタイム
                        val diffTime = it.recordTime - previousDiffTime

                        // 先頭データ以外を更新する。
                        val updateTimeValue = if (it.recordIndexId != recordIndexId) { previousTime + diffTime + eachLapTimeOffset }  else { previousTime + newTimeDataMs }
                        val ret = AppSingleton.controller.updateTimeEntryData(
                            it.recordIndexId,
                            updateTimeValue
                        )
                        previousTime = updateTimeValue
                        previousDiffTime = it.recordTime
                        if (!ret)
                        {
                            Log.v("EDIT", "RECORD UPDATE FAILURE. (id:${it.recordIndexId}, time:${it.recordTime}, offset:$updateTimeValue")
                        }
                    }
                }
            }
            navController.popBackStack()  // 前の画面に戻る
        },
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black)
            .padding(start = 8.dp),
        time = initialTime
    )
}
