package net.osdn.gokigen.joggingtimer.utilities

interface IEditedModelDataCallback
{
    fun editedModelData(
        indexId: Long,
        detailId: Long,
        lapCount: Int,
        prevValue: Long,
        newValue: Long
    )
}
