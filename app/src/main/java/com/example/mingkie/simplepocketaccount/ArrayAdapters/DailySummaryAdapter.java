package com.example.mingkie.simplepocketaccount.ArrayAdapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mingkie.simplepocketaccount.Data.Action;
import com.example.mingkie.simplepocketaccount.R;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Created by MingKie on 7/13/2017.
 */

public class DailySummaryAdapter extends ArrayAdapter<Action> {
    private Context context;
    private List<Action> data;

    public DailySummaryAdapter(@NonNull Context context, @LayoutRes int resource,  @NonNull List<Action> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.listview_daily_summary, parent, false);
        TextView expenseOrIncome = (TextView) view.findViewById(R.id.expenseIncomeDailyListView);
        TextView amount = (TextView) view.findViewById(R.id.amountDailyListView);
        expenseOrIncome.setText(data.get(position).getActionType());
        Double truncatedAmount = BigDecimal.valueOf(data.get(position).getAmount()).setScale(3, RoundingMode.HALF_UP).doubleValue();
        amount.setText(truncatedAmount + "");
        return view;
    }
}
