package net.osdn.gokigen.joggingtimer.presentation.ui.detail

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.itemsIndexed
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import kotlinx.coroutines.launch
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.presentation.theme.JoggingTimerTheme
import java.util.Locale

@Composable
fun DetailRecordScreen(context: Context, navController: NavHostController, id: Int)
{
    val dataItem = AppSingleton.controller.getRecordItem(id)
    val lapTimeList = AppSingleton.controller.getLapTimeList(id)
    val lapTimeDataList : ArrayList<LapTimeDataItem> = ArrayList()

    var previousTime = dataItem.startTime
    var previousLapTime = 0L
    for (recordTime in lapTimeList)
    {
        val lapTime = recordTime.recordTime - previousTime
        lapTimeDataList.add(LapTimeDataItem(recordTime.recordType, recordTime.recordTime, lapTime, lapTime - previousLapTime, 0L))
        previousLapTime = lapTime
        previousTime = recordTime.recordTime
    }

    JoggingTimerTheme {
        val focusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()
        val listState = rememberScalingLazyListState()

        Scaffold(
            timeText = {
                TimeText(
                    timeSource = TimeTextDefaults.timeSource(
                        DateFormat.getBestDateTimePattern(
                            Locale.getDefault(),
                            "HH:mm"
                        ),
                    ),
                    modifier = Modifier.scrollAway(scrollState = listState)
                )
            },
            vignette = {
                Vignette(vignettePosition = VignettePosition.TopAndBottom)
            },
            positionIndicator = {
                PositionIndicator(scalingLazyListState = listState)
            },
        ) {
            if ((dataItem.indexId <= 0) || (lapTimeList.isEmpty()))
            {
                // 「詳細データの取得に失敗」を表示する
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(focusRequester)
                        .padding(
                            PaddingValues(
                                top = 25.dp
                            )
                        ),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colors.primary,
                        fontSize = 16.sp,
                        text = stringResource(R.string.failed_to_get_details)
                    )
                }
            }
            else
            {
                ScalingLazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .onRotaryScrollEvent {
                            coroutineScope.launch {
                                listState.scrollBy(it.verticalScrollPixels)
                                listState.animateScrollBy(0f)
                            }
                            true
                        }
                        .focusRequester(focusRequester)
                        .focusable(),
                    contentPadding = PaddingValues(
                        top = 2.dp,
                        start = 0.dp,
                        end = 0.dp,
                        bottom = 8.dp,
                    ),
                    state = listState
                ) {
                    this.item {
                        ListHeader(
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Column(
                                modifier = Modifier.fillMaxSize()
                                    .focusRequester(focusRequester),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                // タイトル（記録詳細）を表示
                                //Text(
                                //    //modifier = Modifier.fillMaxWidth(),
                                //    textAlign = TextAlign.Center,
                                //    color = Color.White,
                                //    fontSize = 14.sp,
                                //    text = stringResource(id = R.string.result_detail)
                                //)
                                // データの題名を表示 (ただし、長いと表示があふれる)
                                Text(
                                    //modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    color = MaterialTheme.colors.primary,
                                    fontSize = 12.sp,
                                    text = dataItem.title,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                // 制御ボタン群を表示
                                DetailControlButtons(context, navController, id, dataItem, lapTimeDataList)
                            }
                        }
                    }
                    this.itemsIndexed(items = lapTimeDataList) { indexNo, lapTimeData ->
                        if (indexNo > 0)
                        {
                            LapTimeItem(context, navController, indexNo, lapTimeData)
                        }
                    }
                }
            }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
        }
    }
}
