package net.osdn.gokigen.joggingtimer.stopwatch

interface IClickCallback
{
    fun clickedCounter()
    fun clickedBtn1()
    fun clickedBtn2()
    fun clickedBtn3()
    fun clickedArea()
    fun pushedBtn1(): Boolean
    fun pushedBtn2(): Boolean
    fun pushedBtn3(): Boolean
    fun pushedArea(): Boolean
}
