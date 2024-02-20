package net.osdn.gokigen.joggingtimer.presentation.ui.preference

import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.wear.compose.foundation.lazy.rememberScalingLazyListState
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Switch
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.ToggleChip
import androidx.wear.compose.material.ToggleChipDefaults
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.scrollAway
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.presentation.theme.JoggingTimerTheme
import java.util.Locale

@Composable
fun PreferenceScreen(navController: NavHostController)
{
    JoggingTimerTheme {
        val focusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()
        val listState = rememberScalingLazyListState()
        val notifyReachLap = remember { mutableStateOf(false)}
        val lapGraphicMode = remember { mutableIntStateOf(0)}

        notifyReachLap.value = AppSingleton.controller.getNotifyReachedReferenceLap()
        lapGraphicMode.intValue = AppSingleton.controller.getLapGraphicMode()

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
        )
        {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .focusRequester(focusRequester)
                    .padding(top = 24.dp)
/*
                padding = PaddingValues(
                    top = 16.dp,
                    start = 6.dp,
                    end = 6.dp,
                    bottom = 16.dp,
                ),
 */
            )
            {
/*
                // ---------- 通知のオプションは搭載したが、、、バイブレーションが必ず成功しないようなので、無効化する
                ToggleChip(
                    label = {
                        Text(
                            stringResource(R.string.notify_reference_lap),
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    checked = notifyReachLap.value,
                    colors = ToggleChipDefaults.toggleChipColors(
                        uncheckedToggleControlColor = ToggleChipDefaults.SwitchUncheckedIconColor
                    ),
                    toggleControl = {
                        Switch(
                            checked = notifyReachLap.value,
                            enabled = true,
                        )
                    },
                    onCheckedChange = {
                        notifyReachLap.value = it
                        AppSingleton.controller.setNotifyReachedReferenceLap(it)
                                      },
                    appIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_notifications_active_24),
                            contentDescription = "notify",
                            modifier = Modifier
                                .size(16.dp)
                                .wrapContentSize(align = Alignment.Center),
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    enabled = true,
                )
*/
                Spacer(modifier = Modifier.padding(all = 1.dp))
                ToggleChip(
                    label = {
                        Text(
                            stringResource(R.string.show_graph_mode),
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    checked = (lapGraphicMode.intValue != 0),
                    colors = ToggleChipDefaults.toggleChipColors(
                        uncheckedToggleControlColor = ToggleChipDefaults.SwitchUncheckedIconColor
                    ),
                    toggleControl = {
                        Switch(
                            checked = (lapGraphicMode.intValue != 0),
                            enabled = true,
                        )
                    },
                    onCheckedChange = {
                        lapGraphicMode.intValue = if (it) { 1 } else { 0 }
                        AppSingleton.controller.setLapGraphicMode(lapGraphicMode.intValue)
                    },
                    appIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_auto_graph_24),
                            contentDescription = "notify",
                            modifier = Modifier
                                .size(16.dp)
                                .wrapContentSize(align = Alignment.Center),
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    enabled = true,
                )
            }
            LaunchedEffect(Unit) { focusRequester.requestFocus() }
        }
    }
}
