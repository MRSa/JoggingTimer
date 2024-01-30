package net.osdn.gokigen.joggingtimer.stopwatch

import android.Manifest
import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.VibrationEffect
import android.os.VibrationEffect.DEFAULT_AMPLITUDE
import android.os.Vibrator
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.runtime.mutableIntStateOf
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.preference.PreferenceManager
import androidx.wear.ongoing.OngoingActivity
import androidx.wear.ongoing.Status
import net.osdn.gokigen.joggingtimer.MainActivity
import net.osdn.gokigen.joggingtimer.MainActivity.Companion.CHANNEL_ID
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.ResultListData
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabaseCallback
import net.osdn.gokigen.joggingtimer.storage.TimeEntryDatabaseFactory
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryData
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryIndex

/**
 *
 *
 *
 */
class WearableActivityController : IWearableActivityControl, ITimeEntryDatabaseCallback, IDataEntry
{

    private var preferences: SharedPreferences? = null
    private var database: ITimeEntryDatabase? = null
    private var dbCallback: IDatabaseReloadCallback? = null
    private var isReadyDatabase = false
    private var pendingLoadReference = false
    private var recordingIndexId: Long = -1
    private var vibrator: Vibrator? = null
    private var currentReferenceId = mutableIntStateOf(0)
    private lateinit var myActivity : ComponentActivity
    //private PowerManager powerManager = null;

    //init {
    //    //Log.v(TAG, "WearableActivityController()")
    //    //currentReferenceId.intValue = preferences?.getInt(PREF_KEY_REFERENCE_TIME_SELECTION, 0) ?: 0
    //}

