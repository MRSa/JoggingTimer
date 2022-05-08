package net.osdn.gokigen.joggingtimer.recorddetail;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.osdn.gokigen.joggingtimer.R;

import java.util.ArrayList;
import java.util.List;

/**
 *
 *
 */
public class RecordDetailAdapter  extends RecyclerView.Adapter<DetailHolder>  implements IRecordOperation
{
    private final List<DetailRecord> list;

    /**
     *
     *
     */
    RecordDetailAdapter()
    {
        list = new ArrayList<>();
    }

    /**
     *
     *
     */
    @Override
    public void addRecord(DetailRecord record)
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
    @Override
    public void clearRecord()
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
    public DetailRecord getRecord(int position)
    {
        try
        {
            return (list.get(position));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return (null);
    }

    /**
     *
     *
     */
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void dataSetChangeFinished()
    {
        notifyDataSetChanged();
    }

    /**
     *
     *
     */
    @Override
    public @NonNull DetailHolder onCreateViewHolder(@NonNull  ViewGroup parent, int viewType)
    {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_detail, parent,false);
        return (new DetailHolder(inflate));
    }

    /**
     *
     *
     */
    @Override
    public void onBindViewHolder(@NonNull DetailHolder holder, int position)
    {
        try
        {
            holder.setLapCount(list.get(position).getLapCount());
            holder.setMainText(list.get(position).getTitle());
            holder.setSubText(list.get(position).getDetail());
            holder.setOnClickListener(list.get(position));
            holder.setOnLongClickListener(list.get(position));
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
    @Override
    public int getItemCount()
    {
        return (list.size());
    }

}
