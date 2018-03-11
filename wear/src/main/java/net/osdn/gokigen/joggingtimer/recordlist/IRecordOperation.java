package net.osdn.gokigen.joggingtimer.recordlist;

/**
 *
 *
 */
public interface IRecordOperation
{
    void addRecord(DataRecord record);
    void clearRecord();

    void removeItem(int position);
    void dataSetChangeFinished();
}
