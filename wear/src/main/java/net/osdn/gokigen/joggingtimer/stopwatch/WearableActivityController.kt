package net.osdn.gokigen.joggingtimer.stopwatch

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.preference.PreferenceManager
import net.osdn.gokigen.joggingtimer.stopwatch.laptime.ILapTimeHolder
import net.osdn.gokigen.joggingtimer.stopwatch.laptime.LapTimeItems
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabaseCallback
import net.osdn.gokigen.joggingtimer.storage.TimeEntryDatabaseFactory
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryData

/**
 *
 *
 *
 */
class WearableActivityController : IWearableActivityControl, ITimeEntryDatabaseCallback, IDataEntry
{
    private val PREF_KEY_TIMER_STARTED = "TMR_START"
    private val PREF_KEY_TIMER_INDEXID = "TMR_INDEX"
    private var preferences: SharedPreferences? = null
    //private val clickListener = ButtonClickListener()
    private var database: ITimeEntryDatabase? = null
    private var dbCallback: IDatabaseReloadCallback? = null
    private var isReadyDatabase = false
    private var pendingLoadReference = false
    private var recordingIndexId: Long = -1
    private var lapTimeHolder: ILapTimeHolder? = null
    private var vibrator: Vibrator? = null
    //private PowerManager powerManager = null;

    init {
        Log.v(TAG, "WearableActivityController()")
    }

    override fun setup(
        activity: ComponentActivity,
        dbCallback: IDatabaseReloadCallback
    )
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        this.dbCallback = dbCallback
        setupHardwares(activity)
        setupScreen(activity)
        setupDatabase(activity, false); // change true if when database file should be cleared.
        //setupListeners(activity, callback)
    }

    /**
     *
     *
     */
    private fun setupHardwares(activity: ComponentActivity)
    {
        // バイブレータをつかまえる
        vibrator = activity.getSystemService(Vibrator::class.java)

        // パワーマネージャをつかまえる
        //powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);
    }

    /**
     *
     *
     */
    private fun setupScreen(activity: ComponentActivity)
    {
        //val mTextView = activity.findViewById<TextView>(R.id.text)
        //mTextView?.setText(R.string.app_name)
        //val adapter = LapTimeArrayAdapter(activity.applicationContext, R.layout.column_laptime)
        //adapter.clearLapTime()
        //lapTimeHolder = adapter
        //val lapTimeArea = activity.findViewById<ListView>(R.id.laptime_list_area)
        //lapTimeArea.adapter = adapter
    }

    /**
     * データベースのセットアップ
     */
    override fun setupDatabase(activity: ComponentActivity, isReset: Boolean)
    {
        database = TimeEntryDatabaseFactory(activity, this).entryDatabase
        val thread = Thread {
            try
            {
                if (isReset)
                {
                    // 既存のデータベースを消去する場合、、、
                    TimeEntryDatabaseFactory.deleteDatabase(activity)
                }
                database?.prepare()
            }
            catch (e: Exception)
            {
                e.printStackTrace()
            }
        }
        thread.start()
    }

    /**
     * リスナのセットアップ
     */