    override fun setup(
        activity: ComponentActivity,
        dbCallback: IDatabaseReloadCallback
    )
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity)
        try
        {
            currentReferenceId.intValue = preferences?.getInt(PREF_KEY_REFERENCE_TIME_SELECTION, 0) ?: 0
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        this.myActivity = activity
        this.dbCallback = dbCallback
        setupHardwares(activity)
        setupDatabase(activity, false) // change true if when database file should be cleared.
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
                    database?.close()
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
                    val effect = VibrationEffect.createOneShot(duration.toLong(), DEFAULT_AMPLITUDE)
                    vibrator?.vibrate(effect)
                    //vibrator?.vibrate(VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK))
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
/*
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
*/
    override fun getReferenceTimerSelection(): Int
    {
/*
        try
        {
            currentReferenceId = preferences?.getInt(PREF_KEY_REFERENCE_TIME_SELECTION, 0) ?: 0
            return (currentReferenceId)
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
*/
        return (currentReferenceId.intValue)
    }

    override fun setReferenceTimerSelection(id: Int)
    {
        try
        {
            currentReferenceId.intValue = id
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

    override fun getCounterRecordList(): List<ResultListData>
    {
        val recordList: ArrayList<ResultListData> = ArrayList()
        try
        {
            val cursor = database?.allIndexData
            if (cursor != null)
            {
                while (cursor.moveToNext())
                {
                    val indexIdNumber = cursor.getColumnIndex(TimeEntryIndex.EntryIndex._ID)
                    val titleNumber = cursor.getColumnIndex(TimeEntryIndex.EntryIndex.COLUMN_NAME_TITLE)
                    val memoNumber = cursor.getColumnIndex(TimeEntryIndex.EntryIndex.COLUMN_NAME_MEMO)
                    val iconIdNumber = cursor.getColumnIndex(TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID)
                    val startTimeNumber = cursor.getColumnIndex(TimeEntryIndex.EntryIndex.COLUMN_NAME_START_TIME)
                    val durationNumber = cursor.getColumnIndex(TimeEntryIndex.EntryIndex.COLUMN_NAME_TIME_DURATION)

                    val indexId = cursor.getLong(indexIdNumber)
                    val title =  cursor.getString(titleNumber)
                    val memo =  cursor.getString(memoNumber)
                    val iconId =  cursor.getInt(iconIdNumber)
                    val startTime = cursor.getLong(startTimeNumber)
                    val durationTime = cursor.getLong(durationNumber)

                    //Log.v(TAG, " Record ($indexId, $title, $memo, $iconId : [$startTime][$durationTime])")

                    recordList.add(ResultListData(indexId, title, memo, iconId, startTime, durationTime))
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (recordList)
    }

    override fun getRecordItem(id: Int): ResultListData
    {
        try
        {
            //Log.v(TAG, " GET Record ($id)")
            val cursor = database?.getIndexdata(id.toLong())
            if (cursor != null)
            {
                while (cursor.moveToNext())
                {
                    val indexIdNumber = cursor.getColumnIndex(TimeEntryIndex.EntryIndex._ID)
                    val titleNumber =
                        cursor.getColumnIndex(TimeEntryIndex.EntryIndex.COLUMN_NAME_TITLE)
                    val memoNumber =
                        cursor.getColumnIndex(TimeEntryIndex.EntryIndex.COLUMN_NAME_MEMO)
                    val iconIdNumber =
                        cursor.getColumnIndex(TimeEntryIndex.EntryIndex.COLUMN_NAME_ICON_ID)
                    val startTimeNumber =
                        cursor.getColumnIndex(TimeEntryIndex.EntryIndex.COLUMN_NAME_START_TIME)
                    val durationNumber =
                        cursor.getColumnIndex(TimeEntryIndex.EntryIndex.COLUMN_NAME_TIME_DURATION)

                    val indexId = cursor.getLong(indexIdNumber)
                    val title = cursor.getString(titleNumber)
                    val memo = cursor.getString(memoNumber)
                    val iconId = cursor.getInt(iconIdNumber)
                    val startTime = cursor.getLong(startTimeNumber)
                    val durationTime = cursor.getLong(durationNumber)
                    return (ResultListData(indexId, title, memo, iconId, startTime, durationTime))
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (ResultListData(indexId = -1, title = "", memo = "", iconId = 0, startTime = 0, duration = 0))
    }

    override fun getLapTimeList(id: Int): List<LapTimeRecord>
    {
        val lapTimeList = ArrayList<LapTimeRecord>()
        try
        {
            val cursor = database?.getAllDetailData(id.toLong())
            if (cursor != null)
            {
                while (cursor.moveToNext())
                {
                    val lapTimeId = cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY)
                    val lapTime = cursor.getLong(lapTimeId)

                    val recordTypeId = cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_RECORD_TYPE)
                    val recordType = cursor.getInt(recordTypeId)

                    lapTimeList.add(LapTimeRecord(recordType, lapTime))
                }
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (lapTimeList)
    }

    @SuppressLint("Range")
    private fun loadReferenceData()
    {
        // load reference data
        try
        {
            Log.v(TAG, " - - - - - - loadReferenceData() - - - - - -")
            for (id in 0..2)
            {
                val refList: ArrayList<Long> = ArrayList()
                refList.clear()
                val cursor = database?.getAllReferenceDetailData(id)
                if (cursor != null)
                {
                    while (cursor.moveToNext()) {
                        refList.add(cursor.getLong(cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY)))
                    }
                }
                Log.v(TAG, "----- loadReferenceData() : $id, ${refList.size} -----")
                dbCallback?.referenceDataIsReloaded(id, refList)
            }
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

    private fun setIndexId(id: Long)
    {
        try {
            recordingIndexId = id
            val editor = preferences?.edit()
            editor?.putLong(PREF_KEY_TIMER_INDEXID, recordingIndexId)
            editor?.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun onGoingTextTemplate(context : Context) : String
    {
        return (context.getString(R.string.notification_description))
    }

    override fun createIndex(title: String, startTime: Long)
    {
        val memo = ""
        val icon = 0
        Log.v(TAG, "createIndex() $title $startTime")
        val thread = Thread {
            if (isReadyDatabase) {
                try {
                    database?.createIndexData(title, memo, icon, startTime)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }

    override fun appendTimeData(elapsedTime: Long, recordType: Long)
    {
        Log.v(TAG, "appendTimeData()  $elapsedTime [$recordType]")
        val thread = Thread {
            if (isReadyDatabase)
            {
                try
                {
                    database?.appendTimeData(recordingIndexId, elapsedTime, recordType)
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }

    override fun finishTimeData(startTime: Long, endTime: Long)
    {
        Log.v(TAG, "finishTimeData() $startTime $endTime")
        val thread = Thread {
            if (isReadyDatabase) {
                try {
                    database?.finishTimeData(recordingIndexId, startTime, endTime)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        thread.start()
    }

    override fun deleteRecord(id: Int)
    {
        try
        {
            val thread = Thread {
                if (isReadyDatabase)
                {
                    try
                    {
                        database?.deleteTimeEntryData(id.toLong())
                    }
                    catch (e: Exception)
                    {
                        e.printStackTrace()
                    }
                }
            }
            thread.start()
        }
        catch (ee: Exception)
        {
            ee.printStackTrace()
        }
    }
    override fun updateIndexRecord(id: Int, title: String, iconId: Int)
    {
        try
        {
            database?.updateIndexData(id.toLong(), title, iconId)
        }
        catch (ee: Exception)
        {
            ee.printStackTrace()
        }
    }

    override fun updateRecord(id: Int, title: String, iconId: Int)
    {
        try
        {
            val thread = Thread {
                if (isReadyDatabase)
                {
                    try
                    {
                        database?.updateIndexData(id.toLong(), title, iconId)
                    }
                    catch (e: Exception)
                    {
                        e.printStackTrace()
                    }
                }
            }
            thread.start()
        }
        catch (ee: Exception)
        {
            ee.printStackTrace()
        }
    }

    override fun setReferenceIndexData(id: Int, iconId: Int)
    {
        // iconId : Reference A = 0, B = 1, C = それ以外
        try
        {
            val thread = Thread {
                if (isReadyDatabase)
                {
                    try
                    {
                        database?.setReferenceIndexData(iconId, id.toLong())
                    }
                    catch (e: Exception)
                    {
                        e.printStackTrace()
                    }
                }
            }
            thread.start()
        }
        catch (ee: Exception)
        {
            ee.printStackTrace()
        }
    }

    override fun setReferenceIconId(id: Int, iconId: Int)
    {
        // iconId : Reference A = 0, B = 1, C = それ以外
        try
        {
            if (isReadyDatabase)
            {
                try
                {
                    database?.setReferenceIndexData(iconId, id.toLong())
                }
                catch (e: Exception)
                {
                    e.printStackTrace()
                }
            }
        }
        catch (ee: Exception)
        {
            ee.printStackTrace()
        }
    }

    override fun launchNotify(isShow: Boolean)
    {
        try
        {
            if (::myActivity.isInitialized)
            {
                val icon = R.drawable.baseline_directions_run_24
                val title = myActivity.getString(R.string.app_name)
                val description = myActivity.getString(R.string.notification_description)

                Log.v(TAG, "launchNotify(${title} : ${description})")
                val builder = NotificationCompat.Builder(myActivity, CHANNEL_ID)
                    .setSmallIcon(icon)
                    .setContentTitle(title)
                    .setContentText(description)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setOngoing(true)

                val ongoingActivityStatus = Status.Builder()
                    .addTemplate(onGoingTextTemplate(myActivity))
                    .build()

                val intent = Intent(myActivity, MainActivity::class.java)

                val activityPendingIntent = PendingIntent
                    .getActivity(
                        myActivity,
                        0,
                        intent,
                        (PendingIntent.FLAG_UPDATE_CURRENT) or (PendingIntent.FLAG_IMMUTABLE)
                    )

                val ongoingActivity =
                    OngoingActivity.Builder(
                        myActivity, R.string.app_name, builder
                    )
                        .setAnimatedIcon(R.drawable.baseline_directions_run_24)
                        .setStaticIcon(R.drawable.baseline_directions_run_24)
                        .setTouchIntent(activityPendingIntent)
                        .setStatus(ongoingActivityStatus)
                        .build()

                ongoingActivity.apply(myActivity)

                val notificationManager = NotificationManagerCompat.from(myActivity)

                if (ActivityCompat.checkSelfPermission(
                        myActivity,
                        Manifest.permission.POST_NOTIFICATIONS
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Log.v(TAG, " Permission denied to notify.")
                    //ActivityCompat.requestPermissions(context, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
                    return
                }
                if (isShow)
                {
                    // Notificationの発報
                    notificationManager.notify(R.string.app_name, builder.build())
                }
                else
                {
                    // Notificationの解除
                    notificationManager.cancel(R.string.app_name)
                }
            }
        }
        catch(e: Exception)
        {
            e.printStackTrace()
        }
    }

    companion object
    {
        private val TAG = WearableActivityController::class.java.simpleName
        const val PREF_KEY_DISPLAY_LAPGRAPHIC = "DISP_LAPGRPH"
        const val PREF_KEY_REFERENCE_TIME_SELECTION = "REF_TIME_SEL"
        private const val PREF_KEY_TIMER_STARTED = "TMR_START"
        private const val PREF_KEY_TIMER_INDEXID = "TMR_INDEX"
    }
}
