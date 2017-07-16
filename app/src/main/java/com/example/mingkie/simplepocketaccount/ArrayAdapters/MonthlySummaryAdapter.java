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

import com.example.mingkie.simplepocketaccount.Data.Week;
import com.example.mingkie.simplepocketaccount.R;

import java.util.List;

/**
 * Created by MingKie on 7/13/2017.
 */

public class MonthlySummaryAdapter extends ArrayAdapter<Week> {

    private Context context;
    private List<Week> data;

    public MonthlySummaryAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Week> objects) {
        super(context, resource, objects);
        this.context = context;
        this.data = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.listview_monthly_summary, parent, false);

        TextView week = (TextView) view.findViewById(R.id.weekMonthlyListView);
        TextView income = (TextView) view.findViewById(R.id.incomeMonthlyListView);
        TextView expense = (TextView) view.findViewById(R.id.expenseMonthlyListView);

        week.setText("Week " + data.get(position).getWeekOfMonth());
        income.setText(data.get(position).getIncome().getTotalAmount() + "");
        expense.setText(data.get(position).getExpense().getTotalAmount() + "");


        return view;
    }
}
