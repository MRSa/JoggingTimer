package net.osdn.gokigen.joggingtimer.recorddetail;

import net.osdn.gokigen.joggingtimer.utilities.CreateModelData;

public interface IDetailEditor
{
    void editDetailData(long indexId, long dataId, int count, long defaultMillis);

}
