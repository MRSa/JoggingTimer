package net.osdn.gokigen.joggingtimer.presentation.ui.list

import android.text.format.DateFormat
import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
import androidx.wear.compose.material.Icon
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
import net.osdn.gokigen.joggingtimer.utilities.IconIdProvider
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ShowDrawItem(dataItem: ResultListItems)
{
    val showDialog = remember { mutableStateOf(false) }

    val iconId = IconIdProvider.getIconResourceId(dataItem.iconId)
    val totalTime = TimeStringConvert.getTimeString(dataItem.duration)
    Column(
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                enabled = true,
                onClick = {
                    Log.v("LIST", "select Item (id: ${dataItem.indexId} title: ${dataItem.title})")
                },
                onLongClick = {
                    Log.v("LIST", "LONG CLICK (id: ${dataItem.indexId} title: ${dataItem.title})")
                    showDialog.value = true
                },
            )
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Chip(
            //modifier = Modifier
            //    .wrapContentSize(align = Alignment.TopStart),
            modifier = Modifier
                .fillMaxSize()
                .combinedClickable(
                    enabled = true,
                    onClick = {
                        Log.v("LIST", "select Item (_id: ${dataItem.indexId} title: ${dataItem.title})")
                    },
                    onLongClick = {
                        Log.v("LIST", "LONG CLICK (_id: ${dataItem.indexId} title: ${dataItem.title})")
                        showDialog.value = true
                    },
                )
                .background(color = Color.Black),
            onClick = {Log.v("Chip", "onClick()") },
            enabled = true,
            label = {
                Text(
                    text = dataItem.title,
                    fontSize = 12.sp,
                    //maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            },
            icon = {
                Icon(
                    painter = painterResource(id = iconId),
                    contentDescription = "icon",
                    modifier = Modifier.size(ChipDefaults.IconSize)
                        .wrapContentSize(align = Alignment.Center),
                )
            },
            secondaryLabel = {
                Text(
                    text = totalTime.toString(),
                    fontSize = 10.sp,
                    overflow = TextOverflow.Ellipsis
                )
            }
        )
        Divider()
    }

/*
    Chip(
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                enabled = true,
                onClick = {
                    Log.v("LIST", "select Item (id: ${dataItem.indexId} title: ${dataItem.title})")
                },
                onLongClick = {
                    Log.v("LIST", "LONG CLICK (id: ${dataItem.indexId} title: ${dataItem.title})")
                    showDialog.value = true
                },
            ),
        contentPadding = PaddingValues(5.dp),
        onClick = { },
        enabled = true,
        label = { Text(
            text = dataItem.title,
            fontSize = 12.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        ) },
        icon = { Icon(
            painter = painterResource(id = iconId),
            contentDescription = "icon",
            modifier = Modifier.size(ChipDefaults.IconSize)
                .wrapContentSize(align = Alignment.Center),
        ) },
        secondaryLabel = { Text(
            text = totalTime.toString(),
            fontSize = 10.sp,
            overflow = TextOverflow.Ellipsis
        )}
    )
    if (showDialog.value)
    {
        Log.v("LIST", " - - - SHOW DIALOG - - - ")
        DataDeleteConfirm(dataItem = dataItem)
    }

 */
}


@Composable
private fun DataDeleteConfirm(dataItem: ResultListItems)
{
    Text(
        text = dataItem.title,
        fontSize = 14.sp,
        color = Color.Gray
    )
    Log.v("DIALOG", "${dataItem.title}, ${dataItem.memo} ${dataItem.iconId}")

/*
    Alert(
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.ic_warning_black_24dp),
                contentDescription = "warning",
                //modifier = Modifier.size(24.dp).wrapContentSize(align = Alignment.Center),
            )
        },
        title = {
            Text(
                text = stringResource(id = R.string.dialog_message_delete) + " : ",
                textAlign = TextAlign.Center
            )
        },
        negativeButton = { Button(
            colors = ButtonDefaults.secondaryButtonColors(),
            onClick = {
                /* Do something e.g. navController.popBackStack()*/
            }) {
            Text("No")
        } },
        positiveButton = { Button(onClick = {
            /* Do something e.g. navController.popBackStack()*/
        }) { Text("Yes") } },
        contentPadding = PaddingValues(start = 10.dp, end = 10.dp, top = 24.dp, bottom = 32.dp),
    )
 */
}
