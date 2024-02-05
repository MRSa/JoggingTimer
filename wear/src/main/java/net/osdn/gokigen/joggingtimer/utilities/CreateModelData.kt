package net.osdn.gokigen.joggingtimer.utilities

import android.util.Log
import net.osdn.gokigen.joggingtimer.storage.ITimeEntryDatabase

class CreateModelData(
    private val database: ITimeEntryDatabase,
    private val editCallback: IEditedModelDataCallback?,
    private val createCallback: ICreatedModelDataCallback?,
    private val indexId: Long,
    private val detailId: Long
)
{
    fun dataCreated(isLap: Boolean, lap: Int, prevValue: Long, newValue: Long)
    {
        try
        {
            val thread = Thread {
                try
                {
                    if (isLap) {
                        val indexId =
                            database.createTimeEntryModelData(lap, newValue, "")
                        createCallback?.createdModelData(indexId)
                    } else {
                        Log.v(
                            TAG,
                            "[$lap] MODIFIED FROM  $prevValue TO $newValue indexId: $indexId  dataId: $detailId"
                        )
                        editCallback?.editedModelData(
                            indexId,
                            detailId,
                            lap,
                            prevValue,
                            newValue
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            thread.start()
        }
        catch (e: Exception)
        {
            e.printStackTrace()
        }
    }

    fun dataCreateCancelled()
    {
        Log.v(TAG, "dataCreateCancelled()")
    }

    companion object
    {
        private val TAG = CreateModelData::class.java.simpleName
    }

}
