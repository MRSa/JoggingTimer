package net.osdn.gokigen.joggingtimer.stopwatch

import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_LAUNCHED_FROM_HISTORY
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.ImageButton
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.widget.NestedScrollView
import androidx.wear.ambient.AmbientModeSupport
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.recordlist.ListActivity
import net.osdn.gokigen.joggingtimer.stopwatch.MyTimerCounter.ICounterStatusNotify
import net.osdn.gokigen.joggingtimer.stopwatch.MyTimerTrigger.ITimeoutReceiver
import net.osdn.gokigen.joggingtimer.stopwatch.graphview.LapTimeGraphView
import net.osdn.gokigen.joggingtimer.utilities.SelectReferenceViewModeDialog
import net.osdn.gokigen.joggingtimer.utilities.SelectReferenceViewModeDialog.SelectReferenceCallback
import net.osdn.gokigen.joggingtimer.utilities.TimeStringConvert
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 *
 *
 */
class MainActivity : AppCompatActivity(), IClickCallback, ITimeoutReceiver, ICounterStatusNotify, AmbientModeSupport.AmbientCallbackProvider, SelectReferenceCallback
{
    private val controller: IWearableActivityControl = WearableActivityController()
    private val counter = MyTimerCounter()
    private var isCounterLapTime = true
    private var isLaptimeView = true
    private var pendingStart = false
    private var currentLapCount = 0
    private var currentReferenceId = 0
    private var stopTrigger: ITimerStopTrigger? = null

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

        try
        {
            setContentView(R.layout.activity_main)
            controller.setup(this, this, counter)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

        try
        {
            val ambientController = AmbientModeSupport.attach(this)
            ambientController.setAutoResumeEnabled(true)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }

    }

    override fun onNewIntent(intent: Intent?)
    {
        super.onNewIntent(intent)
        Log.v(TAG, "onNewIntent")
        runOnUiThread {
            Toast.makeText(this, "onNewIntent" + title, Toast.LENGTH_SHORT).show()
        }
    }

