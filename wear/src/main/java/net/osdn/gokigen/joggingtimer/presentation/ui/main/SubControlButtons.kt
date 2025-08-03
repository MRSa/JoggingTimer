package net.osdn.gokigen.joggingtimer.presentation.ui.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.google.android.horologist.annotations.ExperimentalHorologistApi
import com.google.android.horologist.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import kotlinx.coroutines.launch
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter
import net.osdn.gokigen.joggingtimer.utilities.IconIdProvider

///////////////////////////////////////////////////
//  サブのボタンエリアの描画＆制御
//   (JoggingTimer互換のボタン)
///////////////////////////////////////////////////

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun BtnSubStop(navController: NavHostController, counterManager: ITimerCounter)
{
    val referenceId = remember { mutableIntStateOf(AppSingleton.controller.getReferenceTimerSelection()) }

    // ストップ状態時のボタン
    Row(modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ////////////////////  リファレンスアイコン(操作アイコン)  ////////////////////
        Button(
            id = IconIdProvider.getReferenceIconId(referenceId.intValue),
            contentDescription = "ReferenceIcon",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = {
                // UIスレッドで実行が必要、ボタンを押すと設定基準値を切り替える
                referenceId.intValue = if (referenceId.intValue >= 2) { 0 } else { (referenceId.intValue + 1) }
                AppSingleton.controller.setReferenceTimerSelection(referenceId.intValue)
                AppSingleton.controller.vibrate(75)
            },
            enabled = true,
        )

        ////////////////////  設定画面へ遷移  ////////////////////
        Button(
            id = R.drawable.baseline_settings_24,
            contentDescription = "Settings",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = { navController.navigate("PreferenceScreen") },
            enabled = true,
        )

        ////////////////////  カウンターのスタート  ////////////////////
        Button(
            id = R.drawable.baseline_play_arrow_24,
            contentDescription = "Start",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = { counterManager.start() },
            enabled = true
        )
    }
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun BtnSubStart(counterManager: ITimerCounter, context: Context)
{
    val referenceId = remember { mutableIntStateOf(AppSingleton.controller.getReferenceTimerSelection()) }
    val coroutineScope = rememberCoroutineScope()

    // スタート状態時のボタン
    Row(modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ////////////////////  リファレンスアイコン(長押しで変更)  ////////////////////
        Button(
            id = IconIdProvider.getReferenceIconId(referenceId.intValue),
            contentDescription = "ReferenceIcon",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = {
                // UIスレッドで実行が必要、ボタンは長押しで止まることを表示する
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.long_press_to_change),
                        Toast.LENGTH_SHORT
                    )
                    .show()
            },
            onLongClick = {
                coroutineScope.launch {
                    referenceId.intValue = if (referenceId.intValue >= 2) { 0 } else { (referenceId.intValue + 1) }
                    AppSingleton.controller.setReferenceTimerSelection(referenceId.intValue)
                    AppSingleton.controller.setupReferenceData()
                    AppSingleton.controller.vibrate(75)
                }
            },
            enabled = true,
        )

        ////////////////////  カウンターのストップ  ////////////////////
        Button(
            id = R.drawable.baseline_stop_24,
            contentDescription = "Stop",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = {
                // UIスレッドで実行が必要、ボタンは長押しで止まることを表示する
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.long_press_to_stop),
                        Toast.LENGTH_SHORT
                    )
                    .show()
            },
            onLongClick = {
                Log.v("STOP", "STOP: onLongClick (2)")
                counterManager.stop()
            },
            enabled = true
        )

        ////////////////////  パススルーする  ////////////////////
        Button(
            id = R.drawable.baseline_start_24,
            contentDescription = "Pass",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = {
                // UIスレッドで実行が必要、ボタンは長押しで止まることを表示する
                Toast
                    .makeText(
                        context,
                        context.getString(R.string.long_press_to_pass),
                        Toast.LENGTH_SHORT
                    )
                    .show()
            },
            onLongClick = {
                Log.v("STOP", "PASS: onLongClick (2)")
                counterManager.timeStamp(true)
            },
            enabled = true
        )
    }
}

@OptIn(ExperimentalHorologistApi::class)
@Composable
fun BtnSubFinished(navController: NavHostController, counterManager: ITimerCounter)
{
    val referenceId = remember { mutableIntStateOf(AppSingleton.controller.getReferenceTimerSelection()) }

    // カウントストップ時のボタン
    Row(modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ////////////////////  リファレンスアイコン(操作アイコン)  ////////////////////
        Button(
            id = IconIdProvider.getReferenceIconId(referenceId.intValue),
            contentDescription = "ReferenceIcon",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = {
                // UIスレッドで実行が必要、ボタンは長押しで止まることを表示する
                referenceId.intValue = if (referenceId.intValue >= 2) { 0 } else { (referenceId.intValue + 1) }
                AppSingleton.controller.setReferenceTimerSelection(referenceId.intValue)
                AppSingleton.controller.vibrate(75)
            },
            enabled = true,
        )

        ////////////////////  設定画面へ遷移  ////////////////////
        Button(
            id = R.drawable.baseline_settings_24,
            contentDescription = "Settings",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = { navController.navigate("PreferenceScreen") },
            enabled = true,
        )

        ////////////////////  カウンターのスタート  ////////////////////
        Button(
            id = R.drawable.baseline_play_arrow_24,
            contentDescription = "Start",
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            colors = ButtonDefaults.primaryButtonColors(
                backgroundColor =  Color.Black,
                contentColor = Color.White
            ),
            onClick = { counterManager.start() },
            enabled = true
        )
    }
}
