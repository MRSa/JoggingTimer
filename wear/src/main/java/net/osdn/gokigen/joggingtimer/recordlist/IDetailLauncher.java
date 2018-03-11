package net.osdn.gokigen.joggingtimer.recordlist;

public interface IDetailLauncher
{
    void launchDetail(long recordId);
    void deleteRecord(DataRecord targetRecord);
}
