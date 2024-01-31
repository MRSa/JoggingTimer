package net.osdn.gokigen.joggingtimer.presentation.ui.main

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.wear.compose.navigation.SwipeDismissableNavHost
import androidx.wear.compose.navigation.composable
import net.osdn.gokigen.joggingtimer.presentation.theme.JoggingTimerTheme
import net.osdn.gokigen.joggingtimer.presentation.ui.detail.DetailRecordScreen
import net.osdn.gokigen.joggingtimer.presentation.ui.edit.LapTimeEditScreen
import net.osdn.gokigen.joggingtimer.presentation.ui.edit.RecordEditScreen
import net.osdn.gokigen.joggingtimer.presentation.ui.edit.SetReferenceScreen
import net.osdn.gokigen.joggingtimer.presentation.ui.list.ResultListScreen
import net.osdn.gokigen.joggingtimer.presentation.ui.preference.PreferenceScreen
import net.osdn.gokigen.joggingtimer.presentation.ui.reference.CreateReferenceScreen
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ITimerCounter

@Composable
fun NavigationMain(context: Context, navController: NavHostController, counterManager: ITimerCounter)
{
    // ----- 画面の切り替え(ナビゲーション)部分 ------
    JoggingTimerTheme {
        SwipeDismissableNavHost(
            navController = navController,
            startDestination = "MainScreen" // "PreferenceScreen"
        ) {
            composable(
                route = "MainScreen"
            ) {
                // メイン画面
                MainScreen(context = context, navController = navController, counterManager = counterManager)
            }
            composable(
                route = "RecordListScreen"
            ) {
                // 記録一覧画面
                ResultListScreen(navController = navController)
            }
            composable(
                route = "PreferenceScreen"
            ) {
                // 設定画面
                PreferenceScreen(navController = navController)
            }
            composable(
                route = "CreateReferenceScreen"
            ) {
                // 基準値作成画面
                CreateReferenceScreen(context = context, navController = navController)
            }
            composable(
                route = "DetailRecordScreen/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType }
                )
            ) {
                // 記録詳細画面
                backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                DetailRecordScreen(context = context, navController = navController, id = id)
            }
            composable(
                route = "RecordEditScreen/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType }
                )
            ) {
                // 記録データの編集画面
                backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                RecordEditScreen(context = context, navController = navController, indexId = id)
            }
            composable(
                route = "SetReferenceScreen/{id}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType }
                )
            ) {
                // データの基準値設定画面
                backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                SetReferenceScreen(context = context, navController = navController, indexId = id)
            }
            composable(
                route = "LapTimeEditScreen/{id}/{lap}/{recordIndex}",
                arguments = listOf(
                    navArgument("id") { type = NavType.IntType },
                    navArgument("lap") { type = NavType.IntType },
                    navArgument("recordIndex") { type = NavType.LongType }
                )
            ) {
                // ラップタイムデータの編集画面
                backStackEntry ->
                val id = backStackEntry.arguments?.getInt("id") ?: 0
                val lap = backStackEntry.arguments?.getInt("lap") ?: 0
                val recordIndex = backStackEntry.arguments?.getLong("recordIndex") ?: 0L
                LapTimeEditScreen(navController = navController, indexId = id, lapIndex = lap, recordIndexId = recordIndex)
            }
        }
    }
}

