package jp.sourceforge.gokigen

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import net.osdn.gokigen.joggingtimer.AppSingleton
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.importer.IntentSendImporter
import net.osdn.gokigen.joggingtimer.presentation.ui.main.ViewRoot
import net.osdn.gokigen.joggingtimer.stopwatch.timer.ICounterStatusNotify

class MainActivity : ComponentActivity(), ICounterStatusNotify
{
    private lateinit var rootComponent : ViewRoot
    private var pendingStart = false
    //private var stopTrigger: ITimerStopTrigger? = null

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        try
        {
            ///////// SHOW SPLASH SCREEN : call before super.onCreate() /////////
            installSplashScreen()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate()")

        setTheme(android.R.style.Theme_DeviceDefault)
        try
        {
            ///////// SET PERMISSIONS /////////
            if (!allPermissionsGranted())
            {
                val requestPermission = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
                    if(!allPermissionsGranted())
                    {
                        // Abort launch application because required permissions was rejected.
                        Toast.makeText(this, getString(R.string.permission_not_granted), Toast.LENGTH_SHORT).show()
                        Log.v(TAG, "----- APPLICATION LAUNCH ABORTED -----")
                        finish()
                    }
                }
                requestPermission.launch(REQUIRED_PERMISSIONS)
            }
            else
            {
                setupEnvironments()
            }
            rootComponent = ViewRoot(applicationContext)
            setContent {
                rootComponent.Content()
            }
        }
        catch (ex: Exception)
        {
            ex.printStackTrace()
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setupEnvironments()
    {
        try
        {
            AppSingleton.controller.setup(this, AppSingleton.timerCounter)
            createNotificationChannel()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    private fun createNotificationChannel()
    {
        try
        {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }


    private fun importReceivedIntent()
    {
        try
        {
            val thread = Thread {
                // 受信したSENDインテントを処理する
                IntentSendImporter(this.applicationContext, intent).start()
                val title = intent.getStringExtra(Intent.EXTRA_SUBJECT)
                runOnUiThread {
                    Toast.makeText(this, getString(R.string.data_imported) + title, Toast.LENGTH_SHORT).show()
                    //setResult(RESULT_OK, intent)
                    finish()
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     *
     */
    override fun onResume()
    {
        super.onResume()

        // インテントを取得する
        val intent = intent
        val action = intent.action
        Log.v(TAG, "onResume() : $action")
        var isStartTimer = false

        if (action != null)
        {
            try
            {
                if (Intent.ACTION_SEND == action)
                {
                    val flags = intent.flags
                    val checkFlags = FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY // + FLAG_ACTIVITY_BROUGHT_TO_FRONT
                    Log.v(TAG, "INTENT : $intent")
                    if ((flags.and(checkFlags)) == 0)
                    {
                        Log.v(TAG, " IMPORT DATA ")
                        importReceivedIntent()
                    }
                }
                else if (action == "com.google.android.wearable.action.STOPWATCH")
                {
                    isStartTimer = true
                }
                else if (action == "vnd.google.fitness.TRACK")
                {
                    if (intent.getStringExtra("actionStatus") == "ActiveActionStatus")
                    {
                        isStartTimer = true
                    }
                }
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
        onResumeAction(isStartTimer)
    }

    private fun onResumeAction(isStartTimer: Boolean)
    {
        try
        {
            AppSingleton.controller.setupReferenceData()
            if (isStartTimer)
            {
                pendingStart = true
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }



    /**
     *
     */
    override fun onPause()
    {
        super.onPause()
        Log.v(TAG, "onPause()")
    }

    /**
     *
     *
     */
    public override fun onStart()
    {
        super.onStart()
        Log.v(TAG, "onStart()")

        // データベースのセットアップ
        //AppSingleton.timerCounter.setCallback(this)
        //AppSingleton.controller.setupDatabase(this, false)
    }

    /**
     *
     *
     */
    public override fun onStop()
    {
        super.onStop()
        Log.v(TAG, "onStop()")
        //stopTrigger?.forceStop()
        //AppSingleton.controller.exitApplication(this)
    }

    override fun onUserLeaveHint()
    {
        Log.v(TAG, "onUserLeaveHint() ")
        // ハードキー（ホームボタン）が押されたとき、これがひろえるが...
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean
    {
        return super.onKeyDown(keyCode, event)
    }

    override fun counterStatusChanged(forceStartTimer: Boolean)
    {
        try
        {
            Log.v(TAG, "counterStatusChanged($forceStartTimer),  pendingStart:v$pendingStart")
            if (forceStartTimer)
            {
                AppSingleton.timerCounter.start()
            }
            runOnUiThread {
                if (pendingStart)
                {
                    AppSingleton.timerCounter.start()
                    pendingStart = false
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = MainActivity::class.java.simpleName
        const val CHANNEL_ID = "net.osdn.ja.gokigen.joggingtimer"
        private val REQUIRED_PERMISSIONS =
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                // Wear OS 4 以上
                arrayOf(
                    Manifest.permission.VIBRATE,
                    Manifest.permission.WAKE_LOCK,
                    Manifest.permission.POST_NOTIFICATIONS,
                )
            }
            else
            {
                // Wear OS 3 まで
                arrayOf(
                    Manifest.permission.VIBRATE,
                    Manifest.permission.WAKE_LOCK,
                )
            }
    }

}
