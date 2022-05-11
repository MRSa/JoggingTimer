package net.osdn.gokigen.joggingtimer.recordlist;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.wear.ambient.AmbientModeSupport;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;
import androidx.wear.widget.drawer.WearableNavigationDrawerView;

import net.osdn.gokigen.joggingtimer.R;
import net.osdn.gokigen.joggingtimer.recorddetail.DetailActivity;
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase;
import net.osdn.gokigen.joggingtimer.utilities.ConfirmationDialog;
import net.osdn.gokigen.joggingtimer.utilities.CreateModelData;
import net.osdn.gokigen.joggingtimer.utilities.CreateModelDataDialog;

/**
 *
 *
 */
public class ListActivity extends AppCompatActivity implements IDetailLauncher, RecordSummarySetup.IDatabaseReadyNotify, CreateModelData.ICreatedModelDataCallback, ListSelectionMenuAdapter.ISelectedMenu, AmbientModeSupport.AmbientCallbackProvider
{
    private final String TAG = toString();
    private RecordSummaryAdapter summaryAdapter = null;
    private RecordSummarySetup setupper = null;

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate()");

        setContentView(R.layout.activity_list);

        // Enables Always-on
        //setAmbientEnabled();
        try
        {
            AmbientModeSupport.AmbientController ambientController = AmbientModeSupport.attach(this);
            ambientController.setAutoResumeEnabled(true);
            //boolean isAmbient = ambientController.isAmbient();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            WearableNavigationDrawerView naviView = findViewById(R.id.list_top_navigation_drawer);
            ListSelectionMenuAdapter menuAdapter = new ListSelectionMenuAdapter(this, this);
            naviView.setAdapter(menuAdapter);
            naviView.addOnItemSelectedListener(menuAdapter);


            WearableRecyclerView view = findViewById(R.id.recycler_list_view);
            summaryAdapter = new RecordSummaryAdapter();
            WearableLinearLayoutManager layoutManager = new WearableLinearLayoutManager(this);

            view.setCircularScrollingGestureEnabled(getResources().getConfiguration().isScreenRound());
            //view.setCircularScrollingGestureEnabled(false);

            DividerItemDecoration dividerDecoration = new DividerItemDecoration(view.getContext(), layoutManager.getOrientation());

            view.addItemDecoration(dividerDecoration);
            view.setLayoutManager(layoutManager);
            view.setAdapter(summaryAdapter);

        }
        catch (Exception e)
        {
            e.printStackTrace();
            summaryAdapter = null;
        }
    }

    /**
     *
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    /**
     *
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState)
    {
        super.onRestoreInstanceState(savedInstanceState);
    }

    /**
     *
     */
    @Override
    protected void onResume()
    {
        super.onResume();
        Log.v(TAG, "onResume()");
        try
        {
            setupper = new RecordSummarySetup(this, this, this, summaryAdapter, this);
            setupper.setup();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     *
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.v(TAG, "onPause()");
        try
        {
            if (setupper != null)
            {
                setupper.closeDatabase();
                setupper = null;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        System.gc();
    }

    /**
     *
     *
     */
    @Override
    public void onStart()
    {
        super.onStart();
        Log.v(TAG, "onStart()");
    }

    /**
     *
     *
     */
    @Override
    public void onStop()
    {
        super.onStop();
        Log.v(TAG, "onStop()");
    }

/*
    @Override
    public void onEnterAmbient(Bundle ambientDetails)
    {
        super.onEnterAmbient(ambientDetails);
        Log.v(TAG, "onEnterAmbient()");
    }

    @Override
    public void onExitAmbient()
    {
        super.onExitAmbient();
        Log.v(TAG, "onExitAmbient()");
    }

    @Override
    public void onUpdateAmbient()
    {
        super.onUpdateAmbient();
        Log.v(TAG, "onUpdateAmbient()");
    }
*/

    /**
     *
     *
     */
    @Override
    public void launchDetail(long recordId)
    {
        Log.v(TAG, "launchDetail() id:" + recordId);
        try
        {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(DetailActivity.INTENT_EXTRA_DATA_ID, recordId);
            startActivity(intent);
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
    private void itemSelected(int itemId)
    {
        //String toastMessage = "";
        if (itemId == R.id.menu_create_model)
        {
                // モデルデータの作成
                CreateModelDataDialog dialog2 = CreateModelDataDialog.newInstance(true, getString(R.string.information_time_picker), 0, setupper.getCreateModelDataCallback(ITimeEntryDatabase.DONT_USE_ID, ITimeEntryDatabase.DONT_USE_ID), 0);
                dialog2.show(getSupportFragmentManager(), "dialog2");
        }
/*
        try
        {
            if (toastMessage.length() > 0)
            {
                Toast toast = Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
 */
    }

    /**
     *
     *
     */
    @Override
    public void deleteRecord(@NonNull DataRecord targetRecord)
    {
        try
        {
            final int positionId = targetRecord.getPositionId();
            final String title = targetRecord.getTitle();

            Log.v(TAG, "deleteRecord() : " + title);

            String message = getString(R.string.dialog_message_delete) + " (" + title + ")";
            ConfirmationDialog dialog = ConfirmationDialog.newInstance(getString(R.string.dialog_title_delete), message, () -> {
                Log.v(TAG, "Delete Record Execute [" + title + "]" + " pos:" + positionId);
                if (summaryAdapter != null)
                {
                    final long indexId = summaryAdapter.removeItem(positionId);
                    try
                    {
                        Thread thread = new Thread(() -> {
                            if (indexId >= 0)
                            {
                                setupper.deleteTimeEntryData(indexId);
                            }
                        });
                        thread.start();
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            dialog.show(getSupportFragmentManager(), "dialog");
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
    @Override
    public void databaseSetupFinished(boolean result)
    {
        Log.v(TAG, "databaseSetupFinished() : " + result);
    }

    /**
     *
     *
     */
    @Override
    public void selectedMenu(int itemId)
    {
        itemSelected(itemId);
    }

    /**
     *
     *
     */
    @Override
    public void createdModelData(long indexId)
    {
        // データの登録
        setupper.setIndexData(indexId);

        // 一覧の更新
        runOnUiThread(() -> {
            if (summaryAdapter != null)
            {
                int count = summaryAdapter.getItemCount();
                summaryAdapter.notifyItemChanged(count - 1);
                summaryAdapter.notifyDataSetChanged();
            }

            // Toastで作成を通知する
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.created_model_data), Toast.LENGTH_SHORT);
            toast.show();
        });
    }

    @Override
    public AmbientModeSupport.AmbientCallback getAmbientCallback()
    {
        return (new AmbientModeSupport.AmbientCallback() {
            public void onEnterAmbient(Bundle ambientDetails)
            {
                Log.v(TAG, "onEnterAmbient()");
            }
            public void onExitAmbient(Bundle ambientDetails)
            {
                Log.v(TAG, "onExitAmbient()");
                //updateTimerLabel();
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
        super.onPointerCaptureChanged(hasCapture);
    }
}
