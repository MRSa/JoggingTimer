package net.osdn.gokigen.joggingtimer.presentation.ui.reference

import android.content.Context
import android.text.format.DateFormat
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
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
import androidx.wear.compose.material.Chip
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
import java.util.Locale

@Composable
fun CreateReferenceScreen(context: Context, navController: NavHostController)
{
    val focusRequester = remember { FocusRequester() }
    val coroutineScope = rememberCoroutineScope()
    val scrollState = rememberScrollState() // remember { MyPositionIndicatorState() }
    val horizontalPadding = 5.dp

    val totalTime = remember { mutableLongStateOf(0L) }
    val totalLapCount = remember { mutableIntStateOf(0) }

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
                .padding(horizontal = horizontalPadding, vertical = 28.dp)
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
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                text = stringResource(R.string.information_time_picker)
            )



            // ------ モデル作成ボタン
            Chip(
                modifier = Modifier
                    .height(48.dp)
                    .width(96.dp)
                    .background(color = Color.Black)
                    .padding(paddingValues = PaddingValues(top = 6.dp)),
                onClick = {
                    // ダミー値を入れる
                    totalLapCount.intValue = 25
                    totalTime.longValue = 200000L

                    // モデルデータの作成
                    coroutineScope.launch {
                        AppSingleton.controller.createTimeEntryModelData(totalLapCount.intValue, totalTime.longValue, "") // ダミーで固定
                    }
                    Toast.makeText(context, context.getString(R.string.created_model_data), Toast.LENGTH_SHORT).show()
                    navController.popBackStack()  // 前の画面に戻る
                },
                enabled = true,
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
}
