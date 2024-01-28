package net.osdn.gokigen.joggingtimer.presentation.ui.edit

import android.content.Context
import android.text.format.DateFormat
import android.widget.Toast
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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.TextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.ChipDefaults
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
fun RecordEditScreen(context: Context, navController: NavHostController, indexId: Int)
{
    val dataItem = remember { AppSingleton.controller.getRecordItem(indexId) }

    JoggingTimerTheme {
        val focusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()
        val scrollState = rememberScrollState()
        val horizontalPadding = 5.dp
        val isEditIcon = remember { mutableStateOf(false) }
        val iconSelectionExpanded = remember { mutableStateOf(false) }
        val selectedIndex = remember { mutableIntStateOf(dataItem.iconId) }
        val titleData = remember { mutableStateOf(dataItem.title) }
        val drawableIconId = IconIdProvider.getIconResourceId(selectedIndex.intValue)

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
                                //Log.v("TEST", "Pixels: ${it.verticalScrollPixels}")
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
                        text = stringResource(id = R.string.action_edit_title),
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        fontSize = 14.sp,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Icon(
                        painter = painterResource(id = drawableIconId),
                        contentDescription = "Icon",
                        tint = if (isEditIcon.value) { MaterialTheme.colors.primary } else { Color.White },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.DarkGray)
                            .clickable(onClick = { iconSelectionExpanded.value = true })
                    )
                    Spacer(modifier = Modifier.padding(1.dp))
                    DropdownMenu(
                        expanded = iconSelectionExpanded.value,
                        onDismissRequest = { iconSelectionExpanded.value = false },
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black)
                            .padding(start = 10.dp),
                    ) {
                        IconIdProvider.getIconIdList().forEachIndexed { index, iconId ->
                            if ((index == 0) || (index >= 5)) {
                                DropdownMenuItem(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(
                                            if (selectedIndex.intValue == index) {
                                                Color.DarkGray
                                            } else {
                                                Color.Black
                                            }
                                        ),
                                    onClick = {
                                        selectedIndex.intValue = index
                                        iconSelectionExpanded.value = false
                                    },
                                ) {
                                    Icon(
                                        painter = painterResource(
                                            id = IconIdProvider.getIconResourceId(
                                                index
                                            )
                                        ),
                                        contentDescription = "Icon",
                                        tint = if (selectedIndex.intValue == index) {
                                            MaterialTheme.colors.primary
                                        } else {
                                            Color.White
                                        },
                                        modifier = Modifier
                                            .background(
                                                if (selectedIndex.intValue == index) {
                                                    Color.DarkGray
                                                } else {
                                                    Color.Black
                                                }
                                            )
                                    )
                                }
                            }
                        }
                    }
                    TextField(
                        value = titleData.value,
                        onValueChange = { titleData.value = it },
                        textStyle = TextStyle(
                            color = MaterialTheme.colors.primary,
                            textDecoration = TextDecoration.Underline
                        ),
                        modifier = Modifier
                            .background(Color.DarkGray)
                            .fillMaxWidth(),
                        maxLines = 2
                    )

                    //Divider(color = Color.DarkGray, thickness = 1.dp)

                    Chip(
                        // OKボタン。ボタンが押されたときに更新する。
                        modifier = Modifier
                            //.fillMaxSize()
                            .height(48.dp)
                            .width(96.dp)
                            .background(color = Color.Black)
                            .padding(paddingValues = PaddingValues(4.dp))
                            .align(alignment = Alignment.CenterHorizontally),
                        label = {
                            Text(
                                text = stringResource(id = R.string.dialog_positive_execute),
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) },
                        onClick = {
                            AppSingleton.controller.updateRecord(indexId, titleData.value, iconId = selectedIndex.intValue)
                            Toast.makeText(context, context.getString(R.string.action_edited_data), Toast.LENGTH_SHORT).show()
                            navController.popBackStack()  // 前の画面に戻る
                        },
                        colors = ChipDefaults.primaryChipColors(),
                    )
                }
            }
            LaunchedEffect(Unit) {
                focusRequester.requestFocus()
            }
        }
    }
}