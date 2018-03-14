package net.osdn.gokigen.joggingtimer.stopwatch;

import java.util.ArrayList;

public interface IClickCallback
{
    void clickedCounter();
    void clickedBtn1();
    void clickedBtn2();
    void clickedBtn3();

    boolean pushedBtn1();
    boolean pushedBtn2();
    boolean pushedBtn3();

    void dataIsReloaded(ArrayList<Long> list);

}
