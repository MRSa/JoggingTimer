package net.osdn.gokigen.joggingtimer.recordlist;

import android.support.annotation.NonNull;
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
public class RecordSummaryAdapter extends RecyclerView.Adapter<RecordHolder> implements IRecordOperation
{
    private List<DataRecord> list = null;

    /**
     *
     *
     */
    RecordSummaryAdapter()
    {
        list = new ArrayList<>();
        list.clear();
    }

    /**
     *
     *
     */
    @Override
    public void addRecord(DataRecord record)
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

    /**
     *
     *
     */
    @Override
    public @NonNull RecordHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_list, parent, false);
        return (new RecordHolder(inflate));
    }

    /**
     *
     *
     */
    @Override
    public void onBindViewHolder(@NonNull RecordHolder holder, int position)
    {
        try
        {
            holder.setIconId(list.get(position).getIconId());
            holder.setMainText(list.get(position).getTitle());
            holder.setSubText(list.get(position).getDetail());
            holder.setOnClickListener(list.get(position));
            holder.setOnLongClickListener(list.get(position));
            list.get(position).setPositionId(position);
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

    /**
     *
     *
     */
    @Override
    public long removeItem(int position)
    {
        long indexId = list.get(position).getDataId();
        list.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, list.size());
        return (indexId);
    }

    /**
     *
     *
     */
    @Override
    public void dataSetChangeFinished()
    {
        notifyDataSetChanged();
    }
}
