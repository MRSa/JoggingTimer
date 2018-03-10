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

        addRecord(new DetailRecord(10, R.drawable.ic_do_not_disturb_black_24dp, "1XYZXYZXYZXYZXYZ", "121212121212121"));
        addRecord(new DetailRecord(11, R.drawable.ic_location_on_black_24dp, "1ABCABCABCABCABC", "121212121212121"));
        addRecord(new DetailRecord(12, R.drawable.ic_build_black_24dp, "1DEFDEFDEFDEFDEF", "333232324324324"));
        addRecord(new DetailRecord(13, R.drawable.ic_flag_black_24dp, "1GHIGHIGHIGHIGHI", "898989898989898"));
        addRecord(new DetailRecord(14, R.drawable.ic_history_black_24dp, "1JKLJKLJKLJKLJKL", "787878767676767"));
        addRecord(new DetailRecord(15, R.drawable.ic_do_not_disturb_black_24dp, "1XYZXYZXYZXYZXYZ", "121212121212121"));
        addRecord(new DetailRecord(16, R.drawable.ic_location_on_black_24dp, "1ABCABCABCABCABC", "121212121212121"));
        addRecord(new DetailRecord(17, R.drawable.ic_build_black_24dp, "1DEFDEFDEFDEFDEF", "333232324324324"));
        addRecord(new DetailRecord(18, R.drawable.ic_flag_black_24dp, "1GHIGHIGHIGHIGHI", "898989898989898"));
        addRecord(new DetailRecord(19, R.drawable.ic_history_black_24dp, "1JKLJKLJKLJKLJKL", "787878767676767"));
        addRecord(new DetailRecord(20, R.drawable.ic_do_not_disturb_black_24dp, "2XYZXYZXYZXYZXYZ", "2121212121212121"));
        addRecord(new DetailRecord(21, R.drawable.ic_location_on_black_24dp, "2ABCABCABCABCABC", "2121212121212121"));
        addRecord(new DetailRecord(22, R.drawable.ic_build_black_24dp, "2DEFDEFDEFDEFDEF", "2333232324324324"));
        addRecord(new DetailRecord(23, R.drawable.ic_flag_black_24dp, "2GHIGHIGHIGHIGHI", "2898989898989898"));
        addRecord(new DetailRecord(24, R.drawable.ic_history_black_24dp, "2JKLJKLJKLJKLJKL", "2787878767676767"));
        addRecord(new DetailRecord(25, R.drawable.ic_do_not_disturb_black_24dp, "2XYZXYZXYZXYZXYZ", "2121212121212121"));
        addRecord(new DetailRecord(26, R.drawable.ic_location_on_black_24dp, "2ABCABCABCABCABC", "2121212121212121"));
        addRecord(new DetailRecord(27, R.drawable.ic_build_black_24dp, "2DEFDEFDEFDEFDEF", "2333232324324324"));
        addRecord(new DetailRecord(28, R.drawable.ic_flag_black_24dp, "2GHIGHIGHIGHIGHI", "2898989898989898"));
        addRecord(new DetailRecord(29, R.drawable.ic_history_black_24dp, "2JKLJKLJKLJKLJKL", "2787878767676767"));
        addRecord(new DetailRecord(30, R.drawable.ic_do_not_disturb_black_24dp, "3XYZXYZXYZXYZXYZ", "3121212121212121"));
        addRecord(new DetailRecord(31, R.drawable.ic_location_on_black_24dp, "3ABCABCABCABCABC", "3121212121212121"));
        addRecord(new DetailRecord(32, R.drawable.ic_build_black_24dp, "3DEFDEFDEFDEFDEF", "3333232324324324"));
        addRecord(new DetailRecord(33, R.drawable.ic_flag_black_24dp, "3GHIGHIGHIGHIGHI", "3898989898989898"));
        addRecord(new DetailRecord(34, R.drawable.ic_history_black_24dp, "3JKLJKLJKLJKLJKL", "3787878767676767"));
        addRecord(new DetailRecord(35, R.drawable.ic_do_not_disturb_black_24dp, "3XYZXYZXYZXYZXYZ", "3121212121212121"));
        addRecord(new DetailRecord(36, R.drawable.ic_location_on_black_24dp, "3ABCABCABCABCABC", "3121212121212121"));
        addRecord(new DetailRecord(37, R.drawable.ic_build_black_24dp, "3DEFDEFDEFDEFDEF", "3333232324324324"));
        addRecord(new DetailRecord(38, R.drawable.ic_flag_black_24dp, "3GHIGHIGHIGHIGHI", "3898989898989898"));
        addRecord(new DetailRecord(39, R.drawable.ic_history_black_24dp, "3JKLJKLJKLJKLJKL", "3787878767676767"));
        addRecord(new DetailRecord(40, R.drawable.ic_do_not_disturb_black_24dp, "4XYZXYZXYZXYZXYZ", "4121212121212121"));
        addRecord(new DetailRecord(41, R.drawable.ic_location_on_black_24dp, "4ABCABCABCABCABC", "4121212121212121"));
        addRecord(new DetailRecord(42, R.drawable.ic_build_black_24dp, "4DEFDEFDEFDEFDEF", "4333232324324324"));
        addRecord(new DetailRecord(43, R.drawable.ic_flag_black_24dp, "4GHIGHIGHIGHIGHI", "4898989898989898"));
        addRecord(new DetailRecord(44, R.drawable.ic_history_black_24dp, "4JKLJKLJKLJKLJKL", "4787878767676767"));
        addRecord(new DetailRecord(45, R.drawable.ic_do_not_disturb_black_24dp, "4XYZXYZXYZXYZXYZ", "4121212121212121"));
        addRecord(new DetailRecord(46, R.drawable.ic_location_on_black_24dp, "4ABCABCABCABCABC", "4121212121212121"));
        addRecord(new DetailRecord(47, R.drawable.ic_build_black_24dp, "4DEFDEFDEFDEFDEF", "4333232324324324"));
        addRecord(new DetailRecord(48, R.drawable.ic_flag_black_24dp, "4GHIGHIGHIGHIGHI", "4898989898989898"));
        addRecord(new DetailRecord(49, R.drawable.ic_history_black_24dp, "4JKLJKLJKLJKLJKL", "4787878767676767"));
        addRecord(new DetailRecord(50, R.drawable.ic_do_not_disturb_black_24dp, "5XYZXYZXYZXYZXYZ", "5121212121212121"));
        addRecord(new DetailRecord(51, R.drawable.ic_location_on_black_24dp, "5ABCABCABCABCABC", "5121212121212121"));
        addRecord(new DetailRecord(52, R.drawable.ic_build_black_24dp, "5DEFDEFDEFDEFDEF", "5333232324324324"));
        addRecord(new DetailRecord(53, R.drawable.ic_flag_black_24dp, "5GHIGHIGHIGHIGHI", "5898989898989898"));
        addRecord(new DetailRecord(54, R.drawable.ic_history_black_24dp, "5JKLJKLJKLJKLJKL", "5787878767676767"));
        addRecord(new DetailRecord(55, R.drawable.ic_do_not_disturb_black_24dp, "5XYZXYZXYZXYZXYZ", "5121212121212121"));
        addRecord(new DetailRecord(56, R.drawable.ic_location_on_black_24dp, "5ABCABCABCABCABC", "5121212121212121"));
        addRecord(new DetailRecord(57, R.drawable.ic_build_black_24dp, "5DEFDEFDEFDEFDEF", "5333232324324324"));
        addRecord(new DetailRecord(58, R.drawable.ic_flag_black_24dp, "5GHIGHIGHIGHIGHI", "5898989898989898"));
        addRecord(new DetailRecord(59, R.drawable.ic_history_black_24dp, "5JKLJKLJKLJKLJKL", "5787878767676767"));

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
