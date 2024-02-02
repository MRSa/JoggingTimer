package net.osdn.gokigen.joggingtimer.presentation.ui.main

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.AbstractComposeView
import androidx.wear.compose.navigation.rememberSwipeDismissableNavController
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.presentation.theme.JoggingTimerTheme

class ViewRoot @JvmOverloads constructor(appContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : AbstractComposeView(appContext, attrs, defStyleAttr)
{
    private val appContext: Context
    init {
        this.appContext = appContext
    }

    @Composable
    override fun Content()
    {
        val navController = rememberSwipeDismissableNavController()
        val counterModel = remember { AppSingleton.timerCounter }

        JoggingTimerTheme {
            NavigationMain(appContext, navController, counterModel)
        }
        Log.v(TAG, " ... ViewRoot ...")
    }
    companion object
    {
        private val TAG = ViewRoot::class.java.simpleName
    }
}
