package net.osdn.gokigen.joggingtimer.recordlist;

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
    RecordSummaryAdapter(IDetailLauncher detailLauncher)
    {
        list = new ArrayList<>();
        list.clear();

        addRecord(new DataRecord(0, 0, "XXXXX", "00000000", detailLauncher));
        addRecord(new DataRecord(1, R.drawable.ic_sentiment_very_dissatisfied_black_24dp, "XXXXX", "11111111", detailLauncher));
        addRecord(new DataRecord(2, R.drawable.ic_mood_bad_black_24dp, "XXXXX", "2222222", detailLauncher));
        addRecord(new DataRecord(3, R.drawable.ic_sentiment_dissatisfied_black_24dp, "XXXXX", "3333333", detailLauncher));
        addRecord(new DataRecord(4, R.drawable.ic_sentiment_neutral_black_24dp, "XXXXX", "44444444", detailLauncher));
        addRecord(new DataRecord(5, R.drawable.ic_sentiment_satisfied_black_24dp, "XXXXX", "55555555", detailLauncher));
        addRecord(new DataRecord(6, R.drawable.ic_mood_black_24dp, "XXXXX", "66666666", detailLauncher));
        addRecord(new DataRecord(7, R.drawable.ic_sentiment_very_satisfied_black_24dp, "XXXXX", "77777777", detailLauncher));
        addRecord(new DataRecord(8, R.drawable.ic_timer_off_black_24dp, "XXXXX", "77777777", detailLauncher));
        addRecord(new DataRecord(9, R.drawable.ic_timer_black_24dp, "XXXXX", "8888888", detailLauncher));
        addRecord(new DataRecord(10, R.drawable.ic_info_outline_black_24dp, "XXXXX", "99999999", detailLauncher));
        addRecord(new DataRecord(11, R.drawable.ic_history_black_24dp, "XXXXX", "12345678", detailLauncher));
        addRecord(new DataRecord(12, R.drawable.ic_block_black_24dp, "XXXXX", "87654321", detailLauncher));
        addRecord(new DataRecord(13, R.drawable.ic_do_not_disturb_black_24dp, "XXXXX", "12341234", detailLauncher));
        addRecord(new DataRecord(14, R.drawable.ic_battery_alert_black_24dp, "XXXXX", "43214321", detailLauncher));
        addRecord(new DataRecord(15, R.drawable.ic_flag_black_24dp, "XXXXX", "91919191919191", detailLauncher));

    }

    /**
     *
     *
     */
    void addRecord(DataRecord record)
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
