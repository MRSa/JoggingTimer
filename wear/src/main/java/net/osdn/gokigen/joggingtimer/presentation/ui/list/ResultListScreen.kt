package net.osdn.gokigen.joggingtimer.presentation.ui.list

import android.text.format.DateFormat
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Divider
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
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
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
            positionIndicator = {
                PositionIndicator(scalingLazyListState = listState)
            },
        ) {
            ScalingLazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .onRotaryScrollEvent {
                        coroutineScope.launch {
                            listState.scrollBy(it.verticalScrollPixels)
                        }
                        true
                    }
                    .focusRequester(focusRequester)
                    .focusable(),
/*
                contentPadding = PaddingValues(
                    top = 32.dp,
                    start = 16.dp,
                    end = 16.dp,
                    bottom = 32.dp,
                ),
*/
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.Start,
                state = listState
            ) {
                this.items(items = recordList) {
                    data -> ShowDrawItem(data)
                }
            }
            if (recordList.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                )
                {
                    Text(
                        text = stringResource(id = R.string.record_data_empty),
                        color = MaterialTheme.colors.primary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
        }
    }
}

@Composable
fun ShowDrawItem(dataItem: ResultListItems)
{
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = dataItem.title,
            fontSize = 14.sp,
        )
        Divider()
    }
}
