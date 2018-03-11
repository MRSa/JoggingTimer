package net.osdn.gokigen.joggingtimer.stopwatch;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.os.Vibrator;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import net.osdn.gokigen.joggingtimer.R;
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase;
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabaseCallback;
import net.osdn.gokigen.joggingtimer.storage.TimeEntryDatabaseFactory;

import static android.content.Context.POWER_SERVICE;
import static android.content.Context.VIBRATOR_SERVICE;

/**
 *
 *
 *
 */
class WearableActivityController implements IWearableActivityControl, ITimeEntryDatabaseCallback, IDataEntry
{
    private final String TAG = toString();
    private final ButtonClickListener clickListener = new ButtonClickListener();
    private ITimeEntryDatabase database = null;
    private boolean isReadyDatabase = false;
    private long recordingIndexId = -1;

    private Vibrator vibrator = null;
    private PowerManager powerManager = null;


    WearableActivityController()
    {
        Log.v(TAG, "WearableActivityController()");
    }

    @Override
    public void setup(WearableActivity activity, IClickCallback callback)
    {
        setupPermissions(activity);
        setupHardwares(activity);
        setupScreen(activity);
        setupDatabase(activity, false); // chhange true if when databaese file should be cleared.
        setupListeners(activity, callback);
    }

    /**
     *
     *
     */
    private void setupPermissions(WearableActivity activity)
    {
        if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED)||
                (ContextCompat.checkSelfPermission(activity, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED))
        {
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
    private void setupHardwares(WearableActivity activity)
    {
        // バイブレータをつかまえる
        vibrator = (Vibrator) activity.getSystemService(VIBRATOR_SERVICE);

        // パワーマネージャをつかまえる
        powerManager = (PowerManager) activity.getSystemService(POWER_SERVICE);
    }

    /**
     *
     *
     */
    private void setupScreen(WearableActivity activity)
    {
        TextView mTextView = activity.findViewById(R.id.text);
        if (mTextView != null)
        {
            mTextView.setText(R.string.app_name);
        }
    }

    /**
     *   データベースのセットアップ
     *
     */
    private void setupDatabase(final WearableActivity activity, final boolean isInitialize)
    {
        database = new TimeEntryDatabaseFactory(activity, this).getEntryDatabase();
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                try
                {
                    if (isInitialize)
                    {
                        // 既存のデータベースを消去する場合、、、
                        TimeEntryDatabaseFactory.deleteDatabase(activity);
                    }

                    database.prepare();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }

    /**
     *   リスナのセットアップ
     *
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
            public void run()
            {
                // DBのクローズ実行
                if (isReadyDatabase)
                {
                    isReadyDatabase = false;
                    try
                    {
                        Log.v(TAG, "closeDatabase() EXECUTE...");
                        database.close();
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

    private int convertIconId(int iconId)
    {
        return (iconId);
    }

    /**
     *
     *
     */
    @Override
    public void exitApplication(WearableActivity activity)
    {
        Log.v(TAG, "exitApplication()");

        // パワーマネージャを確認し、interactive modeではない場合は、ライブビューも止めず、カメラの電源も切らない
        if ((powerManager != null)&&(!powerManager.isInteractive()))
        {
            Log.v(TAG, "not interactive, keep database.");
            return;
        }

        closeDatabase();


        //finish();
        //finishAndRemoveTask();
        //android.os.Process.killProcess(android.os.Process.myPid());
    }

    @Override
    public void vibrate(final int duration)
    {
        try
        {
            if ((vibrator == null)||(!vibrator.hasVibrator()))
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
    public IDataEntry getDataEntry()
    {
        return (this);
    }

    @Override
    public void prepareFinished(boolean isReady)
    {
        Log.v(TAG, "database prepareFinished() : " + isReady);
        isReadyDatabase = isReady;
    }


    @Override
    public void timeEntryFinished(OperationType operationType, boolean result, long indexId, long dataId)
    {
        Log.v(TAG, "database timeEntryFinished() : " + result + "  [" + indexId + "] " + dataId);
        if (result)
        {
            recordingIndexId = indexId;
        }
    }

    @Override
    public void dataEntryFinished(OperationType operationType, boolean result, long id, String title)
    {
        Log.v(TAG, "database dataEntryFinished() : " + result + "  [" + id + "] " + title);
        if (result)
        {
            recordingIndexId = id;
        }
    }

    @Override
    public void createIndex(final String title, final String memo, final int icon, final long startTime)
    {
        Log.v(TAG, "createIndex() " + title + " " + startTime);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run()
            {
                if (isReadyDatabase)
                {
                    try
                    {
                        database.createIndexData(title, memo, convertIconId(icon), startTime);
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
