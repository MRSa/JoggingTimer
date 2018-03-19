package net.osdn.gokigen.joggingtimer.recorddetail;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.support.wear.widget.drawer.WearableActionDrawerView;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import net.osdn.gokigen.joggingtimer.R;
import net.osdn.gokigen.joggingtimer.utilities.DataEditDialog;

public class DetailActivity extends WearableActivity implements RecordDetailSetup.IDatabaseReadyNotify, MenuItem.OnMenuItemClickListener, DataEditDialog.Callback
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
        setAmbientEnabled();

        try
        {
            WearableRecyclerView view = findViewById(R.id.recycler_detail_view);
            detailAdapter = new RecordDetailAdapter();
            WearableLinearLayoutManager layoutManager = new WearableLinearLayoutManager(this);

            view.setCircularScrollingGestureEnabled(getResources().getConfiguration().isScreenRound());

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
    protected void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
    }

    /**
     *
     */
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState)
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

            setupper = new RecordDetailSetup(this, indexId, this, detailAdapter);
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

    /**
     *
     *
     */
    @Override
    public void onEnterAmbient(Bundle ambientDetails)
    {
        super.onEnterAmbient(ambientDetails);
        Log.v(TAG, "onEnterAmbient()");
    }

    /**
     *
     *
     */
    @Override
    public void onExitAmbient()
    {
        super.onExitAmbient();
        Log.v(TAG, "onExitAmbient()");
    }

    /**
     *
     *
     */
    @Override
    public void onUpdateAmbient()
    {
        super.onUpdateAmbient();
        Log.v(TAG, "onUpdateAmbient()");
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
    public boolean onMenuItemClick(MenuItem item)
    {
        Log.v(TAG, "onMenuItemClick(): " + item);

        boolean ret = false;
        final int itemId = item.getItemId();
        String toastMessage = "";
        switch (itemId)
        {
            case R.id.menu_edit_title:
                // タイトルの編集
                DataEditDialog dialog = new DataEditDialog(this);
                dialog.show(R.drawable.ic_android_black_24dp, "", this);
                ret = true;
                break;

            case R.id.menu_set_reference:
                // 現在のデータを基準値を設定する
                toastMessage = getString(R.string.action_set_reference);
                ret = true;
                break;

            default:
                // 何もしない
                break;
        }
        try
        {
            actionDrawerView.getController().closeDrawer();

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

        return (ret);
    }

    @Override
    public void dataEdited(int iconId, String title)
    {
        Log.v(TAG, "iconId : " + iconId + " title : '"+ title +"'");
        try {
            WearableRecyclerView view = findViewById(R.id.recycler_detail_view);
            view.postInvalidate();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void cancelled()
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
}
