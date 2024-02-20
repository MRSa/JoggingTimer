package net.osdn.gokigen.joggingtimer.presentation.ui.main

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.scrollAway
import kotlinx.coroutines.launch
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ICounterStatus
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter
import net.osdn.gokigen.joggingtimer.utilities.MyPositionIndicatorState
import java.util.Locale

@Composable
fun MainScreen(context: Context, navController: NavHostController, counterManager: ITimerCounter)
{
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = remember { MyPositionIndicatorState() }
    val horizontalPadding = 5.dp
    val enableLapStamp  = remember { mutableStateOf(false)}
    val reportReachedLapTime  = remember { mutableStateOf(false)}
    val checkLapCount  = remember { mutableIntStateOf(0)}
    var isNotifyVibrate = false

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background),
        contentAlignment = Alignment.Center
    ) {
        Scaffold(
            timeText = {
                TimeText(
                    timeSource = TimeTextDefaults.timeSource(
                        DateFormat.getBestDateTimePattern(
                            Locale.getDefault(),
                            "HH:mm"
                        ),
                    ),
                    modifier = Modifier.scrollAway(scrollState = scrollState.scrollState)
                )
            },
            positionIndicator = {
                PositionIndicator(scrollState = scrollState.scrollState)
            },
        ) {
            Column(
                modifier = Modifier
                    .onRotaryScrollEvent {
                        coroutineScope.launch {
                            scrollState.scrollState.scrollBy(it.verticalScrollPixels)
                            scrollState.scrollState.animateScrollBy(0f)
                        }
                        true
                    }
                    .fillMaxWidth()
                    .verticalScroll(scrollState.scrollState)
                    .padding(horizontal = horizontalPadding, vertical = 28.dp)  // 20.dp -> 26.dp -> 28.dp
                    .focusRequester(focusRequester)
                    .focusable(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {

                // ----- ラップタイムボタンが有効になる条件を設定
                val lapTimeValue = counterManager.getPastTime() - if (counterManager.getLastLapTime() <= 0 ) { 0 } else { counterManager.getLastLapTime() }
                enableLapStamp.value = lapTimeValue > 3000

                // ----- メインカウンタ と サブカウンタ の表示
                MainCounter(counterManager)
                SubCounter(counterManager)

                // ----- 基準ラップタイムの取得
                val refId = remember { mutableIntStateOf(0) }
                refId.intValue = AppSingleton.controller.getReferenceTimerSelection()
                val refLapTimeList = counterManager.getReferenceLapTimeList(refId.intValue)
                val refLapTimeCount = refLapTimeList?.size ?: 0

                // 「ラップタイム到達通知」を ON に設定している場合の処理...
                if ((refLapTimeList != null)&&(AppSingleton.controller.getNotifyReachedReferenceLap()))
                {
                    // lapTimeValue ... ラップタイム計測経過時間

                    // ----- ラップタイム数
                    val currentLapTime = counterManager.getLapTimeCount() - 1
                    if (currentLapTime != checkLapCount.intValue)
                    {
                        // --- 次のラップタイム ... カウンタをリセット
                        checkLapCount.intValue = currentLapTime
                        reportReachedLapTime.value = false
                    }

                    // reportReachedLapTime.value ... 通知したときに true にする
                    val countStatus = counterManager.getCurrentCountStatus()
                    if ((refLapTimeList.size > (currentLapTime + 1))&&
                        (currentLapTime >= 0)&&
                        (!reportReachedLapTime.value)&&
                        (countStatus != ICounterStatus.STOP)&&
                        (countStatus != ICounterStatus.FINISHED))
                    {
                        // ----- チェック条件（基準ラップタイム）
                        val targetReferenceLapTime =
                            refLapTimeList[currentLapTime + 1] - refLapTimeList[currentLapTime]

                        if (targetReferenceLapTime < lapTimeValue)
                        {
                            // ----- 「お知らせ」の通知する
                            Log.v("MainScreen", " ----- REACHED LAP TIME COUNT -----")

                            // ----- ぶるぶるさせる (実処理は後で実施)
                            isNotifyVibrate = true

                            // 通知したことを記憶する
                            reportReachedLapTime.value = true
                        }
                        // Log.v("MainScreen", "LAP: $currentLapTime target: $targetReferenceLapTime currentTime: $lapTimeValue checkLapCount: ${checkLapCount.intValue} status: ${counterManager.getCurrentCountStatus()}")
                    }

                    // ----- 確認するLAP数のリセット
                    when (counterManager.getCurrentCountStatus())
                    {
                        ICounterStatus.STOP -> {
                            // 開始前
                            checkLapCount.intValue = 0
                        }
                        ICounterStatus.FINISHED -> {
                            // 終了
                            checkLapCount.intValue = 0
                        }
                        else -> { }
                    }

                }

                if ((refLapTimeList != null)&&(refLapTimeCount > 1))
                {
                    // 基準ラップタイムが設定されていた時は、グラフ表示を行う
                    when (AppSingleton.controller.getLapGraphicMode()) {
                        0 -> {
                            // 進捗グラフの表示 (新バージョン)
                            GraphArea(
                                counterManager = counterManager,
                                refLapTimeList = refLapTimeList,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(45.dp)    // 48.dp -> 40.dp -> 44.dp (33.dp)
                                    .padding(6.dp)
                            )
                        }
                        else -> {
                            // 進捗グラフの表示 (旧バージョン)
                            GraphAreaLegacy(
                                counterManager = counterManager,
                                refLapTimeList = refLapTimeList,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(45.dp)    // 48.dp -> 40.dp -> 44.dp (33.dp)
                                    .padding(6.dp)
                            )
                        }
                    }
                }
                else
                {
                    // 基準ラップタイムが設定されていない場合は、平均ラップタイムを表示する
                    DrawAverageLapTime(
                        counterManager = counterManager,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(45.dp)    // 48.dp -> 40.dp -> 44.dp (33.dp)
                            .padding(6.dp)
                    )
                }

                // 現在の状態によって、メインボタンの表示を切り替える
                when (counterManager.getCurrentCountStatus())
                {
                    ICounterStatus.START -> BtnStart(counterManager, context, enableLapStamp.value) // 実行中
                    ICounterStatus.LAPTIME -> BtnStart(counterManager, context, enableLapStamp.value) // 実行中(その2)
                    ICounterStatus.STOP -> BtnStop(navController, counterManager) // 開始前
                    ICounterStatus.FINISHED -> BtnFinished(navController, counterManager)  // 終了
                }

                // ラップタイム一覧の表示
                LapTimeList(counterManager)

                // 現在の状態によって、サブボタンの表示を切り替える
                when (counterManager.getCurrentCountStatus())
                {
                    ICounterStatus.START -> BtnSubStart(counterManager, context) // 実行中
                    ICounterStatus.LAPTIME -> BtnSubStart(counterManager, context) // 実行中 (その2)
                    ICounterStatus.STOP -> BtnSubStop(navController, counterManager) // 開始前
                    ICounterStatus.FINISHED -> BtnSubFinished(navController, counterManager)  // 終了
                }
            }
        }
        LaunchedEffect(Unit) {
            if (isNotifyVibrate)
            {
                coroutineScope.launch {
                    AppSingleton.controller.vibrate(120)
                }
                Log.v("MainScreen", "Notify Vibration")
                isNotifyVibrate = false
            }
            focusRequester.requestFocus()
        }
    }
}
