package net.osdn.gokigen.joggingtimer.recordlist;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;

import net.osdn.gokigen.joggingtimer.R;

import java.util.ArrayList;
import java.util.List;

public class ListSelectionMenuAdapter  extends WearableNavigationDrawerView.WearableNavigationDrawerAdapter implements WearableNavigationDrawerView.OnItemSelectedListener
{
    private final Context context;
    private final ListSelectionMenuAdapter.ISelectedMenu callback;
    private List<ListSelectionMenuAdapter.MenuItem> menuItems = new ArrayList<>();

    ListSelectionMenuAdapter(Context context, ListSelectionMenuAdapter.ISelectedMenu callback)
    {
        this.context = context;
        this.callback = callback;

        menuItems.add(new ListSelectionMenuAdapter.MenuItem(R.id.menu_create_model, R.drawable.ic_done_black_24dp, context.getString(R.string.action_create_model)));
    }

    @Override
    public CharSequence getItemText(int pos)
    {
        try
        {
            return (menuItems.get(pos).itemText);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    @Override
    public Drawable getItemDrawable(int pos)
    {
        try
        {
            int iconId = menuItems.get(pos).iconResId;
            Drawable icon = android.support.v4.content.res.ResourcesCompat.getDrawable(context.getResources(), iconId, null);
            if (icon != null)
            {
                icon.setTint(Color.WHITE);
            }
            return (icon);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    @Override
    public int getCount()
    {
        return (menuItems.size());
    }

    private class MenuItem
    {
        private final int menuId;
        private final int iconResId;
        private final CharSequence itemText;

        private MenuItem(int menuId, int iconResId, CharSequence itemText)
        {
            this.menuId = menuId;
            this.iconResId = iconResId;
            this.itemText = itemText;
        }
    }

    @Override
    public void onItemSelected(int pos)
    {
        try
        {
            callback.selectedMenu(menuItems.get(pos).menuId);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    interface ISelectedMenu
    {
        void selectedMenu(int id);
    }
}
