package net.osdn.gokigen.joggingtimer.recordlist;

import android.provider.ContactsContract;
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
public class RecordSummaryAdapter extends RecyclerView.Adapter<RecordHolder>
{
    private List<DataRecord> list = null;

    /**
     *
     *
     */
    public RecordSummaryAdapter()
    {
        list = new ArrayList<>();
        list.clear();

        addRecord(new DataRecord(0, "XXXXX", "00000000"));
        addRecord(new DataRecord(R.drawable.ic_sentiment_very_dissatisfied_black_24dp, "XXXXX", "11111111"));
        addRecord(new DataRecord(R.drawable.ic_mood_bad_black_24dp, "XXXXX", "2222222"));
        addRecord(new DataRecord(R.drawable.ic_sentiment_dissatisfied_black_24dp, "XXXXX", "3333333"));
        addRecord(new DataRecord(R.drawable.ic_sentiment_neutral_black_24dp, "XXXXX", "44444444"));
        addRecord(new DataRecord(R.drawable.ic_sentiment_satisfied_black_24dp, "XXXXX", "55555555"));
        addRecord(new DataRecord(R.drawable.ic_mood_black_24dp, "XXXXX", "66666666"));
        addRecord(new DataRecord(R.drawable.ic_sentiment_very_satisfied_black_24dp, "XXXXX", "77777777"));
    }

    /**
     *
     *
     */
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
    public RecordHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.column_list, parent,false);
        return (new RecordHolder(inflate));
    }

    /**
     *
     *
     */
    @Override
    public void onBindViewHolder(RecordHolder holder, int position)
    {
        try
        {
            holder.setIconId(list.get(position).getIconId());
            holder.setMainText(list.get(position).getTitle());
            holder.setSubText(list.get(position).getDetail());
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
