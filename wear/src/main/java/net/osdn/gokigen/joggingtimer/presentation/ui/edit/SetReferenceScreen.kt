package net.osdn.gokigen.joggingtimer.presentation.ui.edit

import android.text.format.DateFormat
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Icon
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
import net.osdn.gokigen.joggingtimer.utilities.IconIdProvider
import java.util.Locale

@Composable
fun SetReferenceScreen(navController: NavHostController, indexId: Int)
{
    val iconIdNormal = 0
    val iconIdEditable = 4
    val iconIdReferenceLow = 1
    val iconIdReferenceHigh = 3
    val iconIdReferenceA = 2
    val iconIdReferenceB = 1
    val iconIdReferenceC = 3
    val iconReferenceA = 0
    val iconReferenceB = 1
    val iconReferenceC = 2

    val dataItem = remember { AppSingleton.controller.getRecordItem(indexId) }
    val normalIconId = if (AppSingleton.controller.isEditableRecord(indexId)) { iconIdEditable } else { iconIdNormal }
    val iconId = if ((dataItem.iconId == iconIdNormal)&&(normalIconId == iconIdEditable)) { iconIdEditable } else if ((dataItem.iconId >= iconIdReferenceLow)&&(dataItem.iconId <= iconIdReferenceHigh)) { normalIconId } else { dataItem.iconId }

    JoggingTimerTheme {
        val focusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        val horizontalPadding = 5.dp
        val isEditIcon = remember { mutableStateOf(false) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)
                .padding(paddingValues = PaddingValues(2.dp)),
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
                        .padding(5.dp)
                        .onRotaryScrollEvent {
                            coroutineScope.launch {
                                scrollState.scrollBy(it.verticalScrollPixels)
                                scrollState.animateScrollBy(0f)
                            }
                            true
                        }
                        .fillMaxWidth()
                        .verticalScroll(scrollState)
                        .padding(
                            horizontal = horizontalPadding,
                            vertical = 28.dp
                        )  // 20.dp -> 26.dp
                        .focusRequester(focusRequester)
                        .focusable(),
                    verticalArrangement = Arrangement.Top,
                    horizontalAlignment = Alignment.Start,
                ) {
                    Text(
                        // タイトル表示 （記録編集）
                        text = stringResource(id = R.string.action_set_reference),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.padding(1.dp))

                    Spacer(modifier = Modifier.padding(1.dp))

                    Icon(
                        painter = painterResource(id = IconIdProvider.getIconResourceId(iconId)),
                        contentDescription = "Icon",
                        tint = if (isEditIcon.value) { MaterialTheme.colors.primary } else { Color.White },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.DarkGray)
                            .clickable(onClick = {
                                coroutineScope.launch {
                                    AppSingleton.controller.updateIndexRecord(indexId, dataItem.title, iconId)
                                    AppSingleton.controller.setupReferenceData()
                                }
                                navController.popBackStack()  // 前の画面に戻る
                            })
                    )
                    Spacer(modifier = Modifier.padding(1.dp))
                    Icon(
                        painter = painterResource(id = IconIdProvider.getIconResourceId(iconIdReferenceA)),
                        contentDescription = "Reference-A",
                        tint = if (isEditIcon.value) { MaterialTheme.colors.primary } else { Color.White },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.DarkGray)
                            .clickable(onClick = {
                                coroutineScope.launch {
                                    AppSingleton.controller.setReferenceIconId(indexId, iconReferenceA)
                                    AppSingleton.controller.setupReferenceData()
                                }
                                navController.popBackStack()  // 前の画面に戻る
                            })
                    )
                    Spacer(modifier = Modifier.padding(1.dp))
                    Icon(
                        painter = painterResource(id = IconIdProvider.getIconResourceId(iconIdReferenceB)),
                        contentDescription = "Reference-B",
                        tint = if (isEditIcon.value) { MaterialTheme.colors.primary } else { Color.White },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.DarkGray)
                            .clickable(onClick = {
                                coroutineScope.launch {
                                    AppSingleton.controller.setReferenceIconId(indexId, iconReferenceB)
                                    AppSingleton.controller.setupReferenceData()
                                }
                                navController.popBackStack()  // 前の画面に戻る
                            })
                    )
                    Spacer(modifier = Modifier.padding(1.dp))
                    Icon(
                        painter = painterResource(id = IconIdProvider.getIconResourceId(iconIdReferenceC)),
                        contentDescription = "Reference-C",
                        tint = if (isEditIcon.value) { MaterialTheme.colors.primary } else { Color.White },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.DarkGray)
                            .clickable(onClick = {
                                coroutineScope.launch {
                                    AppSingleton.controller.setReferenceIconId(indexId, iconReferenceC)
                                    AppSingleton.controller.setupReferenceData()
                                }
                                navController.popBackStack()  // 前の画面に戻る
                            })
                    )
                    Spacer(modifier = Modifier.padding(1.dp))
                    //Divider(color = Color.DarkGray, thickness = 1.dp)
                }
            }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}
