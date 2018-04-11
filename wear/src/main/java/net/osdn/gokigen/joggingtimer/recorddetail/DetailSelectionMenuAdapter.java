package net.osdn.gokigen.joggingtimer.recorddetail;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.wear.widget.drawer.WearableNavigationDrawerView;

import net.osdn.gokigen.joggingtimer.R;

import java.util.ArrayList;
import java.util.List;


/**
 *
 *
 */
public class DetailSelectionMenuAdapter extends WearableNavigationDrawerView.WearableNavigationDrawerAdapter implements WearableNavigationDrawerView.OnItemSelectedListener
{
    private final Context context;
    private final ISelectedMenu callback;
    private List<MenuItem> menuItems = new ArrayList<>();

    DetailSelectionMenuAdapter(Context context, ISelectedMenu callback)
    {
        this.context = context;
        this.callback = callback;

        menuItems.add(new MenuItem(R.id.menu_edit_title, R.drawable.ic_edit_black_24dp, context.getString(R.string.action_edit_title)));
        menuItems.add(new MenuItem(R.id.menu_set_reference, R.drawable.ic_assignment_turned_in_black_24dp, context.getString(R.string.action_set_reference)));
        menuItems.add(new MenuItem(R.id.menu_share_data, R.drawable.ic_share_black_24dp, context.getString(R.string.action_share_data)));
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
            return (android.support.v4.content.res.ResourcesCompat.getDrawable(context.getResources(), iconId, null));
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
