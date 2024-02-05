package net.osdn.gokigen.joggingtimer.presentation.ui.list

import android.text.format.DateFormat
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
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
fun ResultListScreen(navController: NavHostController)
{
    val recordList = AppSingleton.controller.getCounterRecordList()
    JoggingTimerTheme {
        val focusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()
        val listState = rememberScalingLazyListState()
        Scaffold(
            timeText = { TimeText(
                timeSource = TimeTextDefaults.timeSource(
                    DateFormat.getBestDateTimePattern(
                        Locale.getDefault(),
                        "HH:mm"
                    ),
                ),
                modifier = Modifier.scrollAway(scrollState = listState)
            ) },
            vignette = {
                Vignette(vignettePosition = VignettePosition.TopAndBottom)
            },
            positionIndicator = {
                PositionIndicator(scalingLazyListState = listState)
            },
        ) {
            if (recordList.isEmpty())
            {
                // recorded data is nothing...
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .focusRequester(focusRequester),
                )
                {
                    ResultListTitle(navController, stringResource(id = R.string.record_data_empty))
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
                        top = 16.dp,
                        start = 6.dp,
                        end = 6.dp,
                        bottom = 16.dp,
                    ),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                    state = listState
                ) {
                    this.item {
                        ListHeader(
                            modifier = Modifier
                                .fillMaxSize(),
                        ) {
                            ResultListTitle(navController, "")
                        }
                    }
                    this.items(items = recordList) {
                            data -> ResultListItem(navController, data)
                    }
                }
            }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
        }
    }
}
