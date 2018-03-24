package net.osdn.gokigen.joggingtimer.stopwatch;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import net.osdn.gokigen.joggingtimer.R;
import net.osdn.gokigen.joggingtimer.stopwatch.graphview.LapTimeGraphView;
import net.osdn.gokigen.joggingtimer.stopwatch.listview.ILapTimeHolder;
import net.osdn.gokigen.joggingtimer.stopwatch.listview.LapTimeArrayAdapter;
import net.osdn.gokigen.joggingtimer.stopwatch.listview.LapTimeItems;
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase;
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabaseCallback;
import net.osdn.gokigen.joggingtimer.storage.TimeEntryDatabaseFactory;
import net.osdn.gokigen.joggingtimer.storage.contract.TimeEntryData;

import java.util.ArrayList;

import static android.content.Context.VIBRATOR_SERVICE;

/**
 *
 *
 *
 */
class WearableActivityController implements IWearableActivityControl, ITimeEntryDatabaseCallback, IDataEntry
{
    private final String TAG = toString();
    private final String PREF_KEY_TIMER_STARTED = "TMR_START";
    private final String PREF_KEY_TIMER_INDEXID = "TMR_INDEX";

    private SharedPreferences preferences = null;
    private final ButtonClickListener clickListener = new ButtonClickListener();
    private ITimeEntryDatabase database = null;
    private IDatabaseReloadCallback dbCallback = null;
    private boolean isReadyDatabase = false;
    private boolean pendingLoadReference = false;
    private long recordingIndexId = -1;
    private ILapTimeHolder lapTimeHolder = null;

    private Vibrator vibrator = null;
    //private PowerManager powerManager = null;


    WearableActivityController() {
        Log.v(TAG, "WearableActivityController()");
    }

    @Override
    public void setup(WearableActivity activity, IClickCallback callback, IDatabaseReloadCallback dbCallback) {
        this.preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        this.dbCallback = dbCallback;
        setupPermissions(activity);
        setupHardwares(activity);
        setupScreen(activity);
        //setupDatabase(activity, false); // chhange true if when databaese file should be cleared.
        setupListeners(activity, callback);
    }

