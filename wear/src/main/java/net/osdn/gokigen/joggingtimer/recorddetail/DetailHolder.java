package net.osdn.gokigen.joggingtimer.recorddetail;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import net.osdn.gokigen.joggingtimer.R;

class DetailHolder  extends RecyclerView.ViewHolder
{
    private final View itemView;
    private TextView lapView;
    private TextView mainText;
    private TextView subText;

    DetailHolder(@NonNull View itemView)
    {
        super(itemView);
        lapView = itemView.findViewById(R.id.detail_lap_count);
        mainText = itemView.findViewById(R.id.label1);
        subText = itemView.findViewById(R.id.label2);
        this.itemView = itemView;
    }

    void setOnClickListener(View.OnClickListener listener)
    {
        itemView.setOnClickListener(listener);
    }

    void setLapCount(String message)
    {
        lapView.setText(message);
    }

    void setMainText(String message)
    {
        mainText.setText(message);
    }

    void setSubText(String message)
    {
        subText.setText(message);
    }

}
