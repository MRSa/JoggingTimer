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
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
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
import net.osdn.gokigen.joggingtimer.presentation.ui.list.ResultListItem
import net.osdn.gokigen.joggingtimer.presentation.ui.list.ResultListTitle
import java.util.Locale

@Composable
fun DetailRecordScreen(context: Context, navController: NavHostController, id: Int)
{
    val dataItem = AppSingleton.controller.getRecordItem(id)
    val lapTimeList = AppSingleton.controller.getLapTimeList(id)
    val lapCount = if (lapTimeList.isEmpty()) { 0 } else if (lapTimeList.size < 2) { 0 } else { lapTimeList.size - 1 }

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
                        fontSize = 14.sp,
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
                    //contentPadding = PaddingValues(
                    //    top = 16.dp,
                    //    start = 6.dp,
                    //    end = 6.dp,
                    //    bottom = 16.dp,
                    //),
                    //verticalArrangement = Arrangement.Top,
                    //horizontalAlignment = Alignment.Start,
                    state = listState
                ) {
                    this.item {
                        ListHeader(
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {
                            DetailRecordTitle(navController, dataItem.title, id, lapCount)
                        }
                    }
                    this.itemsIndexed(items = lapTimeList) { indexNo, lapTime ->
                        if (indexNo > 0)
                        {
                            LapTimeItem(navController, indexNo, lapTime, dataItem.startTime)
                        }
                    }
                }
            }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
        }
    }
}
