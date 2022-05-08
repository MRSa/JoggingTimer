package net.osdn.gokigen.joggingtimer.recorddetail;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.osdn.gokigen.joggingtimer.R;

class DetailHolder  extends RecyclerView.ViewHolder
{
    private final View itemView;
    private final TextView lapView;
    private final TextView mainText;
    private final TextView subText;

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

    void setOnLongClickListener(View.OnLongClickListener listener)
    {
        itemView.setOnLongClickListener(listener);
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