/*
    private fun setupListeners(
        activity: AppCompatActivity,
        callback: IClickCallback
    ) {
        try {
            clickListener.setCallback(callback)
            val btn1 = activity.findViewById<ImageButton>(R.id.btn1)
            btn1.setOnClickListener(clickListener)
            btn1.setOnLongClickListener(clickListener)
            val btn2 = activity.findViewById<ImageButton>(R.id.btn2)
            btn2.setOnClickListener(clickListener)
            btn2.setOnLongClickListener(clickListener)
            val btn3 = activity.findViewById<ImageButton>(R.id.btn3)
            btn3.setOnClickListener(clickListener)
            btn3.setOnLongClickListener(clickListener)
            val main = activity.findViewById<TextView>(R.id.main_counter)
            main.setOnClickListener(clickListener)
            main.setOnLongClickListener(clickListener)
            val sub1 = activity.findViewById<TextView>(R.id.sub_counter1)
            sub1.setOnClickListener(clickListener)
            sub1.setOnLongClickListener(clickListener)
            val lap = activity.findViewById<ListView>(R.id.laptime_list_area)
            //lap.setOnClickListener(clickListener);
            lap.setOnLongClickListener(clickListener)
            //lap.setOnTouchListener(clickListener);
            val graphView: LapTimeGraphView = activity.findViewById(R.id.graph_area)
            graphView.setOnTouchListener(clickListener)
            graphView.setOnClickListener(clickListener)
            graphView.setOnLongClickListener(clickListener)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
*/

    /**
     *
     *
     */
    private fun closeDatabase()
    {
        Log.v(TAG, "closeDatabase()")
        val thread = Thread {
            // DBのクローズ実行
            if (isReadyDatabase)
            {
                isReadyDatabase = false
                try {
                    Log.v(TAG, "closeDatabase() EXECUTE...")
                    database!!.close()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }

    /**
     *
     *
     */
    override fun exitApplication(activity: ComponentActivity)
    {
        Log.v(TAG, "exitApplication()")
        closeDatabase()
    }

    override fun vibrate(duration: Int)
    {
        try
        {
            val hasVibrator = vibrator?.hasVibrator() ?: false
            if (vibrator == null || !hasVibrator)
            {
                return
            }

            val thread = Thread {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                {
                    vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
                }
                else
                {
                    @Suppress("DEPRECATION")
                    vibrator?.vibrate(duration.toLong())
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun getDataEntry(): IDataEntry
    {
        return this
    }

    override fun getDisplayMode(): Boolean
    {
        try
        {
            return preferences?.getBoolean(PREF_KEY_DISPLAY_LAPGRAPHIC, false)?: false
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return false
    }

    override fun timerStarted(isStarted: Boolean)
    {
        try
        {
            val editor = preferences?.edit()
            editor?.putBoolean(PREF_KEY_TIMER_STARTED, isStarted)
            editor?.apply()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun setDisplayMode(displayLapTime: Boolean)
    {
        try
        {
            val editor = preferences?.edit()
            editor?.putBoolean(PREF_KEY_DISPLAY_LAPGRAPHIC, displayLapTime)
            editor?.apply()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun addTimeStamp(count: Long, lapTime: Long, diffTime: Long)
    {
        if (lapTimeHolder != null)
        {
            lapTimeHolder?.addLapTime(LapTimeItems(count, lapTime, diffTime))
        }
    }

    override fun clearTimeStamp()
    {
        if (lapTimeHolder != null)
        {
            lapTimeHolder?.clearLapTime()
        }
    }

    override fun getLapTimeCount(): Int
    {
        var count = 0
        if (lapTimeHolder != null)
        {
            count = lapTimeHolder?.getLapTimeCount() ?: 0
        }
        return count
    }

    override fun getReferenceTimerSelection(): Int
    {
        try
        {
            return (preferences?.getInt(PREF_KEY_REFERENCE_TIME_SELECTION, 0) ?: 0)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return 0
    }

    override fun setReferenceTimerSelection(id: Int)
    {
        try
        {
            val editor = preferences?.edit()
            editor?.putInt(PREF_KEY_REFERENCE_TIME_SELECTION, id)
            editor?.apply()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun setupReferenceData() {
        try {
            pendingLoadReference = if (isReadyDatabase) {
                loadReferenceData()
                false
            } else {
                true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("Range")
    override fun prepareFinished(isReady: Boolean) {
        Log.v(TAG, "database prepareFinished() : $isReady")
        isReadyDatabase = isReady
        try
        {
            val isStarted = preferences?.getBoolean(PREF_KEY_TIMER_STARTED, false) ?: false
            recordingIndexId = preferences?.getLong(PREF_KEY_TIMER_INDEXID, -1) ?: -1L
            Log.v(TAG, "isStarted : $isStarted  indexId : $recordingIndexId")

            // load reference data
            if (pendingLoadReference)
            {
                loadReferenceData()
                pendingLoadReference = false
            }

            // load current lap time list
            if (isStarted && recordingIndexId >= 0 && isReadyDatabase)
            {
                val list = ArrayList<Long>()
                val cursor = database?.getAllDetailData(recordingIndexId)
                if (cursor != null)
                {
                    while (cursor.moveToNext())
                    {
                        list.add(cursor.getLong(cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY)))
                    }
                }
                dbCallback?.dataIsReloaded(list)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    @SuppressLint("Range")
    private fun loadReferenceData()
    {
        // load reference data
        try
        {
            val id = getReferenceTimerSelection()
            val refList: ArrayList<Long> = ArrayList()
            val cursor = database?.getAllReferenceDetailData(id)
            if (cursor != null)
            {
                //refList = ArrayList()
                while (cursor.moveToNext())
                {
                    refList.add(cursor.getLong(cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY)))
                }
            }
            dbCallback?.referenceDataIsReloaded(id, refList)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun timeEntryFinished(
        operationType: ITimeEntryDatabaseCallback.OperationType,
        result: Boolean,
        indexId: Long,
        dataId: Long
    ) {
        Log.v(TAG, "database timeEntryFinished() : $result  [$indexId] $dataId")
    }

    override fun modelDataEntryFinished(
        operationType: ITimeEntryDatabaseCallback.OperationType,
        result: Boolean,
        indexId: Long,
        title: String
    ) {
        //
    }

    override fun dataEntryFinished(
        operationType: ITimeEntryDatabaseCallback.OperationType,
        result: Boolean,
        id: Long,
        title: String
    ) {
        Log.v(TAG, "database dataEntryFinished() : $result  [$id] $title")
        if (result && operationType == ITimeEntryDatabaseCallback.OperationType.CREATED) {
            setIndexId(id)
        }
    }

    private fun setIndexId(id: Long) {
        try {
            recordingIndexId = id
            val editor = preferences!!.edit()
            editor.putLong(PREF_KEY_TIMER_INDEXID, recordingIndexId)
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun createIndex(title: String, startTime: Long)
    {
        val memo = ""
        val icon = 0
        Log.v(TAG, "createIndex() $title $startTime")
        val thread = Thread {
            if (isReadyDatabase) {
                try {
                    database!!.createIndexData(title, memo, icon, startTime)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }

    override fun appendTimeData(elapsedTime: Long) {
        Log.v(TAG, "appendTimeData()  $elapsedTime")
        val thread = Thread {
            if (isReadyDatabase) {
                try {
                    database!!.appendTimeData(recordingIndexId, elapsedTime)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }

    override fun finishTimeData(startTime: Long, endTime: Long) {
        Log.v(TAG, "finishTimeData() $startTime $endTime")
        val thread = Thread {
            if (isReadyDatabase) {
                try {
                    database!!.finishTimeData(recordingIndexId, startTime, endTime)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }

    companion object
    {
        private val TAG = WearableActivityController::class.java.simpleName
        const val PREF_KEY_DISPLAY_LAPGRAPHIC = "DISP_LAPGRPH"
        const val PREF_KEY_REFERENCE_TIME_SELECTION = "REF_TIME_SEL"
    }
}
