package net.osdn.gokigen.joggingtimer.stopwatch.listview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

/**
 *
 */
public class LapTimeArrayAdapter  extends ArrayAdapter<LapTimeItems> implements ILapTimeHolder
{
    private LayoutInflater inflater = null;
    private final int resourceId;

    /**
     *
     */
    public LapTimeArrayAdapter(@NonNull Context context, int resource)
    {
        super(context, resource);
        this.resourceId = resource;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    /**
     *
     */
    @Override
    public @NonNull View getView(int position, View convertView, @NonNull ViewGroup parent)
    {
        View view;
        if(convertView != null)
        {
            view = convertView;
        }
        else
        {
            view = inflater.inflate(resourceId, null);
        }
        try
        {
            LapTimeItems item = getItem(position);
            if (item != null)
            {
                TextView lap1View = view.findViewWithTag("lap1");
                lap1View.setText(item.getLapCount());

                TextView lap2View = view.findViewWithTag("lap2");
                lap2View.setText(item.getMainText());

                TextView lap3View = view.findViewWithTag("lap3");
                lap3View.setText(item.getSubText());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (view);
    }

    @Override
    public void clearLapTime()
    {
        clear();
    }

    @Override
    public void addLapTime(LapTimeItems item)
    {
        add(item);
    }
}
