package net.osdn.gokigen.joggingtimer.storage;

/**
 *
 *
 */
public interface ITimeEntryDatabaseCallback
{
    enum OperationType
    {
        CREATED,
        APPENDED,
        FINISHED,
        //UPDATED,
    };

    void prepareFinished(boolean isReady);
    void dataEntryFinished(OperationType operationType, boolean result, long id, String title);
    void timeEntryFinished(OperationType operationType, boolean result, long indexId, long dataId);

    void modelDataEntryFinished(OperationType operationType, boolean result, long indexId, String title);

}
