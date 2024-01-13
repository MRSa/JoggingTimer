package net.osdn.gokigen.joggingtimer

interface INotifyLauncher
{
    fun launchNotify(icon: Int, title: String, description: String, isShow: Boolean)
}
