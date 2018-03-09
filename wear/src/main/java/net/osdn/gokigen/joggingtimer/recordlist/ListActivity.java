package net.osdn.gokigen.joggingtimer.recordlist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wearable.activity.WearableActivity;
import android.support.wear.widget.WearableRecyclerView;
import android.util.Log;

import net.osdn.gokigen.joggingtimer.R;
import net.osdn.gokigen.joggingtimer.recorddetail.DetailActivity;

public class ListActivity extends WearableActivity implements IDetailLauncher
{
    private final String TAG = toString();

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
        setAmbientEnabled();

        try
        {
            WearableRecyclerView view = findViewById(R.id.recycler_list_view);
            RecordSummaryAdapter adapter = new RecordSummaryAdapter(this);
            WearableLinearLayoutManager layoutManager = new WearableLinearLayoutManager(this);

            view.setCircularScrollingGestureEnabled(getResources().getConfiguration().isScreenRound());

            DividerItemDecoration dividerDecoration = new DividerItemDecoration(view.getContext(), layoutManager.getOrientation());

            view.addItemDecoration(dividerDecoration);
            view.setLayoutManager(layoutManager);
            view.setAdapter(adapter);
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
        Log.v(TAG, "onResume()");
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

    @Override
    public void launchDetail(int recordId)
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

    @Override
    public void deleteRecord(int recordId)
    {
        Log.v(TAG, "deleteRecord() id:" + recordId);

    }
}
