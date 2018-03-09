package net.osdn.gokigen.joggingtimer.recorddetail;

import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.wear.widget.WearableLinearLayoutManager;
import android.support.wear.widget.WearableRecyclerView;
import android.support.wearable.activity.WearableActivity;
import android.util.Log;

import net.osdn.gokigen.joggingtimer.R;

public class DetailActivity extends WearableActivity
{
    private final String TAG = toString();
    public static final String INTENT_EXTRA_DATA_ID = "Detail.dataId";

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
            RecordDetailAdapter adapter = new RecordDetailAdapter();
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
        try
        {
            int dataId = getIntent().getIntExtra(INTENT_EXTRA_DATA_ID, -1);
            Log.v(TAG, "onResume() " + dataId);
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
}
