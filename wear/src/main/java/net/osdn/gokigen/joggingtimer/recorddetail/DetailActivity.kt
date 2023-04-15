package net.osdn.gokigen.joggingtimer.recorddetail

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
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
import androidx.wear.widget.drawer.WearableActionDrawerView
import androidx.wear.widget.drawer.WearableNavigationDrawerView
import net.osdn.gokigen.joggingtimer.R
import net.osdn.gokigen.joggingtimer.utilities.CreateModelData.IEditedModelDataCallback
import net.osdn.gokigen.joggingtimer.utilities.DataEditDialog
import net.osdn.gokigen.joggingtimer.utilities.SetReferenceDialog
import net.osdn.gokigen.joggingtimer.utilities.SetReferenceDialog.SetReferenceCallback
import kotlin.math.roundToInt

/**
 *
 *
 */
class DetailActivity : AppCompatActivity(),
    RecordDetailSetup.IDatabaseReadyNotify, MenuItem.OnMenuItemClickListener,
    DataEditDialog.Callback, IEditedModelDataCallback, DetailSelectionMenuAdapter.ISelectedMenu,
    AmbientModeSupport.AmbientCallbackProvider, SetReferenceCallback {
    private val TAG = toString()
    private var detailAdapter: RecordDetailAdapter? = null
    private var setupper: RecordDetailSetup? = null
    private var actionDrawerView: WearableActionDrawerView? = null

    /**
     *
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.v(TAG, "onCreate()")
        setContentView(R.layout.activity_detail)

        // Enables Always-on
        //setAmbientEnabled();
        try {
            val ambientController = AmbientModeSupport.attach(this)
            ambientController.setAutoResumeEnabled(true)
            //boolean isAmbient = ambientController.isAmbient();
        } catch (e: Exception) {
            e.printStackTrace()
        }
        try {
            val naviView = findViewById<WearableNavigationDrawerView>(R.id.top_navigation_drawer)
            val menuAdapter = DetailSelectionMenuAdapter(this, this)
            naviView.setAdapter(menuAdapter)
            naviView.addOnItemSelectedListener(menuAdapter)
            val view = findViewById<WearableRecyclerView>(R.id.recycler_detail_view)
            detailAdapter = RecordDetailAdapter()
            val layoutManager = WearableLinearLayoutManager(this)

            //view.setCircularScrollingGestureEnabled(getResources().getConfiguration().isScreenRound());
            view.isCircularScrollingGestureEnabled = false
            val dividerDecoration = DividerItemDecoration(view.context, layoutManager.orientation)
            view.addItemDecoration(dividerDecoration)
            view.layoutManager = layoutManager
            view.adapter = detailAdapter
        } catch (e: Exception) {
            e.printStackTrace()
            detailAdapter = null
        }

        // Bottom Action Drawer
        actionDrawerView = findViewById(R.id.bottom_action_drawer)
        actionDrawerView?.controller?.peekDrawer()
        actionDrawerView?.setOnMenuItemClickListener(this)
    }

    /**
     *
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
    }

    /**
     *
     */
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
    }

    /**
     *
     */
    override fun onResume() {
        super.onResume()
        Log.v(TAG, "onResume() ")
    }

    /**
     *
     */
    override fun onPause() {
        super.onPause()
        Log.v(TAG, "onPause()")
    }

    /**
     *
     *
     */
    public override fun onStart() {
        super.onStart()
        try {
            val indexId = intent.getLongExtra(INTENT_EXTRA_DATA_ID, -1)
            Log.v(TAG, "onResume() $indexId")
            setupper = RecordDetailSetup(this, indexId, this, detailAdapter, this)
            setupper!!.setup()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *
     *
     */
    public override fun onStop() {
        super.onStop()
        Log.v(TAG, "onStop()")
        try {
            if (setupper != null) {
                setupper!!.closeDatabase()
                setupper = null
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        System.gc()
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

    override fun onGenericMotionEvent(ev: MotionEvent?): Boolean
    {
        try
        {
            if ((ev?.action == MotionEvent.ACTION_SCROLL)&& (ev.isFromSource(InputDeviceCompat.SOURCE_ROTARY_ENCODER)))
            {
                // ロータリー入力でスクロールする
                // Log.v(TAG, "Rotary Encoder Input")
                val view = findViewById<WearableRecyclerView>(R.id.recycler_detail_view)
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
    override fun databaseSetupFinished(result: Boolean) {
        Log.v(TAG, "databaseSetupFinished() : $result")
    }

    /**
     *
     *
     */
    override fun updatedIndexData(isIconOnly: Boolean) {
        Log.v(TAG, "selectedReferenceData() : $isIconOnly")
        runOnUiThread {
            try {
                val title: String
                title = if (isIconOnly) {
                    getString(R.string.action_set_reference)
                } else {
                    getString(R.string.action_edited_data)
                }
                val toast =
                    Toast.makeText(applicationContext, title, Toast.LENGTH_SHORT)
                toast.show()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     *
     *
     */
    override fun onMenuItemClick(item: MenuItem): Boolean {
        Log.v(TAG, "onMenuItemClick(): $item")
        val itemId = item.itemId
        try {
            actionDrawerView!!.controller.closeDrawer()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return itemSelected(itemId)
    }

    private fun itemSelected(itemId: Int): Boolean {
        var ret = false
        //String toastMessage = "";
        if (itemId == R.id.menu_edit_title) {
            // タイトルの編集
            var title = ""
            var iconId = R.drawable.ic_android_black_24dp
            val data = setupper!!.editIndexData
            if (data != null) {
                iconId = data.getIcon()
                title = data.getTitle()
            }
            val dialog = DataEditDialog.newInstance(iconId, title, this)
            val manager = supportFragmentManager
            dialog.show(manager, "dialog")
            ret = true
        } else if (itemId == R.id.menu_set_reference) {
            // 基準値の設定ダイアログを表示する
            val callback: SetReferenceCallback = this
            runOnUiThread {
                try {
                    // 基準値設定ダイアログを表示する
                    val dialog = SetReferenceDialog.newInstance(callback)
                    val manager = supportFragmentManager
                    dialog.show(manager, "dialog")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            ret = true
        } else if (itemId == R.id.menu_share_data) {
            // 現在のデータを共有する
            setupper!!.shareTheData(detailAdapter)
            ret = true
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
*/return ret
    }

    /**
     *
     */
    override fun dataEdited(iconId: Int, title: String) {
        Log.v(TAG, "iconId : $iconId title : '$title'")
        try {
            setupper!!.setEditIndexData(title, iconId)
            val view = findViewById<WearableRecyclerView>(R.id.recycler_detail_view)
            view.postInvalidate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *
     */
    private fun updateScreen() {
        try {
            val view = findViewById<WearableRecyclerView>(R.id.recycler_detail_view)
            view.postInvalidate()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     *
     */
    override fun cancelled() {
        updateScreen()
    }

    /**
     *
     */
    override fun editedModelData(
        indexId: Long,
        detailId: Long,
        lapCount: Int,
        prevTime: Long,
        newTime: Long
    ) {
        Log.v(
            TAG,
            "editedModelData() $indexId $detailId $lapCount ($prevTime -> $newTime)"
        )
        if (detailAdapter == null) {
            return
        }
        val diffTime = newTime - prevTime
        val count = detailAdapter!!.itemCount
        if (count > 1) {
            var totalTime: Long = 0
            val modTime = diffTime * -1 / (count - 1)
            for (index in 1..count) {
                val record = detailAdapter!!.getRecord(index - 1)
                totalTime = if (lapCount == index) {
                    record.addModifiedTime(diffTime, totalTime)
                } else {
                    record.addModifiedTime(modTime, totalTime)
                }
                detailAdapter!!.notifyItemChanged(index - 1)
            }
            try {
                val thread = Thread {
                    if (setupper != null) {
                        setupper!!.updateDatabaseRecord(detailAdapter!!)
                    }
                }
                thread.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        try {
            detailAdapter?.notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun selectedMenu(itemId: Int) {
        itemSelected(itemId)
    }

    override fun getAmbientCallback(): AmbientModeSupport.AmbientCallback {
        return object : AmbientModeSupport.AmbientCallback() {
            override fun onEnterAmbient(ambientDetails: Bundle) {
                Log.v(TAG, "onEnterAmbient()")
            }

            fun onExitAmbient(ambientDetails: Bundle?) {
                Log.v(TAG, "onExitAmbient()")
            }
        }
    }

    override fun onPointerCaptureChanged(hasCapture: Boolean) {
        super.onPointerCaptureChanged(hasCapture)
    }

    override fun confirmed(id: Int) {
        // 現在のデータを基準値として設定する
        Log.v(TAG, " SET REFERENCE DATA ID: $id")
        setupper!!.setReferenceData(id)
    }

    companion object {
        const val INTENT_EXTRA_DATA_ID = "Detail.dataId"
    }
}