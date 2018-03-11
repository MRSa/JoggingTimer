package net.osdn.gokigen.joggingtimer.recorddetail;

/**
 *
 *
 */
public interface IRecordOperation
{
    void addRecord(DetailRecord record);
    void clearRecord();

    void dataSetChangeFinished();
}
