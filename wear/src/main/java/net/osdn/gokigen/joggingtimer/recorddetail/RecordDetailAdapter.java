package net.osdn.gokigen.joggingtimer.recorddetail;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.osdn.gokigen.joggingtimer.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class RecordDetailAdapter  extends RecyclerView.Adapter<DetailHolder>
{
    private List<DetailRecord> list = null;

    /**
     *
     *
     */
    RecordDetailAdapter()
    {
        list = new ArrayList<>();
        list.clear();

        addRecord(new DetailRecord(R.drawable.ic_do_not_disturb_black_24dp, "XYZXYZXYZXYZXYZ", "121212121212121"));
        addRecord(new DetailRecord(R.drawable.ic_location_on_black_24dp, "ABCABCABCABCABC", "121212121212121"));
        addRecord(new DetailRecord(R.drawable.ic_build_black_24dp, "DEFDEFDEFDEFDEF", "333232324324324"));
        addRecord(new DetailRecord(R.drawable.ic_flag_black_24dp, "GHIGHIGHIGHIGHI", "898989898989898"));
        addRecord(new DetailRecord(R.drawable.ic_history_black_24dp, "JKLJKLJKLJKLJKL", "787878767676767"));

    }
    /**
     *
     *
     */
    void addRecord(DetailRecord record)
    {
        try
        {
            list.add(record);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     *
     *
     */
    void clearRecord()
    {
        try
        {
            list.clear();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public DetailHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_detail, parent,false);
        return (new DetailHolder(inflate));
    }

    @Override
    public void onBindViewHolder(DetailHolder holder, int position)
    {
        try
        {
            holder.setIconId(list.get(position).getIconId());
            holder.setMainText(list.get(position).getTitle());
            holder.setSubText(list.get(position).getDetail());
            holder.setOnClickListener(list.get(position));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount()
    {
        return (list.size());
    }
}
