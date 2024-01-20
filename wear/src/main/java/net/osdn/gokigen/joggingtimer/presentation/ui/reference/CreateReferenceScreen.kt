package net.osdn.gokigen.joggingtimer.presentation.ui.reference

import android.content.Context
import android.text.format.DateFormat
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import net.osdn.gokigen.joggingtimer.R
import java.util.Locale

@Composable
fun CreateReferenceScreen(context: Context, navController: NavHostController)
{
    Scaffold(
        timeText = {
            TimeText(
                timeSource = TimeTextDefaults.timeSource(
                    DateFormat.getBestDateTimePattern(
                        Locale.getDefault(),
                        "HH:mm"
                    ),
                ),
            )
        },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    PaddingValues(
                        top = 25.dp
                    )
                ),
            //verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colors.primary,
                fontSize = 12.sp,
                text = stringResource(R.string.action_create_model)
            )
        }
    }
}