    /**
     *
     *
     */
    private void setupPermissions(WearableActivity activity) {
        if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(activity, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{
                            Manifest.permission.VIBRATE,
                            Manifest.permission.WAKE_LOCK,
                    },
                    100);
        }
    }

    /**
     *
     *
     */
    private void setupHardwares(WearableActivity activity) {
        // バイブレータをつかまえる
        vibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);

        //// パワーマネージャをつかまえる
        //powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);
    }

    /**
     *
     *
     */
    private void setupScreen(WearableActivity activity)
    {
        TextView mTextView = activity.findViewById(R.id.text);
        if (mTextView != null) {
            mTextView.setText(R.string.app_name);
        }

        LapTimeArrayAdapter adapter = new LapTimeArrayAdapter(activity.getApplicationContext(), R.layout.column_laptime);
        adapter.clearLapTime();
        lapTimeHolder = adapter;
        ListView lapTimeArea = activity.findViewById(R.id.laptime_list_area);
        lapTimeArea.setAdapter(adapter);
    }

    /**
     * データベースのセットアップ
     */
    @Override
    public void setupDatabase(final WearableActivity activity, final boolean isInitialize) {
        database = new TimeEntryDatabaseFactory(activity, this).getEntryDatabase();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (isInitialize) {
                        // 既存のデータベースを消去する場合、、、
                        TimeEntryDatabaseFactory.deleteDatabase(activity);
                    }
                    database.prepare();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     * リスナのセットアップ
     */
    private void setupListeners(WearableActivity activity, IClickCallback callback)
    {
        try
        {
            clickListener.setCallback(callback);
            ImageButton btn1 = activity.findViewById(R.id.btn1);
            btn1.setOnClickListener(clickListener);
            btn1.setOnLongClickListener(clickListener);

            ImageButton btn2 = activity.findViewById(R.id.btn2);
            btn2.setOnClickListener(clickListener);
            btn2.setOnLongClickListener(clickListener);

            ImageButton btn3 = activity.findViewById(R.id.btn3);
            btn3.setOnClickListener(clickListener);
            btn3.setOnLongClickListener(clickListener);

            TextView main = activity.findViewById(R.id.main_counter);
            main.setOnClickListener(clickListener);
            main.setOnLongClickListener(clickListener);

            TextView sub1 = activity.findViewById(R.id.sub_counter1);
            sub1.setOnClickListener(clickListener);
            sub1.setOnLongClickListener(clickListener);

            ListView lap = activity.findViewById(R.id.laptime_list_area);
            lap.setOnClickListener(clickListener);
            lap.setOnLongClickListener(clickListener);

            LapTimeGraphView graphView = activity.findViewById(R.id.graph_area);
            graphView.setOnClickListener(clickListener);
            graphView.setOnLongClickListener(clickListener);
            graphView.setOnTouchListener(clickListener);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     */
    private void closeDatabase()
    {
        Log.v(TAG, "closeDatabase()");
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                // DBのクローズ実行
                if (isReadyDatabase) {
                    isReadyDatabase = false;
                    try {
                        Log.v(TAG, "closeDatabase() EXECUTE...");
                        database.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    /**
     *
     *
     */
    @Override
    public void exitApplication(WearableActivity activity)
    {
        Log.v(TAG, "exitApplication()");
        closeDatabase();
    }

    @Override
    public void vibrate(final int duration)
    {
        try
        {
            if ((vibrator == null) || (!vibrator.hasVibrator()))
            {
                return;
            }

            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    vibrator.vibrate(duration);
                }
            });
            thread.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public IDataEntry getDataEntry() {
        return (this);
    }

    @Override
    public void timerStarted(boolean isStarted)
    {
        try
        {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean(PREF_KEY_TIMER_STARTED, isStarted);
            editor.apply();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void addTimeStamp(long count, long lapTime, long diffTime)
    {
        if (lapTimeHolder != null)
        {
            lapTimeHolder.addLapTime(new LapTimeItems(count, lapTime, diffTime));
        }
    }

    @Override
    public void clearTimeStamp()
    {
        if (lapTimeHolder != null)
        {
            lapTimeHolder.clearLapTime();
        }
    }

    @Override
    public void setupReferenceData()
    {
        try
        {
            if (isReadyDatabase)
            {
                loadReferenceData();
                pendingLoadReference = false;
            }
            else
            {
                pendingLoadReference = true;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void prepareFinished(boolean isReady)
    {
        Log.v(TAG, "database prepareFinished() : " + isReady);
        isReadyDatabase = isReady;

        try {
            boolean isStarted = preferences.getBoolean(PREF_KEY_TIMER_STARTED, false);
            recordingIndexId = preferences.getLong(PREF_KEY_TIMER_INDEXID, -1);

            Log.v(TAG, "isStarted : " + isStarted + "  indexId : " + recordingIndexId);

            // load reference data
            if (pendingLoadReference)
            {
                loadReferenceData();
                pendingLoadReference = false;
            }

            // load current lap time list
            if ((isStarted) && (recordingIndexId >= 0) && (isReadyDatabase))
            {
                ArrayList<Long> list = new ArrayList<>();
                Cursor cursor = database.getAllDetailData(recordingIndexId);
                while (cursor.moveToNext())
                {
                    list.add(cursor.getLong(cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY)));
                }
                dbCallback.dataIsReloaded(list);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void loadReferenceData()
    {
        // load reference data
        try
        {
            ArrayList<Long> refList = null;
            Cursor cursor = database.getAllReferenceDetailData();
            if (cursor != null) {
                refList = new ArrayList<>();
                while (cursor.moveToNext())
                {
                    refList.add(cursor.getLong(cursor.getColumnIndex(TimeEntryData.EntryData.COLUMN_NAME_TIME_ENTRY)));
                }
            }
            dbCallback.referenceDataIsReloaded(refList);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void timeEntryFinished(OperationType operationType, boolean result, long indexId, long dataId)
    {
        Log.v(TAG, "database timeEntryFinished() : " + result + "  [" + indexId + "] " + dataId);
    }

    @Override
    public void dataEntryFinished(OperationType operationType, boolean result, long id, String title)
    {
        Log.v(TAG, "database dataEntryFinished() : " + result + "  [" + id + "] " + title);
        if ((result)&&(operationType == OperationType.CREATED))
        {
            setIndexId(id);
        }
    }

    private void setIndexId(long id)
    {
        try
        {
            recordingIndexId = id;
            SharedPreferences.Editor editor = preferences.edit();
            editor.putLong(PREF_KEY_TIMER_INDEXID, recordingIndexId);
            editor.apply();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void createIndex(final String title, final long startTime)
    {
        final String memo = "";
        final int icon = 0;
        Log.v(TAG, "createIndex() " + title + " " + startTime);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                if (isReadyDatabase)
                {
                    try
                    {
                        database.createIndexData(title, memo, icon, startTime);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void appendTimeData(final long elapsedTime)
    {
        Log.v(TAG, "appendTimeData() " + " " + elapsedTime);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                if (isReadyDatabase)
                {
                    try
                    {
                        database.appendTimeData(recordingIndexId, elapsedTime);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    @Override
    public void finishTimeData(final long startTime, final long endTime)
    {
        Log.v(TAG, "finishTimeData() " + startTime + " " + endTime);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                if (isReadyDatabase)
                {
                    try
                    {
                        database.finishTimeData(recordingIndexId, startTime, endTime);
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }
}
