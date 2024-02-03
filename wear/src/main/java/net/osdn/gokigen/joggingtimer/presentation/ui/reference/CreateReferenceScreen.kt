package net.osdn.gokigen.joggingtimer.presentation.ui.reference

import android.content.Context
import android.text.format.DateFormat
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Chip
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.scrollAway
import com.google.android.horologist.composables.TimePicker
import kotlinx.coroutines.launch
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.R
import net.osdn.ja.gokigen.wearos.timerapp.counter.TimeStringConvert
import java.time.LocalTime
import java.util.Locale

@Composable
fun CreateReferenceScreen(context: Context, navController: NavHostController)
{
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState() // remember { MyPositionIndicatorState() }
    val horizontalPadding = 5.dp

    val editTotalTime = remember { mutableStateOf(false) }
    val totalTime = remember { mutableLongStateOf(0L) }
    val totalLapCount = remember { mutableIntStateOf(1) }

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
                        scrollState.scrollBy(it.verticalScrollPixels)
                        scrollState.animateScrollBy(0f)
                    }
                    true
                }
                .fillMaxWidth()
                .verticalScroll(scrollState)
                .padding(horizontal = horizontalPadding, vertical = 20.dp)
                .focusRequester(focusRequester)
                .focusable(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                text = stringResource(R.string.action_create_model)
            )
/**/
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                text = stringResource(R.string.information_time_picker)
            )
/**/

            Row()
            {
                Button(
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                        .padding(0.dp)
                        .background(color = Color.Black),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.primaryButtonColors(backgroundColor = Color.Black),
                    onClick = {
                        if (totalLapCount.intValue <= 1) {
                            totalLapCount.intValue = 1
                        }
                        else
                        {
                            totalLapCount.intValue -= 1
                        }
                    },
                    enabled = true
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sharp_remove_24),
                        contentDescription = "Minus",
                        tint = Color.LightGray
                    )
                }
                Button(
                    modifier = Modifier
                        .width(96.dp)
                        .height(48.dp)
                        .padding(0.dp)
                        .background(color = Color.Black),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.primaryButtonColors(backgroundColor = Color.Black),
                    onClick = {

                    },
                    enabled = true
                ) {
                    Text(
                        text = "${stringResource(id = R.string.create_total_lap_label)}: ${totalLapCount.intValue}",
                        textDecoration = TextDecoration.None, //TextDecoration.Underline,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Button(
                    modifier = Modifier
                        .width(48.dp)
                        .height(48.dp)
                        .padding(0.dp)
                        .background(color = Color.Black),
                    shape = RoundedCornerShape(5.dp),
                    colors = ButtonDefaults.primaryButtonColors(backgroundColor = Color.Black),
                    onClick = {
                        if (totalLapCount.intValue >= 150) {
                            totalLapCount.intValue = 150
                        }
                        else
                        {
                            totalLapCount.intValue += 1
                        }
                    },
                    enabled = true
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.sharp_add_24),
                        contentDescription = "Plus",
                        tint = Color.LightGray
                    )
                }
            }

            val timeString = TimeStringConvert.getTimeString(totalTime.longValue)
            Log.v("Ref", "timeString: $timeString : ${totalTime.longValue}")
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(0.dp)
                    .background(color = Color.Black),
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
                onClick = {
                    editTotalTime.value = true
                },
                enabled = true
            ) {
                Text(
                    text = "${stringResource(id = R.string.create_total_time_label)}: $timeString",
                    textDecoration = TextDecoration.Underline
                )
            }

            // ------ モデル作成ボタン
            val canCreate = (totalTime.longValue > 1000)&&(totalLapCount.intValue > 1)
            Chip(
                modifier = Modifier
                    .height(48.dp)
                    .width(96.dp)
                    .background(color = Color.Black)
                    .padding(paddingValues = PaddingValues(top = 6.dp)),
                onClick = {
                    // モデルデータの作成
                    coroutineScope.launch {
                        AppSingleton.controller.createTimeEntryModelData(totalLapCount.intValue, totalTime.longValue, "") // ダミーで固定
                    }
                    Toast.makeText(context, context.getString(R.string.created_model_data), Toast.LENGTH_SHORT).show()
                    navController.popBackStack()  // 前の画面に戻る
                },
                enabled = canCreate,
                label = {
                    Text(
                        text = stringResource(id = R.string.create_model),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center,
                        color = Color.Black,
                        modifier = Modifier.fillMaxWidth()
                    )
                },
            )
        }
    }
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

    if (editTotalTime.value)
    {
        // ミリ秒をナノ秒に変換して、LocalTimeの初期値として与える
        val initialTime = LocalTime.ofNanoOfDay(totalTime.longValue * 1000000L)

        // ----- 時刻入力
        TimePicker(
            onTimeConfirm = {
                totalTime.longValue = (it.hour * 60L * 60L + it.minute * 60L + it.second) * 1000L  // ミリ秒に変更
                editTotalTime.value = false
                Log.v("TimePick", "Time Data Input : ${totalTime.longValue}")
            },
            modifier = Modifier
                .fillMaxSize()
                .background(color = Color.Black)
                .padding(start = 8.dp),
            time = initialTime
        )
    }
}
