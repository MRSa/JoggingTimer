package net.osdn.gokigen.joggingtimer.presentation.ui.main

import android.content.Context
import android.text.format.DateFormat
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
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ICounterStatus
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter
import net.osdn.gokigen.joggingtimer.utilities.MyPositionIndicatorState
import java.util.Locale

@Composable
fun MainScreen(context: Context, navController: NavHostController, counterManager: ITimerCounter)
{
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = remember { MyPositionIndicatorState() } // rememberScrollState()
    val horizontalPadding = 5.dp
    val enableLapStamp  = remember { mutableStateOf(false)}

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
                            //Log.v("TEST", "Pixels: ${it.verticalScrollPixels}")
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

                // ラップタイムボタンが有効になる条件を設定
                val lapTimeValue = counterManager.getPastTime() - if (counterManager.getLastLapTime() <= 0 ) { 0 } else { counterManager.getLastLapTime() }
                enableLapStamp.value = lapTimeValue > 3000

                // メインカウンタ と サブカウンタ
                MainCounter(counterManager)
                SubCounter(counterManager)

                // 進捗グラフの表示
                GraphArea(
                    counterManager = counterManager,
                    modifier = Modifier
                        .fillMaxWidth()
                        //.height(33.dp)  // 狭い表示...
                        .height(45.dp)    // 48.dp -> 40.dp -> 44.dp
                        .padding(6.dp))

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
            focusRequester.requestFocus()
        }
    }
}
