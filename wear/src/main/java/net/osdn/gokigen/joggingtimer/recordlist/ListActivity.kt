package net.osdn.gokigen.joggingtimer.recordlist

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.InputDeviceCompat
import androidx.core.view.MotionEventCompat
import androidx.core.view.ViewConfigurationCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.wear.ambient.AmbientModeSupport
import androidx.wear.widget.WearableLinearLayoutManager
import androidx.wear.widget.WearableRecyclerView
import androidx.wear.widget.drawer.WearableNavigationDrawerView
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.recorddetail.DetailActivity
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase
import net.osdn.gokigen.joggingtimer.utilities.ConfirmationDialog
import net.osdn.gokigen.joggingtimer.utilities.CreateModelData.ICreatedModelDataCallback
import net.osdn.gokigen.joggingtimer.utilities.CreateModelDataDialog
import kotlin.math.roundToInt

/**
 *
 *
 */
class ListActivity : AppCompatActivity(),
    IDetailLauncher, RecordSummarySetup.IDatabaseReadyNotify, ICreatedModelDataCallback,
    ListSelectionMenuAdapter.ISelectedMenu, AmbientModeSupport.AmbientCallbackProvider
{
    private var summaryAdapter: RecordSummaryAdapter? = null
    private var setupper: RecordSummarySetup? = null

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate()")
        setContentView(R.layout.activity_list)

        // Enables Always-on
        //setAmbientEnabled();
        try
        {
            val ambientController = AmbientModeSupport.attach(this)
            ambientController.setAutoResumeEnabled(true)
            //boolean isAmbient = ambientController.isAmbient();
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        try
        {
            val naviView =
                findViewById<WearableNavigationDrawerView>(R.id.list_top_navigation_drawer)
            val menuAdapter = ListSelectionMenuAdapter(this, this)
            naviView.setAdapter(menuAdapter)
            naviView.addOnItemSelectedListener(menuAdapter)
            val view = findViewById<WearableRecyclerView>(R.id.recycler_list_view)
            summaryAdapter = RecordSummaryAdapter()
            val layoutManager = WearableLinearLayoutManager(this)
            view.isCircularScrollingGestureEnabled = resources.configuration.isScreenRound
            //view.setCircularScrollingGestureEnabled(false);
            val dividerDecoration = DividerItemDecoration(view.context, layoutManager.orientation)
            view.addItemDecoration(dividerDecoration)
            view.layoutManager = layoutManager
            view.adapter = summaryAdapter
        }
        catch (e: Exception)
        {
            e.printStackTrace()
            summaryAdapter = null
        }
    }

    /**
     *
     */
    override fun onResume()
    {
        super.onResume()
        Log.v(TAG, "onResume()")
        try
        {
            setupper = RecordSummarySetup(this, this, this, summaryAdapter, this)
            setupper?.setup()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    /**
     *
     */
    override fun onPause()
    {
        super.onPause()
        Log.v(TAG, "onPause()")
        try
        {
            if (setupper != null)
            {
                setupper?.closeDatabase()
                setupper = null
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        System.gc()
    }

    /**
     *
     *
     */
    public override fun onStart()
    {
        super.onStart()
        Log.v(TAG, "onStart()")
    }

    /**
     *
     *
     */
    public override fun onStop()
    {
        super.onStop()
        Log.v(TAG, "onStop()")
    }

    override fun onGenericMotionEvent(ev: MotionEvent?): Boolean
    {
        try
        {
            if ((ev?.action == MotionEvent.ACTION_SCROLL)&& (ev.isFromSource(InputDeviceCompat.SOURCE_ROTARY_ENCODER)))
            {
                // ロータリー入力でスクロールする
                // Log.v(TAG, "Rotary Encoder Input")
                val view = findViewById<WearableRecyclerView>(R.id.recycler_list_view)
                val delta = -ev.getAxisValue(MotionEventCompat.AXIS_SCROLL) *
                        ViewConfigurationCompat.getScaledVerticalScrollFactor(ViewConfiguration.get(this), this)
                view.scrollBy(0, delta.roundToInt())
                return (true)
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
        return (super.onGenericMotionEvent(ev))
    }

    /**
     *
     *
     */
    override fun launchDetail(recordId: Long)
    {
        Log.v(TAG, "launchDetail() id:$recordId")
        try
        {
            val intent = Intent(this, DetailActivity::class.java)
            intent.putExtra(DetailActivity.INTENT_EXTRA_DATA_ID, recordId)
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
    private fun itemSelected(itemId: Int)
    {
        if (itemId == R.id.menu_create_model)
        {
            // モデルデータの作成
            val dialog2 = CreateModelDataDialog.newInstance(
                true,
                getString(R.string.information_time_picker),
                0,
                setupper?.getCreateModelDataCallback(
                    ITimeEntryDatabase.DONT_USE_ID,
                    ITimeEntryDatabase.DONT_USE_ID
                ),
                0
            )
            dialog2.show(supportFragmentManager, "dialog2")
        }
    }

    /**
     *
     *
     */
    override fun deleteRecord(targetRecord: DataRecord)
    {
        try
        {
            val positionId = targetRecord.positionId
            val title = targetRecord.title
            Log.v(TAG, "deleteRecord() : $title")
            val message = getString(R.string.dialog_message_delete) + " (" + title + ")"
            val dialog = ConfirmationDialog.newInstance(
                getString(R.string.dialog_title_delete), message
            ) {
                Log.v(
                    TAG,
                    "Delete Record Execute [$title] pos:$positionId"
                )
                if (summaryAdapter != null) {
                    val indexId = summaryAdapter!!.removeItem(positionId)
                    try {
                        val thread = Thread {
                            if (indexId >= 0) {
                                setupper!!.deleteTimeEntryData(indexId)
                            }
                        }
                        thread.start()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            dialog.show(supportFragmentManager, "dialog")
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
    override fun databaseSetupFinished(result: Boolean)
    {
        Log.v(TAG, "databaseSetupFinished() : $result")
    }

    /**
     *
     *
     */
    override fun selectedMenu(itemId: Int)
    {
        itemSelected(itemId)
    }

    /**
     *
     *
     */
    @SuppressLint("NotifyDataSetChanged")
    override fun createdModelData(indexId: Long)
    {
        try
        {
            // データの登録
            setupper?.setIndexData(indexId)

            // 一覧の更新
            runOnUiThread {
                if (summaryAdapter != null)
                {
                    val count = summaryAdapter?.itemCount ?: 1
                    summaryAdapter?.notifyItemChanged(count - 1)
                    summaryAdapter?.notifyDataSetChanged()
                }

                // Toastで作成を通知する
                val toast = Toast.makeText(
                    applicationContext,
                    getString(R.string.created_model_data),
                    Toast.LENGTH_SHORT
                )
                toast.show()
            }
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback
    {
        return object : AmbientModeSupport.AmbientCallback() {
            override fun onEnterAmbient(ambientDetails: Bundle)
            {
                Log.v(TAG, "onEnterAmbient()")
            }
        }
    }

    companion object
    {
        private val TAG = ListActivity::class.java.simpleName
    }
}
