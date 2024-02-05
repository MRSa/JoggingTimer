package net.osdn.gokigen.joggingtimer.presentation.ui.main

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import kotlinx.coroutines.launch
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter
import net.osdn.gokigen.joggingtimer.utilities.IconIdProvider

///////////////////////////////////////////////////
//  サブのボタンエリアの描画＆制御
//   (JoggingTimer互換のボタン)
///////////////////////////////////////////////////

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
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = {
                // UIスレッドで実行が必要、ボタンを押すと設定基準値を切り替える
                referenceId.intValue = if (referenceId.intValue >= 2) { 0 } else { (referenceId.intValue + 1) }
                AppSingleton.controller.setReferenceTimerSelection(referenceId.intValue)
                AppSingleton.controller.vibrate(75)
            },
            enabled = true,
        ) {
            Icon(
                painter = painterResource(id = IconIdProvider.getReferenceIconId(referenceId.intValue)),
                contentDescription = "ReferenceIcon",
                tint = Color.White
            )
        }

        ////////////////////  設定画面へ遷移  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = { navController.navigate("PreferenceScreen") },
            enabled = false,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_settings_24),
                contentDescription = "List",
                tint = Color.DarkGray // Color.White
            )
        }

        ////////////////////  カウンターのスタート  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = { counterManager.start() },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                contentDescription = "Start",
                tint = Color.White
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun BtnSubStart(counterManager: ITimerCounter, context: Context)
{
    val referenceId = remember { mutableIntStateOf(AppSingleton.controller.getReferenceTimerSelection()) }
    val interactionSource = remember { MutableInteractionSource() }
    val coroutineScope = rememberCoroutineScope()

    // スタート状態時のボタン
    Row(modifier = Modifier
        .padding(horizontal = 10.dp)
        .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        ////////////////////  リファレンスアイコン(長押しで変更)  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black)
                .combinedClickable(
                    enabled = true,
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
                    }
                ),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = {  },
            enabled = false,
        ) {
            Icon(
                painter = painterResource(id = IconIdProvider.getReferenceIconId(referenceId.intValue)),
                contentDescription = "ReferenceIcon",
                tint = Color.White
            )
        }

        ////////////////////  カウンターのストップ  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black)
                .combinedClickable(
                    enabled = true,
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
                    }
                ),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = { },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_stop_24),
                contentDescription = "Stop",
                tint = Color.White
            )
        }

        ////////////////////  パススルーする  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black)
                .combinedClickable(
                    enabled = true,
                    interactionSource = interactionSource,
                    indication = null,
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
                    }
                ),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = { },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_start_24),
                contentDescription = "Pass",
                tint = Color.White
            )
        }
    }
}

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
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = {
                // UIスレッドで実行が必要、ボタンは長押しで止まることを表示する
                referenceId.intValue = if (referenceId.intValue >= 2) { 0 } else { (referenceId.intValue + 1) }
                AppSingleton.controller.setReferenceTimerSelection(referenceId.intValue)
                AppSingleton.controller.vibrate(75)
            },
            enabled = true,
        ) {
            Icon(
                painter = painterResource(IconIdProvider.getReferenceIconId(referenceId.intValue)),
                contentDescription = "ReferenceIcon",
                tint = Color.White
            )
        }

        ////////////////////  設定画面へ遷移  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = { navController.navigate("PreferenceScreen") },
            enabled = false,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_settings_24),
                contentDescription = "Settings",
                tint = Color.DarkGray // Color.White
            )
        }

        ////////////////////  カウンターのスタート  ////////////////////
        Button(
            modifier = Modifier
                .height(48.dp)
                .width(48.dp)
                .padding(2.dp)
                .background(color = Color.Black),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.primaryButtonColors(backgroundColor =  Color.Black),
            onClick = { counterManager.start() },
            enabled = true
        ) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_play_arrow_24),
                contentDescription = "Start",
                tint = Color.White
            )
        }
    }
}
