package net.osdn.gokigen.joggingtimer.presentation.ui.main

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import java.util.Locale

@Composable
fun MainScreen(navController: NavHostController, counterManager: ITimerCounter)
{
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState() // remember { MyPositionIndicatorState() }
    val horizontalPadding = 5.dp

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
                    modifier = Modifier.scrollAway(scrollState = scrollState)
                )
            },
            positionIndicator = {
                PositionIndicator(scrollState = scrollState)
            },
        ) {
            Column(
                modifier = Modifier
                    .onRotaryScrollEvent {
                        coroutineScope.launch {
                            //Log.v("TEST", "Pixels: ${it.verticalScrollPixels}")
                            scrollState.scrollBy(it.verticalScrollPixels)
                            scrollState.animateScrollBy(0f)
                        }
                        true
                    }
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = horizontalPadding, vertical = 28.dp)  // 20.dp -> 26.dp
                    .focusRequester(focusRequester)
                    .focusable(),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
            ) {
                // メインカウンタ と サブカウンタ
                MainCounter(counterManager)
                SubCounter(counterManager)

                // 進捗グラフの表示
                GraphArea(
                    navController = navController,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)   // 48.dp
                        .padding(6.dp))

                // 現在の状態によって、メインボタンの表示を切り替える
                when (counterManager.getCurrentCountStatus())
                {
                    ICounterStatus.START -> BtnStart(counterManager) // 実行中
                    ICounterStatus.LAPTIME -> BtnStart(counterManager) // 実行中(その2)
                    ICounterStatus.STOP -> BtnStop(navController, counterManager) // 開始前
                    ICounterStatus.FINISHED -> BtnFinished(navController, counterManager)  // 終了
                }

                // ラップタイム一覧の表示
                LapTimeList(counterManager)

                // 現在の状態によって、サブボタンの表示を切り替える
                when (counterManager.getCurrentCountStatus())
                {
                    ICounterStatus.START -> BtnSubStart(counterManager) // 実行中
                    ICounterStatus.LAPTIME -> BtnSubStart(counterManager) // 実行中 (その2)
                    ICounterStatus.STOP -> BtnSubStop(navController, counterManager) // 開始前
                    ICounterStatus.FINISHED -> BtnSubFinished(navController, counterManager)  // 終了
                }
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
            // Log.d("LaunchedEffect", " : ${myScrollState.value}")
        }
    }
}
