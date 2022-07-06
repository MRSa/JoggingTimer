package net.osdn.gokigen.joggingtimer.recorddetail;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.wear.ambient.AmbientModeSupport;
import androidx.wear.widget.WearableLinearLayoutManager;
import androidx.wear.widget.WearableRecyclerView;
import androidx.wear.widget.drawer.WearableActionDrawerView;
import androidx.wear.widget.drawer.WearableNavigationDrawerView;

import net.osdn.gokigen.joggingtimer.R;
import net.osdn.gokigen.joggingtimer.utilities.CreateModelData;
import net.osdn.gokigen.joggingtimer.utilities.DataEditDialog;
import net.osdn.gokigen.joggingtimer.utilities.SetReferenceDialog;


/**
 *
 *
 */
public class DetailActivity extends AppCompatActivity implements RecordDetailSetup.IDatabaseReadyNotify, MenuItem.OnMenuItemClickListener, DataEditDialog.Callback, CreateModelData.IEditedModelDataCallback, DetailSelectionMenuAdapter.ISelectedMenu, AmbientModeSupport.AmbientCallbackProvider, SetReferenceDialog.SetReferenceCallback
{
    private final String TAG = toString();
    public static final String INTENT_EXTRA_DATA_ID = "Detail.dataId";

    private RecordDetailAdapter detailAdapter = null;
    private RecordDetailSetup setupper = null;

    private WearableActionDrawerView actionDrawerView = null;

    /**
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.v(TAG, "onCreate()");

        setContentView(R.layout.activity_detail);

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
            WearableNavigationDrawerView naviView = findViewById(R.id.top_navigation_drawer);
            DetailSelectionMenuAdapter menuAdapter = new DetailSelectionMenuAdapter(this, this);
            naviView.setAdapter(menuAdapter);
            naviView.addOnItemSelectedListener(menuAdapter);

            WearableRecyclerView view = findViewById(R.id.recycler_detail_view);
            detailAdapter = new RecordDetailAdapter();
            WearableLinearLayoutManager layoutManager = new WearableLinearLayoutManager(this);

            //view.setCircularScrollingGestureEnabled(getResources().getConfiguration().isScreenRound());
            view.setCircularScrollingGestureEnabled(false);

            DividerItemDecoration dividerDecoration = new DividerItemDecoration(view.getContext(), layoutManager.getOrientation());

            view.addItemDecoration(dividerDecoration);
            view.setLayoutManager(layoutManager);
            view.setAdapter(detailAdapter);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            detailAdapter = null;
        }

        // Bottom Action Drawer
        actionDrawerView = findViewById(R.id.bottom_action_drawer);
        actionDrawerView.getController().peekDrawer();
        actionDrawerView.setOnMenuItemClickListener(this);

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
        Log.v(TAG, "onResume() ");
    }

    /**
     *
     */
    @Override
    protected void onPause()
    {
        super.onPause();
        Log.v(TAG, "onPause()");
    }