    private fun importReceivedIntent()
    {
        try
        {
            val thread = Thread {
                // 取得したSENDインテントを処理する
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

        isLaptimeView = controller.displayMode
        currentReferenceId = controller.referenceTimerSelection
        controller.setupReferenceData()
        if (isStartTimer)
        {
            pendingStart = true
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
        counter.setCallback(this)
        controller.setupDatabase(this, false)
    }

    /**
     *
     *
     */
    public override fun onStop()
    {
        super.onStop()
        Log.v(TAG, "onStop()")
        stopTrigger?.forceStop()
        controller.exitApplication(this)
    }

    /**
     *
     */
    private fun updateTimerLabel()
    {
        val timerCounter: ITimerCounter = counter
        val bgColor: Int
        val insetLayout = findViewById<NestedScrollView>(R.id.top_layout)
        val layout = findViewById<ConstraintLayout>(R.id.main_layout)
        val btn1 = findViewById<ImageButton>(R.id.btn1)
        val btn2 = findViewById<ImageButton>(R.id.btn2)
        val btn3 = findViewById<ImageButton>(R.id.btn3)
        updateMainSubCounter()
        if (timerCounter.isStarted)
        {
            bgColor = Color.BLACK
            insetLayout.setBackgroundColor(bgColor)
            insetLayout.invalidate()
            layout.setBackgroundColor(bgColor)
            layout.invalidate()
            btn1.setImageResource(R.drawable.ic_flag_black_24dp)
            btn1.setBackgroundColor(bgColor)

            // チャタリング防止（ラップタイムとして、３秒以内は記録しないようボタンを消しておく）
            val currentElapsedTime = timerCounter.currentElapsedTime
            btn1.visibility = if (currentElapsedTime > 3000) View.VISIBLE else View.INVISIBLE
            btn1.invalidate()
            btn2.setImageResource(R.drawable.ic_stop_black_24dp)
            btn2.setBackgroundColor(bgColor)
            btn2.visibility = View.VISIBLE
            btn2.invalidate()
            btn3.setImageResource(R.drawable.ic_block_black_24dp)
            btn3.setBackgroundColor(bgColor)
            btn3.visibility = View.INVISIBLE
            btn3.invalidate()
        }
        else if (timerCounter.isReset)
        {
            bgColor = Color.BLACK
            insetLayout.setBackgroundColor(bgColor)
            insetLayout.invalidate()
            layout.setBackgroundColor(bgColor)
            layout.invalidate()
            btn1.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            btn1.setBackgroundColor(bgColor)
            btn1.visibility = View.VISIBLE
            btn1.invalidate()
            btn2.setImageResource(R.drawable.ic_format_list_bulleted_black_24dp)
            btn2.setBackgroundColor(bgColor)
            btn2.visibility = View.VISIBLE
            btn2.invalidate()
            btn3.setImageResource(R.drawable.ic_refresh_black_24dp)
            btn3.setBackgroundColor(bgColor)
            btn3.visibility = View.INVISIBLE
            btn3.invalidate()
        }
        else
        {
            bgColor = Color.BLACK
            insetLayout.setBackgroundColor(bgColor)
            insetLayout.invalidate()
            layout.setBackgroundColor(bgColor)
            layout.invalidate()
            btn1.setImageResource(R.drawable.ic_play_arrow_black_24dp)
            btn1.visibility = View.VISIBLE
            btn1.setBackgroundColor(bgColor)
            btn1.invalidate()
            btn2.setImageResource(R.drawable.ic_format_list_bulleted_black_24dp)
            btn2.visibility = View.VISIBLE
            btn2.setBackgroundColor(bgColor)
            btn2.invalidate()
            btn3.setImageResource(R.drawable.ic_refresh_black_24dp)
            btn3.visibility = View.VISIBLE
            btn3.setBackgroundColor(bgColor)
            btn3.invalidate()
        }
        updateElapsedTimes()
    }

    override fun clickedCounter()
    {
        // 表示順番を変える
        isCounterLapTime = !isCounterLapTime
    }

    /**
     *
     */
    override fun clickedBtn1()
    {
        startTimer()
    }

    /**
     *
     *
     */
    private fun startTimer()
    {
        try
        {
            val timerCounter: ITimerCounter = counter
            val graphView = findViewById<LapTimeGraphView>(R.id.graph_area)
            if (timerCounter.isStarted)
            {
                Log.v(TAG, "startTimer() LAP TIME")
                val currentElapsedTime = timerCounter.currentElapsedTime
                if (currentElapsedTime > 3000) // チャタリング防止（ラップタイムとして、３秒以内は記録しないようにする）
                {
                    currentLapCount++
                    val lapTime = timerCounter.timeStamp()
                    val refLapTime = timerCounter.getReferenceLapTime(currentLapCount)
                    val diffTime = if (refLapTime == 0L) 0 else currentElapsedTime - refLapTime
                    controller.vibrate(50)
                    controller.dataEntry.appendTimeData(lapTime)
                    controller.addTimeStamp(currentLapCount.toLong(), currentElapsedTime, diffTime)
                    graphView?.notifyLapTime()
                }
            } else {
                Log.v(TAG, "startTimer() START")
                controller.clearTimeStamp()
                timerCounter.start()
                val trigger = MyTimerTrigger(this, 100, timerCounter)
                trigger.startTimer()
                currentLapCount = 0
                stopTrigger = trigger
                controller.timerStarted(true)
                controller.vibrate(120)
                val date = Date()
                val sdf1 = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                val title = sdf1.format(date)
                val startTime = timerCounter.startTime
                controller.dataEntry.createIndex(title, startTime)
                graphView?.notifyStarted(startTime)
            }
            updateTimerLabel()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     *
     *
     */
    private fun stopTimer(): Boolean
    {
        var ret = false
        try
        {
            val timerCounter: ITimerCounter = counter
            if (timerCounter.isStarted)
            {
                timerCounter.stop()
                controller.timerStarted(false)
                controller.vibrate(120)
                controller.dataEntry.finishTimeData(timerCounter.startTime, timerCounter.stopTime)
                val lapCount = currentLapCount + 1
                val currentElapsedTime = timerCounter.lastElapsedTime - timerCounter.getElapsedTime(currentLapCount)
                val refLapTime = timerCounter.getReferenceLapTime(lapCount)
                val diffTime = if (refLapTime == 0L) 0 else currentElapsedTime - refLapTime
                controller.addTimeStamp(lapCount.toLong(), currentElapsedTime, diffTime)
                ret = true
                val graphView = findViewById<LapTimeGraphView>(R.id.graph_area)
                graphView?.notifyStopped()
            }
            updateTimerLabel()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return ret
    }

    /**
     *
     */
    override fun clickedBtn2()
    {
        if (!(counter as ITimerCounter).isStarted)
        {
            // 停止中は、記録一覧を呼び出す
            launchListActivity()

            // ぶるぶる
            controller.vibrate(35)
        }
        updateTimerLabel()
    }

    /**
     *
     */
    override fun clickedBtn3()
    {
        val timerCounter: ITimerCounter = counter
        if (!timerCounter.isStarted)
        {
            timerCounter.reset()
            controller.vibrate(50)
            controller.clearTimeStamp()
            currentLapCount = 0
            val graphView = findViewById<LapTimeGraphView>(R.id.graph_area)
            graphView?.notifyReset()
        }
        updateTimerLabel()
    }

    override fun clickedArea()
    {
        Log.v(TAG, "clickedArea()")
    }

    override fun pushedBtn1(): Boolean
    {
        return false
    }

    override fun pushedBtn2(): Boolean
    {
        return stopTimer()
    }

    override fun pushedBtn3(): Boolean
    {
        return false
    }

    override fun pushedArea(): Boolean
    {
        try
        {
            // 基準値の設定ダイアログを表示する
            val preferences = androidx.preference.PreferenceManager.getDefaultSharedPreferences(
                applicationContext
            )
            val viewMode = preferences.getBoolean(
                SelectReferenceViewModeDialog.PREF_KEY_DISPLAY_LAPGRAPHIC,
                false
            )
            val selectionId = preferences.getInt(
                SelectReferenceViewModeDialog.PREF_KEY_REFERENCE_TIME_SELECTION,
                0
            )
            val callback: SelectReferenceCallback = this
            runOnUiThread {
                try {
                    // 基準値＆表示モード設定ダイアログを表示する
                    val dialog =
                        SelectReferenceViewModeDialog.newInstance(viewMode, selectionId, callback)
                    val manager = supportFragmentManager
                    dialog.show(manager, "dialog")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    /**
     *
     *
     */
    override fun timeout()
    {
        try
        {
            runOnUiThread { updateTimerLabel() }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     *
     *
     */
    private fun updateMainSubCounter()
    {
        val main = findViewById<TextView>(R.id.main_counter)
        val sub = findViewById<TextView>(R.id.sub_counter1)
        val timerCounter: ITimerCounter = counter
        val time1 = timerCounter.pastTime
        val str1 = TimeStringConvert.getTimeString(time1)
        var str2: CharSequence = ""
        if (timerCounter.isStarted)
        {
            val time2 = timerCounter.currentElapsedTime
            val lapCount = timerCounter.elapsedCount
            if (time2 >= 100 &&(lapCount > 1))
            {
                str2 = "[" + lapCount + "] " + TimeStringConvert.getTimeString(time2)
            }
        }
        if (str2.isNotEmpty() && this.isCounterLapTime)
        {
            // ラップタイムの方を大きく表示する
            main.text = str2
            sub.text = str1
        }
        else
        {
            main.text = str1
            sub.text = str2
        }
        main.invalidate()
        sub.invalidate()
    }

    /**
     *
     *
     */
    private fun updateElapsedTimes()
    {
        if (isLaptimeView)
        {
            updateElapsedTimesGraph()
        }
        else
        {
            updateElapsedTimesText()
        }
    }

    /**
     *
     *
     */
    private fun updateElapsedTimesGraph()
    {
        val view = findViewById<LapTimeGraphView>(R.id.graph_area)
        view.invalidate()
    }

    /**
     *
     *
     */
    private fun updateElapsedTimesText()
    {
        // Log.v(TAG, "updateElapsedTimesText()");
    }

    /**
     * Launch ListActivity
     *
     */
    private fun launchListActivity()
    {
        Log.v(TAG, "launchListActivity()")
        try
        {
            val intent = Intent(this, ListActivity::class.java)
            startActivity(intent)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     *
     *
     */
    override fun onUserLeaveHint()
    {
        Log.v(TAG, "onUserLeaveHint() ")
        // ハードキー（ホームボタン）が押されたとき、これがひろえるが...
    }

    /**
     *
     *
     */
    override fun dispatchKeyEvent(event: KeyEvent): Boolean
    {
        Log.v(TAG, "dispatchKeyEvent() : " + event.action + " (" + event.keyCode + ")")
        return super.dispatchKeyEvent(event)
    }

    /**
     *
     *
     */
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean
    {
        Log.v(TAG, "onKeyDown() : " + event.action + " (" + event.keyCode + ")" + keyCode)
        if (event.repeatCount == 0)
        {
            when (keyCode) {
                KeyEvent.KEYCODE_STEM_1 -> {
                    startTimer()
                    return true
                }
                KeyEvent.KEYCODE_STEM_2 -> {
                    startTimer()
                    return true
                }
                KeyEvent.KEYCODE_STEM_3 -> {
                    startTimer()
                    return true
                }
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    /**
     *
     *
     */
    override fun counterStatusChanged(forceStartTimer: Boolean)
    {
        if (forceStartTimer)
        {
            try
            {
                val graphView = findViewById<LapTimeGraphView>(R.id.graph_area)
                val trigger = MyTimerTrigger(this, 100, counter)
                trigger.startTimer()
                stopTrigger = trigger
                graphView?.notifyLapTime()
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
        runOnUiThread {
            // 自動スタート時の処理。。
            if (pendingStart) {
                startTimer()
                pendingStart = false
            }

            // ラップタイム表示状態の更新
            reloadLapTimeList(forceStartTimer)

            // 表示ビューの切り替え
            changeGraphicView(isLaptimeView)

            // 表示のボタン状態を変更
            updateTimerLabel()
        }
    }

    /**
     *
     *
     */
    private fun reloadLapTimeList(forceStartTimer: Boolean)
    {
        if (!forceStartTimer)
        {
            return
        }

        // Adapter と TimerCounterの整合性を確認
        try
        {
            val lapTimeList: List<Long> = (counter as ITimerCounter).lapTimeList
            val lapCount = lapTimeList.size
            val listCount = controller.lapTimeCount
            if (lapCount != listCount)
            {
                Log.v(TAG, "LAP COUNT IS MISMATCH!!! lap:$lapCount vs list:$listCount")
                var index = 0
                controller.clearTimeStamp()
                var prevTime = lapTimeList[0]
                for (lapTime in lapTimeList)
                {
                    index++
                    if (prevTime != lapTime)
                    {
                        val refLapTime = counter.getReferenceLapTime(index - 1)
                        val curLapTime = lapTime - prevTime
                        val calcRefLapTime = if (refLapTime == 0L) 0 else curLapTime - refLapTime
                        controller.addTimeStamp((index - 1).toLong(), curLapTime, calcRefLapTime)
                    }
                    prevTime = lapTime
                }
                currentLapCount = lapCount - 1
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     *
     *
     */
    private fun changeGraphicView(isGraphics: Boolean)
    {
        try
        {
            val graphView = findViewById<LapTimeGraphView>(R.id.graph_area)
            val listView = findViewById<ListView>(R.id.laptime_list_area)
            if (isGraphics)
            {
                graphView.setITimerCounter(counter)
                graphView.visibility = View.VISIBLE
                listView.visibility = View.INVISIBLE
            }
            else
            {
                graphView.visibility = View.INVISIBLE
                listView.visibility = View.VISIBLE
            }
            //controller.vibrate(30);
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback
    {
        return object : AmbientModeSupport.AmbientCallback() {
            override fun onEnterAmbient(ambientDetails: Bundle) {
                Log.v(TAG, "onEnterAmbient()")
            }
        }
    }

    override fun selectedReferenceViewMode(referenceId: Int, viewMode: Int)
    {
        isLaptimeView = viewMode != 0
        currentReferenceId = referenceId
        controller.displayMode = isLaptimeView
        controller.referenceTimerSelection = currentReferenceId
        controller.setupReferenceData()
        Log.v(TAG, "pushedArea() : $isLaptimeView REF: $currentReferenceId")
        runOnUiThread {
            // ラップタイム表示状態の更新
            reloadLapTimeList(true)

            // 表示ビューの切り替え
            changeGraphicView(isLaptimeView)

            // 表示のボタン状態を変更
            updateTimerLabel()
        }
    }

    companion object
    {
        private val TAG = MainActivity::class.java.simpleName
        //private const val REQUEST_CODE_PERMISSIONS = 10
        //private val REQUIRED_PERMISSIONS = arrayOf(
        //    Manifest.permission.VIBRATE,
        //    Manifest.permission.WAKE_LOCK,
        //)
    }
}
