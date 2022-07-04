package net.osdn.gokigen.joggingtimer.stopwatch;

import java.util.ArrayList;

/**
 *
 *
 */
public interface IDatabaseReloadCallback
{
    void dataIsReloaded(ArrayList<Long> list);
    void referenceDataIsReloaded(int id, ArrayList<Long> list);
}