    /**
     *
     *
     */
    @Override
    public void onStart()
    {
        super.onStart();
        try
        {
            long indexId = getIntent().getLongExtra(INTENT_EXTRA_DATA_ID, -1);
            Log.v(TAG, "onResume() " + indexId);

            setupper = new RecordDetailSetup(this, indexId, this, detailAdapter, this);
            setupper.setup();
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
    public void onStop()
    {
        super.onStop();
        Log.v(TAG, "onStop()");

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
    public void databaseSetupFinished(boolean result)
    {
        Log.v(TAG, "databaseSetupFinished() : " + result);
    }

    /**
     *
     *
     */
    @Override
    public void updatedIndexData(final boolean isIconOnly)
    {
        Log.v(TAG, "selectedReferenceData() : " + isIconOnly);
        runOnUiThread(() -> {
            try
            {
                String title;
                if (isIconOnly)
                {
                    title = getString(R.string.action_set_reference);
                }
                else
                {
                    title = getString(R.string.action_edited_data);
                }
                Toast toast = Toast.makeText(getApplicationContext(), title, Toast.LENGTH_SHORT);
                toast.show();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    /**
     *
     *
     */
    @Override
    public boolean onMenuItemClick(MenuItem item)
    {
        Log.v(TAG, "onMenuItemClick(): " + item);

        final int itemId = item.getItemId();
        try
        {
            actionDrawerView.getController().closeDrawer();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (itemSelected(itemId));
    }

    private boolean itemSelected(int itemId)
    {
        boolean ret = false;
        //String toastMessage = "";
        if (itemId == R.id.menu_edit_title)
        {
            // タイトルの編集
            String title = "";
            int iconId = R.drawable.ic_android_black_24dp;
            RecordDetailSetup.EditIndexData data = setupper.getEditIndexData();
            if (data != null) {
                iconId = data.getIcon();
                title = data.getTitle();
            }
            DataEditDialog dialog = DataEditDialog.newInstance(iconId, title, this);
            FragmentManager manager = getSupportFragmentManager();
            dialog.show(manager, "dialog");
            ret = true;
        }
        else if (itemId == R.id.menu_set_reference)
        {
            // 基準値の設定ダイアログを表示する
            final SetReferenceDialog.SetReferenceCallback callback = this;
            this.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try
                    {
                        // 基準値設定ダイアログを表示する
                        SetReferenceDialog dialog = SetReferenceDialog.newInstance("Set Reference", "Please Select Reference Type", callback);
                        FragmentManager manager = getSupportFragmentManager();
                        dialog.show(manager, "dialog");
                    }
                    catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            });
            ret = true;
        }
        else if (itemId == R.id.menu_share_data)
        {
            // 現在のデータを共有する
            setupper.shareTheData(detailAdapter);
            ret = true;
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
        return (ret);
    }

    /**
     *
     */
    @Override
    public void dataEdited(int iconId, String title)
    {
        Log.v(TAG, "iconId : " + iconId + " title : '"+ title +"'");
        try {
            setupper.setEditIndexData(title, iconId);
            WearableRecyclerView view = findViewById(R.id.recycler_detail_view);
            view.postInvalidate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     */
    private void updateScreen()
    {
        try
        {
            WearableRecyclerView view = findViewById(R.id.recycler_detail_view);
            view.postInvalidate();
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
    public void cancelled()
    {
        updateScreen();
    }

    /**
     *
     */
    @Override
    public void editedModelData(long indexId, long detailId, int lapCount, long prevTime, long newTime)
    {
        Log.v(TAG, "editedModelData() " + indexId + " " + detailId + " " + lapCount + " (" + prevTime + " -> " + newTime + ")");
        if (detailAdapter == null)
        {
            return;
        }

        long diffTime = newTime - prevTime;
        int count = detailAdapter.getItemCount();
        if (count > 1)
        {
            long totalTime = 0;
            long modTime = diffTime * (-1) / (count - 1);
            for (int index = 1; index <= count; index++)
            {
                DetailRecord record = detailAdapter.getRecord(index - 1);
                if (lapCount == index)
                {
                    totalTime = record.addModifiedTime(diffTime, totalTime);
                }
                else
                {
                    totalTime = record.addModifiedTime(modTime, totalTime);
                }
                detailAdapter.notifyItemChanged(index - 1);
            }
            try
            {
                Thread thread = new Thread(() -> {
                    if (setupper != null)
                    {
                        setupper.updateDatabaseRecord(detailAdapter);
                    }
                });
                thread.start();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        try
        {
            detailAdapter.notifyDataSetChanged();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void selectedMenu(int itemId)
    {
        itemSelected(itemId);
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
            }
        });
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture)
    {
        super.onPointerCaptureChanged(hasCapture);
    }

    @Override
    public void confirmed(int id)
    {
        // 現在のデータを基準値として設定する
        Log.v(TAG, " SET REFERENCE DATA ID: " + id);
        setupper.setReferenceData(id);
    }
}
